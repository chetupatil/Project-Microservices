package com.nokia.ace.ppa.jira;

import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nokia.ace.connectors.common.httpClient.AceConnectorHttpClient;
import com.nokia.ace.connectors.common.model.httpClient.ConnectionRequest;
import com.nokia.ace.connectors.common.model.toolDetails.ToolResponse;
import com.nokia.ace.core.project.dao.maria.BacklogTypeMasterDao;
import com.nokia.ace.core.project.dao.maria.ProjectDao;
import com.nokia.ace.ppa.dao.PmatBlSpocDao;
import com.nokia.ace.ppa.dao.PmatMasterDao;
import com.nokia.ace.ppa.dto.BASpocAndMarketDTO;
import com.nokia.ace.ppa.dto.JiraIssueFields;
import com.nokia.ace.ppa.dto.JiraIssueParentField;
import com.nokia.ace.ppa.dto.JiraIssueTypeAndKey;
import com.nokia.ace.ppa.dto.ProjectAndSCrmIDPojo;
import com.nokia.ace.ppa.dto.TypeValue;
import com.nokia.ace.utils.constants.PmatConstants;
import com.nokia.ace.utils.util.CsvWriterHelper;
import com.nokia.ace.utils.util.NotificationService;

/**
 * @author Chetana
 *
 */
@Component
public class PmatProspectHelper {

	@Autowired
	private ProjectDao projectDao;
	@Autowired
	private PmatMasterDao pmatMasterDao;
	@Autowired
	private AceConnectorHttpClient aceConnectorHttpClient;
	@Autowired
	private BacklogTypeMasterDao backlogTypeMasterDao;
	@Autowired
	private PmatBlSpocDao pmatBlSpocDao;
	@Autowired
	private NotificationService notificationService;
	@Autowired
	private CsvWriterHelper csvWriterHelper;

	@Value("${ppa.full.load.date}")
	private String ppaFullLoadFlag;

	private static final Logger LOGGER = LoggerFactory.getLogger(PmatProspectHelper.class);

	public void convertOpportunityToProspect(ToolResponse jiraToolResponse) {
		List<ProjectAndSCrmIDPojo> projectAndScrmIdPojoList = new ArrayList<>();
		List<ProjectAndSCrmIDPojo> projectAndScrmIdPojoNotUniqueList = new ArrayList<>();
		List<BASpocAndMarketDTO> baSpocList = new ArrayList<>();
		boolean safDbUpdate = false;
		boolean safJiraUpdate = false;

		try {
			List<Object[]> oppList = pmatMasterDao.findByOpportunityNumber();
			if(!CollectionUtils.isEmpty(oppList)) {
				for(Object[] objA : oppList) {
					boolean isNotUnique = false;
					if(projectAndScrmIdPojoList.size()>0) {
						isNotUnique = projectAndScrmIdPojoList.stream().anyMatch(s -> s.getsCrmId().equalsIgnoreCase(objA[1].toString()));
					}
					List<Object[]> projectOpp =  projectDao.fetchProjectId(objA[1].toString(), PmatConstants.OPPORTUNITY);
					if(!CollectionUtils.isEmpty(projectOpp)) {
						for(Object[] project : projectOpp) {
							if(!isNotUnique) {
								projectAndScrmIdPojoList.add(buildPAndScrmObj(project,objA));
							}else {
								projectAndScrmIdPojoNotUniqueList.add(buildPAndScrmObj(project,objA));
							}
						}
					}else {
//						baSpocList = mapMarketAndEmail(objA[12].toString(),objA[4].toString());
//						for(BASpocAndMarketDTO baSpocDto : baSpocList) {
//							notificationToDTM(baSpocDto.getEmails(),"nonExists",objA[1].toString(),PmatConstants.OPPORTUNITY);
//						}
					}
				}

				if(!CollectionUtils.isEmpty(projectAndScrmIdPojoList)) {
					for(ProjectAndSCrmIDPojo pAsObj : projectAndScrmIdPojoList) {
						try {
							baSpocList = mapMarketAndEmail(pAsObj.getPlsProspectScope(),pAsObj.getMarket());
							if(!pAsObj.getStatus().equalsIgnoreCase(PmatConstants.STATUS_CLOSED)) {
								safDbUpdate = updateProject(pAsObj);
								if(safDbUpdate) {
									LOGGER.info("Db update successfully : "+pAsObj.getAceProjectId());
									pAsObj = retriveOpportunityFromJira(pAsObj,jiraToolResponse);
									safJiraUpdate = editJiraIssue(pAsObj,jiraToolResponse);
								}
							}else {
								// notify the dtm for the status "closed / project started"
								for(BASpocAndMarketDTO baSpocDto : baSpocList) {
									notificationToDTM(baSpocDto.getEmails(),pAsObj,"closed",PmatConstants.PROSPECT);
								}
							}
						}catch(Exception e) {
							LOGGER.error("Exception occure while converting the opportunity to prospect from Jira update : "+e.getMessage());
							for(BASpocAndMarketDTO baSpocDto : baSpocList) {
								notificationToDTM(baSpocDto.getEmails(),pAsObj,"failure",PmatConstants.PROSPECT);
							}
						}
					}


				}
				// non unique sCRM Id
				if(!CollectionUtils.isEmpty(projectAndScrmIdPojoNotUniqueList)) {
					for(ProjectAndSCrmIDPojo pScrmObj : projectAndScrmIdPojoNotUniqueList) {
						baSpocList = mapMarketAndEmail(pScrmObj.getPlsProspectScope(),pScrmObj.getMarket());
						for(BASpocAndMarketDTO baSpocDto : baSpocList) {
							notificationToDTM(baSpocDto.getEmails(),pScrmObj,"false",PmatConstants.PROSPECT);
						}
					}
				}
				// Unique sCRM Id
				if(safJiraUpdate && safDbUpdate) {
					if(!CollectionUtils.isEmpty(projectAndScrmIdPojoList)) {
						for(ProjectAndSCrmIDPojo pScrmObj : projectAndScrmIdPojoList) {
							baSpocList = mapMarketAndEmail(pScrmObj.getPlsProspectScope(),pScrmObj.getMarket());
							for(BASpocAndMarketDTO baSpocDto : baSpocList) {
								notificationToDTM(baSpocDto.getEmails(),pScrmObj,"true",PmatConstants.PROSPECT);
							}
						}
					}
				}
			}
		}catch(Exception e) {
			LOGGER.error("Exception occure while converting the opportunity to prospect : "+e.getMessage());
		}
	}


	private void notificationToDTM(String email, ProjectAndSCrmIDPojo pScrmObj, String uniquesCrmId, String projectType) throws Exception {

		Map<String, Object> model = new HashMap<>();
		model.put("status",pScrmObj.getStatus());
		model.put("sCrmId",pScrmObj.getsCrmId());
		model.put("uniquesCrmId",uniquesCrmId);
		model.put("projectType",projectType);
		model.put("safProjectId",pScrmObj.getAceProjectId());
		model.put("ppaId",pScrmObj.getPpaId());
		notificationService.notifyDTM(email,model);
	}

	private void notificationToDTM(String email, String uniquesCrmId, String sCrmId,String projectType) throws Exception {

		Map<String, Object> model = new HashMap<>();
		model.put("status","");
		model.put("sCrmId",sCrmId);
		model.put("uniquesCrmId",uniquesCrmId);
		model.put("projectType",projectType);
		model.put("safProjectId","");
		model.put("ppaId","");
		notificationService.notifyDTM(email,model);
	}


	private ProjectAndSCrmIDPojo retriveOpportunityFromJira(ProjectAndSCrmIDPojo pAsObj, ToolResponse jiraToolResponse) {
		JSONArray responseJson = null;
		ConnectionRequest request = new ConnectionRequest();
		Map<String, String> credentials = new HashMap<>();
		List<JiraIssueTypeAndKey> issueTypeList = new ArrayList<>();
		try {
			credentials.put("userName", jiraToolResponse.getUserName());
			credentials.put("password", jiraToolResponse.getPassword());
			StringBuilder apiStr = new StringBuilder();
			apiStr.append(jiraToolResponse.getInstanceUrl());
			apiStr.append(PmatConstants.JIRA_SEARCH_ISSUE_URL);
			//apiStr.append(PmatConstants.JIRA_SCRM_ID + pAsObj.getsCrmId() +PmatConstants.JIRA_SAF_PROJECT_ID + pAsObj.getAceProjectId()+"'");
			apiStr.append(PmatConstants.ACE_PROJECT_ID + pAsObj.getAceProjectId()+"'");
			apiStr.append(PmatConstants.SEARCH_CUSTOM_JIRA_ISSUE_FIELDS);
			request = request.getConnection(apiStr.toString(), credentials, null, null, PmatConstants.APPLICATION_JSON, null);
			Map<String, Integer> result = aceConnectorHttpClient.get(request, null, null);

			for(Entry<String, Integer> a : result.entrySet()) {
				JSONObject obj = new JSONObject(a.getKey());
				responseJson = obj.getJSONArray("issues");
				if(responseJson.length()>0) {
					for(Object jObj : responseJson) {
						JiraIssueTypeAndKey issueTypeObj = new JiraIssueTypeAndKey();
						JSONObject jsonObj = (JSONObject) jObj;
						JSONObject issuetype = null;
						JSONObject fields = jsonObj.getJSONObject("fields");
						if(fields!=null) {
							issuetype = fields.getJSONObject("issuetype");
						}
						issueTypeObj.setKey(jsonObj.getString("key"));
						if(issuetype!=null) {
							issueTypeObj.setIssueType(issuetype.getString("name"));
						}
						issueTypeList.add(issueTypeObj);
					}
				}
			}
		}catch(Exception e) {
			LOGGER.error("Exception occure while fetching the project keys : "+e.getMessage());
		}
		if(!CollectionUtils.isEmpty(issueTypeList)) {
			pAsObj.setJiraIssyeTypeAndKeyList(issueTypeList);
		}
		return pAsObj;
	}

	private Boolean editJiraIssue(ProjectAndSCrmIDPojo retrivesCRM, ToolResponse jiraToolResponse) {
		int issueEditCount = 0;
		int retriveKeys = 0;
		try {
			List<JiraIssueTypeAndKey> keys = retrivesCRM.getJiraIssyeTypeAndKeyList();
			retriveKeys = keys.size();
			for(JiraIssueTypeAndKey key : keys) {
				LOGGER.info("Jira update is inprogress : "+key);
				JiraIssueParentField modifyJiraIssue = new JiraIssueParentField();
				JiraIssueFields jiraIssField = new JiraIssueFields();

				TypeValue tyVal = new TypeValue();
				tyVal.setValue(PmatConstants.PROSPECT);
				jiraIssField.setProjectType(tyVal);

				TypeValue tyValMarket = new TypeValue();
				tyValMarket.setValue(retrivesCRM.getMarket());
				jiraIssField.setMarket(tyValMarket);

				if(key.getIssueType().equals("Epic")) {
					jiraIssField.setMarketUnit(retrivesCRM.getMarketUnit());
					List<String> bgdmName = new ArrayList<>();
					bgdmName.add(retrivesCRM.getBgdmName());
					jiraIssField.setBgdmName(bgdmName);
					jiraIssField.setPpaId(retrivesCRM.getPpaId());
					jiraIssField.setCtName(retrivesCRM.getCt());
					TypeValue tyValRisk = new TypeValue();
					tyValRisk.setValue(retrivesCRM.getProjectRisk());
					jiraIssField.setRisk(tyValRisk);
					jiraIssField.setNgseetUrl(retrivesCRM.getNgseetUrl());
				}
				if(key.getIssueType().equalsIgnoreCase("Epic") || key.getIssueType().equalsIgnoreCase("User Story")){
					jiraIssField.setActualStartDate(retrivesCRM.getServiceStartDate());
					jiraIssField.setActualEndDate(retrivesCRM.getServiceEndDate());
				}
				modifyJiraIssue.setFields(jiraIssField);
				try {
					StringBuilder apiStr = new StringBuilder();
					apiStr.append(jiraToolResponse.getInstanceUrl());
					apiStr.append(PmatConstants.MODIFY_ISSUE);
					apiStr.append(key.getKey());
					Map<String, String> credentials = new HashMap<>();
					credentials.put("userName", jiraToolResponse.getUserName());
					credentials.put("password", jiraToolResponse.getPassword());

					ObjectMapper mapper = new ObjectMapper();
					mapper.setSerializationInclusion(Include.NON_NULL);
					mapper.setSerializationInclusion(Include.NON_EMPTY);
					String postString = "";
					postString = mapper.writeValueAsString(modifyJiraIssue);
					ConnectionRequest request = new ConnectionRequest();
					request = request.getConnection(apiStr.toString(), credentials, null, postString,PmatConstants.APPLICATION_JSON, null);
					Map<String, Object> result = aceConnectorHttpClient.put(request, null, null);
					if (result.containsValue(200) || result.containsValue(201) || result.containsValue(204)|| result.containsValue(HttpURLConnection.HTTP_CREATED)) {
						issueEditCount++;
						LOGGER.info("Jira update success : "+key);
					}
				}catch(Exception e) {
					LOGGER.info("Jira update is failed");
					LOGGER.error(key + " = Exception : "+e.getMessage());

				}
			}
		}catch(Exception e) {
			LOGGER.error("Exception occure while updating the jira issue : "+e.getMessage());
		}
		return issueEditCount!=0 && retriveKeys!=0 && issueEditCount==retriveKeys;
	}

	private ProjectAndSCrmIDPojo buildPAndScrmObj(Object[] safProject,Object[] objA) throws Exception {

		ProjectAndSCrmIDPojo pAndScrmIdObj = new ProjectAndSCrmIDPojo();
		pAndScrmIdObj.setAceProjectId(safProject[0].toString());
		pAndScrmIdObj.setPpaId(objA[0].toString());
		pAndScrmIdObj.setsCrmId(objA[1].toString());
		pAndScrmIdObj.setOpportunityName(objA[2].toString());
		pAndScrmIdObj.setBgdmName(objA[3].toString());
		pAndScrmIdObj.setMarket(objA[4].toString());
		pAndScrmIdObj.setMarketUnit(objA[5].toString());
		pAndScrmIdObj.setCbt(objA[6].toString());
		pAndScrmIdObj.setCt(objA[7].toString());
		pAndScrmIdObj.setProjectRisk(objA[8].toString());
		pAndScrmIdObj.setStatus(objA[9].toString());
		pAndScrmIdObj.setComments(objA[10].toString());
		pAndScrmIdObj.setEnoviaProductRelease(objA[11].toString());
		pAndScrmIdObj.setPlsProspectScope(objA[12].toString());
		pAndScrmIdObj.setNgseetUrl(objA[13].toString());
		pAndScrmIdObj.setPpaCreatedBy(objA[14].toString());
		pAndScrmIdObj.setPpaModifiedBy(objA[15].toString());
		pAndScrmIdObj.setPpaCreated(objA[16].toString());
		pAndScrmIdObj.setPpaModified(objA[17].toString());
		
		Map<String,String> dateMap = csvWriterHelper.diffServiceEndDate(safProject[1].toString(), safProject[2].toString(), objA[18].toString());
		pAndScrmIdObj.setServiceEndDate(dateMap.get("endDate"));
		pAndScrmIdObj.setServiceStartDate(dateMap.get("startDate"));
		return pAndScrmIdObj;
	}



	private List<BASpocAndMarketDTO> mapMarketAndEmail(String bl,String market) {

		List<BASpocAndMarketDTO> baSpocList = new ArrayList<BASpocAndMarketDTO>();

		List<String> blList = csvWriterHelper.fetchPlProspectScope(bl);
		for(String blObj :blList) {
			List<Object[]> baSpocObj = pmatBlSpocDao.fetchBaSpocEmailBasedOnBl(blObj);
			BASpocAndMarketDTO baSpoc = new BASpocAndMarketDTO();

			switch(blObj) {
			case "DM":
				baSpoc.setBaName(blObj);
				baSpoc = mapCombinedMarket(baSpocObj,market,baSpoc);
			case "Analytics":
				baSpoc.setBaName(blObj);
				baSpoc = mapCombinedMarket(baSpocObj,market,baSpoc);
			case "Charging":
				baSpoc.setBaName(blObj);
				baSpoc = mapCombinedMarket(baSpocObj,market,baSpoc);
			case "DO":
				baSpoc.setBaName(blObj);
				baSpoc = mapCombinedMarket(baSpocObj,market,baSpoc);
			case "Mediation":
				baSpoc.setBaName(blObj);
				baSpoc = mapCombinedMarket(baSpocObj,market,baSpoc);
			case "Security":
				baSpoc.setBaName(blObj);
				baSpoc = mapCombinedMarket(baSpocObj,market,baSpoc);
			case "CC&DDI":
				baSpoc.setBaName(blObj);
				baSpoc = mapCombinedMarket(baSpocObj,market,baSpoc);
			case "S&S":
				baSpoc.setBaName(blObj);
				baSpoc = mapCombinedMarket(baSpocObj,market,baSpoc);
			}
			baSpocList.add(baSpoc);
		}

		return baSpocList;
	}




	private BASpocAndMarketDTO mapCombinedMarket(List<Object[]> baSpocObj,String market, BASpocAndMarketDTO baSpoc){

		switch(market) {
		case PmatConstants.EUR : 
			baSpoc.setMarket(market);
			baSpoc.setEmails(baSpocObj.get(0)[2].toString());
			break;
		case PmatConstants.NAM : 
			baSpoc.setMarket(market);
			baSpoc.setEmails(baSpocObj.get(0)[3].toString());
			break;
		case PmatConstants.LAT : 
			baSpoc.setMarket(market);
			baSpoc.setEmails(baSpocObj.get(0)[4].toString());
			break;
		case PmatConstants.MEA : 
			baSpoc.setMarket(market);
			baSpoc.setEmails(baSpocObj.get(0)[5].toString());
			break;
		case PmatConstants.APJ : 
			baSpoc.setMarket(market);
			baSpoc.setEmails(baSpocObj.get(0)[6].toString());
			break;
		case PmatConstants.IND : 
			baSpoc.setMarket(market);
			baSpoc.setEmails(baSpocObj.get(0)[7].toString());
			break;
		case PmatConstants.GCHN : 
			baSpoc.setMarket(market);
			baSpoc.setEmails(baSpocObj.get(0)[8].toString());
			break;

		}
		return baSpoc;
	}
	private boolean updateProject(ProjectAndSCrmIDPojo pAsObj) {
		try {
			Integer backLogId = backlogTypeMasterDao.findBacklogTypeByName(PmatConstants.PROSPECT);
			if(backLogId!=0) {
				projectDao.updateProjectBasedOnProspect(backLogId, pAsObj.getsCrmId(),pAsObj.getPpaId(),pAsObj.getOpportunityName(),pAsObj.getBgdmName(),pAsObj.getMarketUnit(),pAsObj.getCbt(),pAsObj.getCt(),pAsObj.getProjectRisk(),pAsObj.getProjectStatus(),pAsObj.getNgseetUrl(),pAsObj.getComments(),pAsObj.getPlsProspectScope(),pAsObj.getEnoviaProductRelease(),pAsObj.getPpaCreatedBy(),pAsObj.getPpaModifiedBy(),pAsObj.getPpaCreated(),pAsObj.getPpaModified(),pAsObj.getAceProjectId());
				return true;
			}
		}catch(Exception e) {
			LOGGER.error("Exception occure while updating in saf db : "+e.getMessage());
		}
		return false;
	}

	public void updateProjectDate(ToolResponse jiraToolResponse) {
		List<Object[]> prospects = new ArrayList<>();
		try {
			LOGGER.info("ppaFullLoadFlag "+ppaFullLoadFlag);

			if(ppaFullLoadFlag.equalsIgnoreCase(PmatConstants.PPA_FULL_LOAD_FLAG_COMPLETE)) {

				prospects = projectDao.fetchProspects(PmatConstants.PROSPECT);

			}else if(ppaFullLoadFlag.equalsIgnoreCase(PmatConstants.PPA_FULL_LOAD_FLAG_PARTIAL)){

				SimpleDateFormat sdf = new SimpleDateFormat(PmatConstants.dateFormate1);

				Date dateYesterday = new Date(new Date().getTime() - PmatConstants.MILLIS_IN_A_DAY);
				String dateYesterdayS = sdf.format(dateYesterday);
				Date dateToday = new Date(new Date().getTime());
				String dateTodayS = sdf.format(dateToday);

				List<Object[]> ppaIds = pmatMasterDao.fetchLatestStartDate(dateYesterdayS, dateTodayS);

				if(!CollectionUtils.isEmpty(ppaIds)) {

					for(Object[] ppaId : ppaIds) {
						prospects.addAll(projectDao.fetchProspectsBasedOnPpaId(PmatConstants.PROSPECT, ppaId[0].toString()));
					}
				}
			}
			if(!CollectionUtils.isEmpty(prospects)) {
				for(Object[] pObj : prospects) {
					List<BASpocAndMarketDTO> baSpocList = new ArrayList<>();

					List<Object[]> ppaInfo = pmatMasterDao.fetchStartDate(pObj[3].toString());
					if(!CollectionUtils.isEmpty(ppaInfo)) {
						String startDate = ppaInfo.get(0)[0].toString();

						Map<String, String> dates = csvWriterHelper.diffServiceEndDate(pObj[5].toString(), pObj[6].toString(), startDate);
//
//						SimpleDateFormat sdf = new SimpleDateFormat(PmatConstants.dateFormate1);
//						Date dataFromDump = sdf.parse(startDate);
//
//						SimpleDateFormat sdf1 = new SimpleDateFormat(PmatConstants.JiraDateFormate);
//						startDate = sdf1.format(dataFromDump);
//
//						LOGGER.info("pObj[0].toString() --> "+pObj[0].toString());
						
						String endDate = dates.get("endDate");
						startDate = dates.get("startDate");

						projectDao.updateDates(startDate,endDate,pObj[0].toString());

						List<JiraIssueTypeAndKey> jiraKeys = fetchJiraKeys(pObj[0].toString(),jiraToolResponse);
						
						boolean safJiraUpdate = editJiraDates(jiraKeys,jiraToolResponse,startDate,endDate);

						// Notify the ba Spoc for date updates.
						if(safJiraUpdate) {
							baSpocList = mapMarketAndEmail(ppaInfo.get(0)[1].toString(),ppaInfo.get(0)[2].toString());
							for(BASpocAndMarketDTO baSpocDto : baSpocList) {
								notificationToDTM(baSpocDto.getEmails(),"none",pObj[2].toString(),PmatConstants.PROSPECT);
							}
						}
					}
				}
			}
		}catch(Exception e) {
			LOGGER.error("Exception occure while updating the start date : "+e.getMessage());
		}
	}

	private List<JiraIssueTypeAndKey> fetchJiraKeys(String aceProjectId,ToolResponse jiraToolResponse) {
		JSONArray responseJson = null;
		ConnectionRequest request = new ConnectionRequest();
		Map<String, String> credentials = new HashMap<>();
		List<JiraIssueTypeAndKey> issueTypeList = new ArrayList<>();
		try {
			credentials.put("userName", jiraToolResponse.getUserName());
			credentials.put("password", jiraToolResponse.getPassword());
			StringBuilder apiStr = new StringBuilder();
			apiStr.append(jiraToolResponse.getInstanceUrl());
			apiStr.append(PmatConstants.JIRA_SEARCH_ISSUE_URL);
			apiStr.append(PmatConstants.ACE_PROJECT_ID + aceProjectId +"'");
			apiStr.append(PmatConstants.SEARCH_CUSTOM_JIRA_ISSUE_FIELDS);
			request = request.getConnection(apiStr.toString(), credentials, null, null, PmatConstants.APPLICATION_JSON, null);
			Map<String, Integer> result = aceConnectorHttpClient.get(request, null, null);

			for(Entry<String, Integer> a : result.entrySet()) {
				JSONObject obj = new JSONObject(a.getKey());
				responseJson = obj.getJSONArray("issues");
				if(responseJson.length()>0) {
					for(Object jObj : responseJson) {
						JiraIssueTypeAndKey issueTypeObj = new JiraIssueTypeAndKey();
						JSONObject jsonObj = (JSONObject) jObj;
						JSONObject issuetype = null;
						JSONObject fields = jsonObj.getJSONObject("fields");
						if(fields!=null) {
							issuetype = fields.getJSONObject("issuetype");
						}
						issueTypeObj.setKey(jsonObj.getString("key"));
						if(issuetype!=null) {
							issueTypeObj.setIssueType(issuetype.getString("name"));
						}
						issueTypeList.add(issueTypeObj);
					}
				}
			}
		}catch(Exception e) {
			LOGGER.error("Exception occure while fetching the project keys : "+e.getMessage());
		}
		return issueTypeList;
	}

	private boolean editJiraDates(List<JiraIssueTypeAndKey> jiraKeys,ToolResponse jiraToolResponse,String startDate, String endDate) {
		int countJiraEdit = 0;
		try {
			for(JiraIssueTypeAndKey key : jiraKeys) {
				LOGGER.info(" Edit Jira Dates : " + key.getKey());
				JiraIssueParentField modifyJiraIssue = new JiraIssueParentField();
				JiraIssueFields jiraIssField = new JiraIssueFields();
				if(key.getIssueType().equalsIgnoreCase("Epic") || key.getIssueType().equalsIgnoreCase("User Story")) {
					jiraIssField.setActualStartDate(startDate);
					jiraIssField.setActualEndDate(endDate);
				}
				modifyJiraIssue.setFields(jiraIssField);
				try {
					StringBuilder apiStr = new StringBuilder();
					apiStr.append(jiraToolResponse.getInstanceUrl());
					apiStr.append(PmatConstants.MODIFY_ISSUE);
					apiStr.append(key.getKey());
					Map<String, String> credentials = new HashMap<>();
					credentials.put("userName", jiraToolResponse.getUserName());
					credentials.put("password", jiraToolResponse.getPassword());

					ObjectMapper mapper = new ObjectMapper();
					mapper.setSerializationInclusion(Include.NON_NULL);
					mapper.setSerializationInclusion(Include.NON_EMPTY);
					String postString = "";
					postString = mapper.writeValueAsString(modifyJiraIssue);
					ConnectionRequest request = new ConnectionRequest();
					request = request.getConnection(apiStr.toString(), credentials, null, postString,PmatConstants.APPLICATION_JSON, null);
					Map<String, Object> result = aceConnectorHttpClient.put(request, null, null);

					if (result.containsValue(200) || result.containsValue(201) ||
							result.containsValue(204)|| result.containsValue(HttpURLConnection.HTTP_CREATED)) {
						LOGGER.info(" Success: " + key.getKey());
						countJiraEdit ++;
					}
				}catch(Exception e) {
					LOGGER.error(key + " = Exception : "+e.getMessage());
				}
			}

		}catch(Exception e) {
			LOGGER.error("Exception occure while updating the service start date: "+e.getMessage());
		}
		return countJiraEdit!=0 && jiraKeys.size() >=countJiraEdit;
	}
}

