package com.nokia.ace.ppa.jira;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.jgit.util.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.nokia.ace.connectors.common.environmentVariables.EnvironmentVariables;
import com.nokia.ace.connectors.common.environmentVariables.ReadEnvironmentVariables;
import com.nokia.ace.connectors.common.httpClient.AceConnectorHttpClient;
import com.nokia.ace.connectors.common.model.httpClient.ConnectionRequest;
import com.nokia.ace.connectors.common.model.toolDetails.ToolResponse;
import com.nokia.ace.connectors.common.toolDetails.ToolDetails;
import com.nokia.ace.core.project.dao.maria.ProjectDao;
import com.nokia.ace.ppa.dao.PmatMasterDao;
import com.nokia.ace.ppa.dto.PmatAggregateEffortsDTO;
import com.nokia.ace.ppa.dto.PmatTimeDTO;
import com.nokia.ace.ppa.dto.PmatWorkPackageDTO;
import com.nokia.ace.ppa.dto.ProjectAndSCrmIDPojo;
import com.nokia.ace.utils.constants.PmatConstants;
import com.nokia.ace.utils.util.CsvWriterHelper;

/**
 * @author Chetana
 *
 */
@Service
public class PmatJiraHelper {

	@Autowired
	private AceConnectorHttpClient aceConnectorHttpClient;
	@Autowired
	private PmatMasterDao pmatMasterDao;
	@Autowired
	ToolDetails toolDetailsImpl;
	@Autowired
	private CsvWriterHelper csvWriterHelper;
	@Autowired
	private ProjectDao projectDao;
	@Autowired
	private ReadEnvironmentVariables readEnvironmentVariables;

	private static final Logger LOGGER = LoggerFactory.getLogger(PmatJiraHelper.class);
	private static final String SAF_DUMP_FILE_NAME = "SAF_DUMP.csv";
	private static final String SAF_DUMP_OLD_FILE_NAME = "SAF_DUMP_Pervious.csv";

	public List<ProjectAndSCrmIDPojo> fetchsCrmId() {
		List<ProjectAndSCrmIDPojo> projectAndScrmIdPojoList = new ArrayList<>();
		try {
			List<Object[]> oppList = pmatMasterDao.findByOpportunityNumber();

			for(String type : getTypes()) {
				for(Object[] objA : oppList) {
					List<Object[]> projectOpp =  projectDao.fetchProjectIdAndScrmIdAndProjectType(objA[1].toString(), type);
					if(!projectOpp.isEmpty()) {
						for(Object[] projectOppObj: projectOpp) {
							ProjectAndSCrmIDPojo pAndScrmIdObj = new ProjectAndSCrmIDPojo();
							pAndScrmIdObj.setAceProjectId(projectOppObj[0].toString());
							pAndScrmIdObj.setsCrmId(projectOppObj[1].toString());
							pAndScrmIdObj.setPpaId(objA[0].toString());
							pAndScrmIdObj.setProjectType(type);
							pAndScrmIdObj.setPlsProspectScope(objA[12].toString());
							projectAndScrmIdPojoList.add(pAndScrmIdObj);
						}
					}
				}
			}
		}catch(Exception e) {
			LOGGER.error("Exception while FETCH sCRM Id, {} "+e.getMessage());
		}
		return projectAndScrmIdPojoList;
	}
	public EnvironmentVariables readEnvVariables() {
		EnvironmentVariables envVariables = new EnvironmentVariables();
		envVariables = readEnvironmentVariables.readEnvVariables(envVariables);
		return envVariables;
	}
	public String retrieveOpportunitiesAndSaveIntoFile(List<ProjectAndSCrmIDPojo> projectAndScrmIdPojoList, ToolResponse toolResponse) {
		JSONArray responseJson = null;
		File file=null;
		FileWriter csvWriter = null;
		try {	
			file = csvWriterHelper.createFileInResource(SAF_DUMP_FILE_NAME);
			file.setWritable(true);
			file.setReadable(true);
			csvWriter = new FileWriter(file);

			csvWriterHelper.writeDumpHeaderIntoCSV(csvWriter);
			List<PmatWorkPackageDTO> pmatWpListOld =  fetchPerviousFileFromResource();

			if(!CollectionUtils.isEmpty(projectAndScrmIdPojoList)) {

				for(ProjectAndSCrmIDPojo objOpp: projectAndScrmIdPojoList) {

					ConnectionRequest request = new ConnectionRequest();
					Map<String, String> credentials = new HashMap<>();
					credentials.put("userName", toolResponse.getUserName());
					credentials.put("password", toolResponse.getPassword());

					StringBuilder apiStr = new StringBuilder();
					apiStr.append(toolResponse.getInstanceUrl());
					apiStr.append(PmatConstants.JIRA_SEARCH_ISSUE_URL);
					apiStr.append(PmatConstants.JIRA_FILTER_PARA_WP_01 + objOpp.getProjectType() + PmatConstants.JIRA_FILTER_PARA_WP_02 + objOpp.getsCrmId()+ "'" +PmatConstants.JIRA_FILTER_PARA_WP_03 + objOpp.getAceProjectId() +"'");
					apiStr.append(PmatConstants.CUSTOM_JIRA_FIELDS);
					request = request.getConnection(apiStr.toString(), credentials, null, null, null, null);
					Map<String, Integer> result = aceConnectorHttpClient.get(request, null, null);

					for(Entry<String, Integer> a : result.entrySet()) {
						JSONObject obj = new JSONObject(a.getKey());
						responseJson = obj.getJSONArray("issues");
						if(responseJson.length()>0) {
							List<PmatWorkPackageDTO> pWp = buildPmatWorkPackage(responseJson,objOpp);
							pWp = validateCurrentAndPerviousFile(pmatWpListOld,pWp,objOpp);
							csvWriterHelper.writeDumpDataIntoCsv(csvWriter,pWp);
						}
					}
				}
			}

		} catch (Exception e) {
			LOGGER.error("Exception while searching for jira issues, {} "+e.getMessage());
		}finally {
			if(csvWriter!=null) {
				try {
					csvWriter.flush();
					csvWriter.close();
				} catch (Exception e) {
					LOGGER.error(e.getMessage());
				}
			}

		}
		return file.getAbsolutePath();
	}

	private String checkJsonObjIsEmpty(JSONObject field, String fieldName) {
		if(!field.isNull(fieldName)) {
			return field.getJSONObject(fieldName).getString("value");
		}
		return "";
	}

	private List<PmatWorkPackageDTO> buildPmatWorkPackage(JSONArray pmatWpJson, ProjectAndSCrmIDPojo objOpp) {
		List<PmatWorkPackageDTO> pmatWpList = new ArrayList<PmatWorkPackageDTO>();
		try {

			Map<String,PmatWorkPackageDTO> pmatWpMap = new HashMap<String,PmatWorkPackageDTO>();
			Map<String,String> lastModifiedFinal = new HashMap<>();
			Map<String,String> wpEndDateFinal = new HashMap<>();
			Map<String,String> wpStartDateFinal = new HashMap<>();
			Map<String,String> aggregateEfforsFinal = new HashMap<>();
			List<PmatAggregateEffortsDTO> pmatAggregateList = new ArrayList<>();
			List<PmatTimeDTO> lastModifiedList = new ArrayList<>();
			List<PmatTimeDTO> wpEndDataList = new ArrayList<>();
			List<PmatTimeDTO> wpStartDataList = new ArrayList<>();

			for(Object pmatObj : pmatWpJson) {

				PmatWorkPackageDTO pmatWp = new PmatWorkPackageDTO();
				PmatTimeDTO lastModifiedPojo = new PmatTimeDTO();
				PmatTimeDTO wpEndDatePojo = new PmatTimeDTO();
				PmatTimeDTO wpStartDatePojo = new PmatTimeDTO();

				PmatAggregateEffortsDTO pmatAggregateObj = new PmatAggregateEffortsDTO();
				JSONObject jObj = (JSONObject)pmatObj;
				JSONObject field = jObj.getJSONObject(PmatConstants.FIELDS);

				String workpackage = checkStringIsEmpty(field,PmatConstants.WORKPACKAGE);
				if(!StringUtils.isEmptyOrNull(workpackage) &&  !workpackage.contains("---")) {
					//if(!StringUtils.isEmptyOrNull(workpackage)) {
					if(!pmatWpMap.isEmpty() && pmatWpMap.get(workpackage) != null) {
						for(PmatTimeDTO lmObj : lastModifiedList) {
							if(lmObj.getWorkpackage().equalsIgnoreCase(workpackage)) {
								lmObj.getDate().add(convertStringToLong(checkStringIsEmptyForIntData(field,PmatConstants.UPDATED)));
							}
						}
						for(PmatTimeDTO weObj : wpEndDataList) {
							if(weObj.getWorkpackage().equalsIgnoreCase(workpackage)) {
								weObj.getDate().add(convertStringToLong(checkStringIsEmptyForIntData(field,PmatConstants.CREATED)));
							}
						}
						for(PmatTimeDTO wsObj : wpStartDataList) {
							if(wsObj.getWorkpackage().equalsIgnoreCase(workpackage)) {
								wsObj.getDate().add(convertStringToLong(checkStringIsEmptyForIntData(field,PmatConstants.CREATED)));
							}
						}
						for(PmatAggregateEffortsDTO pmatObj1 : pmatAggregateList) {
							if(pmatObj1.getWorkpackageName().equalsIgnoreCase(workpackage)) {
								Integer estCountAdd = pmatObj1.getEstimationCount() + checkIntegerIsEmpty(field,PmatConstants.AGGREGATE_ESTIMATION_TIME);
								pmatObj1.setEstimationCount(estCountAdd);
							}
						}
					}else {
						String safProjectId = checkStringIsEmpty(field,PmatConstants.SAF_PROJECT_ID);
						pmatWp.setSafUniqueId(fetchUniqueId(workpackage,safProjectId));
						pmatWp.setSafProjectId(safProjectId);
						pmatWp.setGic(checkStringIsEmpty(field,PmatConstants.GIC));
						pmatWp.setOpportunityNumber(checkStringIsEmpty(field,PmatConstants.OPPORTUNITY_NUMBER));
						pmatWp.setPpaId(objOpp.getPpaId());
						pmatWp.setProspectType(checkJsonObjIsEmpty(field,PmatConstants.PROSPECT_TYPE));
						pmatWp.setServiceOffering(checkStringIsEmpty(field,PmatConstants.SERVICE_OFFERING));
						pmatWp.setServiceSubType(checkStringIsEmpty(field,PmatConstants.SERVICE_SUB_TYPE).isEmpty()?"":getTripleValue(field.getString(PmatConstants.SERVICE_SUB_TYPE)));
						List<String> baUnits = csvWriterHelper.fetchPlProspectScope(objOpp.getPlsProspectScope());
						
						String bussinessUnit = "";
						if(!CollectionUtils.isEmpty(baUnits)) {
							for(String ba: baUnits) {
								bussinessUnit=bussinessUnit +ba +" ";
							}
//							if(!StringUtils.isEmptyOrNull(bussinessUnit)) {
//								//bussinessUnit = bussinessUnit.substring(0, bussinessUnit.length() - 1);
//							}
						}
						
						pmatWp.setBussinessUnit(bussinessUnit);
						pmatWp.setIsDeleted("false");

						//						String bussinessLine = checkStringIsEmpty(field,PmatConstants.BUSSINESS_LINE);
						//						if(!bussinessLine.isEmpty()) {
						//							bussinessLine = bussinessLine.substring(0, 5);
						//						}
						//						pmatWp.setBussinessLine(bussinessLine);

						pmatWp.setBussinessLine("BA");

						pmatWp.setWorkpackageName(workpackage);
						pmatAggregateObj.setWorkpackageName(workpackage);
						pmatAggregateObj.setEstimationCount(checkIntegerIsEmpty(field,PmatConstants.AGGREGATE_ESTIMATION_TIME));

						lastModifiedPojo.setWorkpackage(workpackage);
						Set<Long> lmL = new HashSet<>();
						lmL.add(convertStringToLong(checkStringIsEmptyForIntData(field,PmatConstants.UPDATED)));
						lastModifiedPojo.setDate(lmL);

						wpEndDatePojo.setWorkpackage(workpackage);
						Set<Long> weL = new HashSet<>();
						weL.add(convertStringToLong(checkStringIsEmptyForIntData(field,PmatConstants.CREATED)));
						wpEndDatePojo.setDate(weL);

						wpStartDatePojo.setWorkpackage(workpackage);
						Set<Long> wsL = new HashSet<>();
						wsL.add(convertStringToLong(checkStringIsEmptyForIntData(field,PmatConstants.CREATED)));
						wpStartDatePojo.setDate(wsL);

						lastModifiedList.add(lastModifiedPojo);
						wpEndDataList.add(wpStartDatePojo);
						wpStartDataList.add(wpStartDatePojo);
						pmatAggregateList.add(pmatAggregateObj);

						pmatWpMap.put(workpackage, pmatWp);
					}	
				}
			}

			if(!lastModifiedList.isEmpty()) {
				lastModifiedList.stream().forEach(i ->{
					try {
						lastModifiedFinal.put(i.getWorkpackage(), csvWriterHelper.lastModified(i.getDate()));
					} catch (Exception e) {

					}
				});
			}

			if(!wpEndDataList.isEmpty()) {
				wpEndDataList.stream().forEach(i ->{
					try {
						wpEndDateFinal.put(i.getWorkpackage(), csvWriterHelper.wpEndDate(i.getDate()));
					} catch (Exception e) {

					}
				});
			}
			if(!wpStartDataList.isEmpty()) {
				wpStartDataList.stream().forEach(i ->{
					try {
						wpStartDateFinal.put(i.getWorkpackage(), csvWriterHelper.wpStartDate(i.getDate()));
					} catch (Exception e) {

					}
				});
			}

			if(!pmatAggregateList.isEmpty()) {
				for(PmatAggregateEffortsDTO obj:pmatAggregateList) {
					aggregateEfforsFinal.put(obj.getWorkpackageName(), String.valueOf(obj.getEstimationCount()));
				}

			}



			if(!pmatWpMap.isEmpty()) {
				pmatWpMap.entrySet().forEach(i ->{
					if(!StringUtils.isEmptyOrNull(i.getValue().getWorkpackageName())) {
						if(lastModifiedFinal.containsKey(i.getKey())) {
							i.getValue().setLastModifiedDate(lastModifiedFinal.get(i.getKey()));
						}
						if(wpEndDateFinal.containsKey(i.getKey())) {
							i.getValue().setWpEndDate(wpEndDateFinal.get(i.getKey()));
						}
						if(wpStartDateFinal.containsKey(i.getKey())) {
							i.getValue().setWpStartDate(wpStartDateFinal.get(i.getKey()));
						}
						if(aggregateEfforsFinal.containsKey(i.getKey())) {
							i.getValue().setAggregateEfforts(csvWriterHelper.convertHoursToDays(aggregateEfforsFinal.get(i.getKey())));
						}
						pmatWpList.add(i.getValue());
					}
				});
			}
			//}
		}catch(Exception e) {
			LOGGER.error("Exception : "+e.getMessage());
		}
		return pmatWpList;
	}
	private Long convertStringToLong(String date) throws Exception {

		if(!StringUtils.isEmptyOrNull(date)) {
			SimpleDateFormat sdf = new SimpleDateFormat(PmatConstants.dateFormate);
			return sdf.parse(date).getTime();
		}
		return 0L;
	}

	private String checkStringIsEmpty(JSONObject field, String fieldName) {
		if(!field.isNull(fieldName)) {
			return field.getString(fieldName);
		}
		return "";
	}

	private String checkStringIsEmptyForIntData(JSONObject field, String fieldName) {
		if(!field.isNull(fieldName)) {
			return field.getString(fieldName);
		}
		return "0";
	}

	private Integer checkIntegerIsEmpty(JSONObject field, String fieldName) {
		if(!field.isNull(fieldName)) {
			return field.getInt(fieldName);
		}
		return 0;
	}

	private String getTripleValue(String serviceSubType) {
		serviceSubType = serviceSubType.replace(".", "#");
		String[] spiltSubType = serviceSubType.split("#");
		return spiltSubType[2].toString();

	}

	private String fetchUniqueId(String wpName,String safProjectId) {
		switch(wpName) {
		case "Analysis & Design":
			return safProjectId +"-"+ 1;
		case "Integration & Verification":
			return safProjectId +"-"+ 2;
		case "Project  Management":
			return safProjectId +"-"+ 3;
		case "Acceptance Testing":
			return safProjectId +"-"+ 4;
		case "Implementation":
			return safProjectId +"-"+ 5;
		case "OJT and IOS":
			return safProjectId +"-"+ 6;
		case "Adaptation":
			return safProjectId +"-"+ 7;
		case "FlowOne Deployment:FlowOne Test Automation":
			return safProjectId +"-"+ 8;
		default:
			return safProjectId +"-"+9;
		}
	}
	private List<String> getTypes(){
		List<String> typeList = new ArrayList<>();
		typeList.add(PmatConstants.PROSPECT);
		//typeList.add(PmatConstants.OPPORTUNITY);
		typeList.add(PmatConstants.PROJECT);
		return typeList;
	}

	private List<PmatWorkPackageDTO> fetchPerviousFileFromResource() {
		List<PmatWorkPackageDTO> pmatWpList = new ArrayList<PmatWorkPackageDTO>();
		File file = null;
		BufferedReader in = null;
		try {
			file = csvWriterHelper.fetchFileFromResource(SAF_DUMP_OLD_FILE_NAME);
			in = new BufferedReader(new FileReader(file));
			String strLine = "";
			String line = in.readLine();
			while( (strLine = in.readLine()) != null) {
				PmatWorkPackageDTO pmatWpObj = new PmatWorkPackageDTO();
				String[] index = strLine.split(",");
				pmatWpObj.setProspectType(index[0]);
				pmatWpObj.setSafUniqueId(index[1]);
				pmatWpObj.setPpaId(index[2]);
				pmatWpObj.setWorkpackageName(index[3]);
				pmatWpObj.setSafProjectId(index[4]);
				pmatWpObj.setServiceOffering(index[5]);
				pmatWpObj.setServiceSubType(index[6]);
				pmatWpObj.setAggregateEfforts(index[7]);
				pmatWpObj.setOpportunityNumber(index[8]);
				pmatWpObj.setGic(index[9]);
				pmatWpObj.setLastModifiedDate(index[10]);
				pmatWpObj.setWpStartDate(index[11]);
				pmatWpObj.setWpEndDate(index[12]);
				pmatWpObj.setBussinessLine(index[13]);
				pmatWpObj.setBussinessUnit(index[14]);
				pmatWpList.add(pmatWpObj);
			}

		} catch (Exception e) {
			LOGGER.error("Exception : "+e.getMessage());
		}finally {
			if(file!=null) {
				csvWriterHelper.deleteFileInResource(file);
			}try {
				in.close();
			} catch (IOException e) {
				LOGGER.info("Error while closing buffer reader");
			}
		}
		return pmatWpList;
	}

	private List<PmatWorkPackageDTO> validateCurrentAndPerviousFile(List<PmatWorkPackageDTO> pmatWpListOld, List<PmatWorkPackageDTO> pmatWpListCurrent, ProjectAndSCrmIDPojo objOpp) {
		List<PmatWorkPackageDTO> listOneList = new ArrayList<>();
		pmatWpListOld = pmatWpListOld.stream()
				.filter(i -> (i.getPpaId().equalsIgnoreCase(objOpp.getPpaId())) && (i.getSafProjectId().equalsIgnoreCase(objOpp.getAceProjectId())))
				.collect(Collectors.toList());
		if(!CollectionUtils.isEmpty(pmatWpListOld)) {
			listOneList = pmatWpListOld.stream()
					.filter(two -> pmatWpListCurrent.stream()
							.noneMatch(one -> one.getWorkpackageName().contains(two.getWorkpackageName())))
					.collect(Collectors.toList());

			if(!CollectionUtils.isEmpty(listOneList)) {
				listOneList.forEach(i -> i.setIsDeleted("true"));
			}
			pmatWpListCurrent.addAll(listOneList);
		}


		//		if(!CollectionUtils.isEmpty(pmatWpListOld)) {
		//			for(PmatWorkPackageDTO pmatObj : pmatWpListCurrent) {
		//				boolean isDeleted = pmatWpListOld.stream()
		//						.noneMatch(i -> ((i.getPpaId().equalsIgnoreCase(pmatObj.getPpaId())) 
		//								&& (i.getWorkpackageName().equalsIgnoreCase(pmatObj.getWorkpackageName()))
		//								&& (i.getSafProjectId().equalsIgnoreCase(pmatObj.getSafProjectId()))));
		//				pmatObj.setIsDeleted(Boolean.toString(isDeleted));
		//			}
		//		}
		return pmatWpListCurrent;

	}



}
