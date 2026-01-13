package com.nokia.ace.connector.sonar;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import com.nokia.ace.connector.sonar.constants.SonarConstants;
import com.nokia.ace.connector.sonar.error.SonarExecutionException;
import com.nokia.ace.connector.sonar.model.Sonar;
import com.nokia.ace.connector.sonar.model.SonarConnectorInputData;
import com.nokia.ace.connectors.common.AceConnector;
import com.nokia.ace.connectors.common.environmentVariables.EnvironmentVariables;
import com.nokia.ace.connectors.common.environmentVariables.ReadEnvironmentVariables;
import com.nokia.ace.connectors.common.httpClient.AceConnectorHttpClient;
import com.nokia.ace.connectors.common.input.ConnectorInputData;
import com.nokia.ace.connectors.common.model.httpClient.ConnectionRequest;
import com.nokia.ace.connectors.common.model.toolDetails.ToolResponse;
import com.nokia.ace.connectors.common.model.transaction.ToolFunction;
import com.nokia.ace.connectors.common.output.ConnectorOutputData;
import com.nokia.ace.connectors.common.util.CommonUtilities;
import com.nokia.ace.core.commons.api.response.AceResponse;
import com.nokia.ace.core.commons.exception.ACEBaseException;
import com.nokia.ace.core.commons.exception.AceErrorRepository;
import com.nokia.ace.core.commons.model.BlueprintDto;

/**
 * Sonar connector layer which contains business logic of all existing
 * functionalities.
 *
 * @author Chetana P
 */
@Component
@Qualifier("SonarConnector")
public class SonarConnector implements AceConnector<ConnectorInputData, ConnectorOutputData> {

	@Autowired
	AceErrorRepository aceErrorRepository;

	@Autowired
	AceConnectorHttpClient aceConnectorHttpClient;

	@Autowired
	private ReadEnvironmentVariables readEnvironmentVariables;
	@Autowired
	CommonUtilities commonUtilities;

	@Autowired
	private ACEServiceSonar aceService;


	public static final Logger LOGGER = LoggerFactory.getLogger(SonarConnector.class);

	/**
	 * Connect method is center point for the sonar connectivity.
	 * 
	 * @param: connectorInput
	 * 
	 * @author chetana
	 */
	@Override
	public ConnectorOutputData connect(ConnectorInputData connectorInput) throws ACEBaseException {
		AceResponse aceResponse = new AceResponse();
		HashMap<String, String> addResponse = new HashMap<>();
		Map<String, Object> responseMap = null;
		EnvironmentVariables envVariables = readEnvironmentVariables();
		ToolResponse toolResponse = new ToolResponse();
		ConnectorOutputData connectorOutputData = new ConnectorOutputData();
		Sonar sonar = new Sonar();
		String sonarName = StringUtils.EMPTY;
		String sonarKey = StringUtils.EMPTY;
		try {
			if(connectorInput.getTransType().name().equalsIgnoreCase(SonarConstants.CREATION)) {
				toolResponse = aceService.getToolDetails(SonarConstants.TOOL_NAME, connectorInput, envVariables);
				responseMap = createSonarProject(connectorInput,envVariables,toolResponse);
				aceResponse = prepareSonarResponse(responseMap);
				addResponse.put("status", aceResponse.getStatus());
				
				if (responseMap!= null && aceResponse.getStatus().equalsIgnoreCase(SonarConstants.SUCCESS)) {
					String keyValue = commonUtilities.getKey(responseMap, 200);
					addResponse.put("data", keyValue);
					org.json.simple.JSONObject sonarProjectObj = jsonConversion(keyValue);
					sonarKey = (String) sonarProjectObj.get("key");
					sonarName = (String) sonarProjectObj.get("name");
					sonar.setCreateProject(true);
				}else {
					sonar.setCreateProject(false);
				}
				
				if(sonar.getCreateProject()!=null && sonar.getCreateProject()) {
					// add quality profile
					Boolean isProfileCreated = addQualityProfileInProject(envVariables,sonarKey,toolResponse);
					// user user Mapping
					Boolean isSonarUserValid = sonarUserMapping(connectorInput,envVariables,sonarKey,toolResponse);

					Boolean isSonarPortfolioValid = addProjectIntoPortfolios(connectorInput,envVariables,sonarKey,toolResponse); 

					sonar.setToolProjKey(sonarName);
					sonar.setProjectKey(sonarKey);
					sonar.setCreateProfile(isProfileCreated);
					sonar.setAssignRoleStatus(isSonarUserValid);
					sonar.setAddProjectInPortfolio(isSonarPortfolioValid);
				}else {
					sonar.setToolProjKey(sonarName);
					sonar.setProjectKey(sonarKey);
					sonar.setCreateProfile(false);
					sonar.setAssignRoleStatus(false);
					sonar.setAddProjectInPortfolio(false);
				}
				connectorOutputData.setSonarResponse(addResponse);
				callPostTransaction(sonar, connectorInput.getProjectCdmlInput(), toolResponse,envVariables, connectorInput,connectorOutputData);
			}
		} catch (Exception e) {
			LOGGER.error("Exception Occured during sonar user connection{}", e);
		}
		return connectorOutputData;
	}



	/**
	 * Method: Add user permission to sonar project .
	 * 
	 * @param: connectorInput
	 * @param: envVariables
	 * @param: projectKey
	 * @param: toolResponse
	 * 
	 * @author chetana
	 */
	public Boolean sonarUserMapping(ConnectorInputData connectorInput,EnvironmentVariables envVariables, String projectKey, ToolResponse toolResponse) {
		Boolean isSonarUserValid = false;
		try {
			String[] logMsgParams = { connectorInput.getProjectName(), "Sonarqube User Mapping." };

			Map<String, Map<String, String>> sonarUserPermissionMap =  connectorInput.getSonarUserPermissionList();
			for(Entry<String, Map<String, String>> sonarUserEntry : sonarUserPermissionMap.entrySet()) {   	 
				for(Entry<String, String> sonarIter :  sonarUserEntry.getValue().entrySet()) {

					LOGGER.info("Sonar User Permission Addition with "+sonarUserEntry+" : "+sonarIter.getKey());
					StringBuilder apiStr = new StringBuilder();
					String url = toolResponse.getInstanceUrl() + SonarConstants.SONAR_QUBE_ADD_USER_API;
					apiStr.append(url);
					apiStr.append("permission=").append(sonarIter.getValue());
					apiStr.append("&projectKey=").append(projectKey);
					apiStr.append("&login=").append(sonarUserEntry.getKey());
					Map<String, String> credentials = new HashMap();
					credentials.put("userName", toolResponse.getUserName());
					credentials.put("password", toolResponse.getPassword());
					String postString = "{}";
					ConnectionRequest request = new ConnectionRequest();
					request = request.getConnection(apiStr.toString(), credentials, null, postString, SonarConstants.APPLICATION_JSON, null);
					isSonarUserValid = aceConnectorHttpClient.post(request, logMsgParams, null).containsValue(204);
					LOGGER.info("isSonarUserValid "+isSonarUserValid);	
				}
			}

		}catch(Exception ex) {
			LOGGER.error("unable to sonarqube user mapping. error logs: " +ex.getMessage());
			return isSonarUserValid;
		}
		return isSonarUserValid;

	}


	/**
	 * Method: Add projects into Portfolios.
	 * 
	 * @param: connectorInput
	 * @param: envVariables
	 * @param: projectKey
	 * @param: toolResponse
	 * 
	 * @author chetana
	 */
	public Boolean addProjectIntoPortfolios(ConnectorInputData connectorInput,EnvironmentVariables envVariables, String projectKey, ToolResponse toolResponse) {
		Map<String, Object> mapAddProjInPortfolios = new HashMap<>();
		try {
			String[] logMsgParams = { connectorInput.getProjectName(), "Sonarqube Add Portfolios." };
			StringBuilder apiStr = new StringBuilder();
			String url = toolResponse.getInstanceUrl() + SonarConstants.SONAR_QUBE_ADD_PORTFOLIOS;

			String key = connectorInput.getProjectCdmlInput().getConfig().getDomainID();

			apiStr.append(url);
			apiStr.append("key=").append(key);
			apiStr.append("&project=").append(projectKey);

			Map<String, String> credentials = new HashMap();
			credentials.put("userName", toolResponse.getUserName());
			credentials.put("password", toolResponse.getPassword());
			String postString = "{}";
			ConnectionRequest request = new ConnectionRequest();
			request = request.getConnection(apiStr.toString(), credentials, null, postString, SonarConstants.APPLICATION_JSON, null);
			mapAddProjInPortfolios = aceConnectorHttpClient.post(request, logMsgParams, null);
			LOGGER.info("mapAddProjInPortfolios   "+mapAddProjInPortfolios);
			if(mapAddProjInPortfolios.containsValue(204)) {
				return true;
			}
		}catch(Exception ex) {
			LOGGER.error("unable to sonarqube add Portfolios. error logs: " +ex.getMessage());
		}
		return false;

	}



	/**
	 *  Create sonar project with connection with sonar platform.
	 *
	 * @param: inputData
	 * @param: envVariables
	 * @param  toolResponse 
	 * 
	 * @author chetana
	 */
	public Map<String, Object> createSonarProject(ConnectorInputData inputData,
			EnvironmentVariables envVariables, ToolResponse toolResponse) {
		Map<String, Object> result = null;
		try {
			String[] logMsgParams = { inputData.getProjectName(), "Create sonarqube projects." };

			String url = toolResponse.getInstanceUrl() + SonarConstants.CREATE_SONAR_QUBE_PROJECT_API;
			String projectKey = envVariables.getSonarQubeProjectPattern() + StringUtils.EMPTY + ((SonarConnectorInputData) inputData).getProjectName();

			StringBuilder apiStr = new StringBuilder();
			apiStr.append(url);
			apiStr.append("name=").append(((SonarConnectorInputData) inputData).getProjectName());
			apiStr.append("&project=").append(projectKey);

			Map<String, String> credentials = new HashMap();
			credentials.put("userName", toolResponse.getUserName());
			credentials.put("password", toolResponse.getPassword());
			String postString = "{}";
			ConnectionRequest request = new ConnectionRequest();
			request = request.getConnection(apiStr.toString(), credentials, null, postString, SonarConstants.APPLICATION_JSON, null);
			result = aceConnectorHttpClient.post(request, logMsgParams, null);
			LOGGER.info("result "+result);
			if (result.containsValue(200) || result.containsValue(201) || result.containsValue(204) || result.containsValue(HttpURLConnection.HTTP_CREATED)) {
				LOGGER.info("Sonar project created successfully");
				return result;
			}
		} catch (Exception e) {
			LOGGER.error("unable to create sonarqube project. error logs: " +e.getMessage());
		}
		return result;

	}
	/**
	 *  Add quality profile in sonar project
	 *
	 * @param: projectKey
	 * @param: envVariables
	 * @param: toolResponse 
	 * 
	 * @author chetana
	 */
	private Boolean addQualityProfileInProject(EnvironmentVariables envVariables, String projectKey, ToolResponse toolResponse) {
		Map<String, Object> result = null;
		Boolean isProfileCreated = false;
		try {
			String[] logMsgParams = { projectKey, "Add quality profile in project." };

			String url =toolResponse.getInstanceUrl() + SonarConstants.SONAR_QUALITY_PROFILE_ADD_PROJECT_URL;
			Map<String,String> languageWithProfile = new HashMap<String,String>();

			languageWithProfile.put(SonarConstants.JAVA_LANGUAGE, SonarConstants.JAVA_QUALITY_PROFILE);
			languageWithProfile.put(SonarConstants.PYTHON_LANGUAGE, SonarConstants.PYTHON_QUALITY_PROFILE);

			for(Entry<String, String> lwp : languageWithProfile.entrySet()) {
				
				LOGGER.info("Sonar Quality profile : "+lwp.getKey());
				StringBuilder apiStr = new StringBuilder();
				apiStr.append(url);
				apiStr.append("language=").append(lwp.getKey());
				apiStr.append("&qualityProfile=").append(lwp.getValue());
				apiStr.append("&project=").append(projectKey);

				LOGGER.info("apiStr "+apiStr);

				Map<String, String> credentials = new HashMap();
				credentials.put("userName", toolResponse.getUserName());
				credentials.put("password", toolResponse.getPassword());
				String postString = "{}";

				ConnectionRequest request = new ConnectionRequest();
				request = request.getConnection(apiStr.toString(), credentials, null, postString, SonarConstants.APPLICATION_JSON, null);
				result = aceConnectorHttpClient.post(request, logMsgParams, null);
				LOGGER.info("result "+result);
				
				if (result!=null && result.containsValue(204)) {
					isProfileCreated = true;
					LOGGER.info("added quality profile in project successfully. ");
				}
			}
		} catch (Exception e) {
			LOGGER.error("unable to add quality profile in project. error logs: " +e.getMessage());
			return isProfileCreated;
		}
		return isProfileCreated;
	}
	public AceResponse<String> prepareSonarResponse(Map<String, Object> responseMap)
			throws SonarExecutionException {
		AceResponse aceResponse = new AceResponse();
		if (responseMap == null) {
			aceResponse.setStatus(SonarConstants.FAILURE);
		}else {
			if (responseMap.containsValue(200) || responseMap.containsValue(201) || responseMap.containsValue(204) || responseMap.containsValue(HttpURLConnection.HTTP_CREATED)) {
				aceResponse.setStatus(SonarConstants.SUCCESS);
			} else {
				aceResponse.setStatus(SonarConstants.FAILURE);
			}
		}
		return aceResponse;
	}

	private EnvironmentVariables readEnvironmentVariables() {
		EnvironmentVariables envVariables = new EnvironmentVariables();
		envVariables = readEnvironmentVariables.readEnvVariables(envVariables);
		return envVariables;
	}
	/**
	 *  Add quality profile in sonar project
	 *
	 * @param: sonar
	 * @param: sbp
	 * @param: connectorInput
	 * @param: connectorOutputData
	 * @param: envVariables
	 * @param: toolResponse 
	 * 
	 * @author chetana
	 */
	private ConnectorOutputData callPostTransaction(Sonar sonar, BlueprintDto sbp, ToolResponse toolResponse,
			EnvironmentVariables envVariables, ConnectorInputData connectorInput, ConnectorOutputData connectorOutputData) {
		try {
			if (sonar.getCreateProfile() && sonar.getCreateProject() && sonar.getAssignRoleStatus() && sonar.getAddProjectInPortfolio()) {
				sonar.setStatus(SonarConstants.SUCCESS);
			} else {
				sonar.setStatus(SonarConstants.FAILURE);
			}
			List<ToolFunction> statusList = new ArrayList<>();
			statusList.add(commonUtilities.populateStatusList(SonarConstants.CREATE_PROJECT, sonar.getCreateProject(),null));
			statusList.add(commonUtilities.populateStatusList(SonarConstants.CREATE_PROFILE, sonar.getCreateProfile(),null));
			statusList.add(commonUtilities.populateStatusList(SonarConstants.ASSIGN_ROLES, sonar.getAssignRoleStatus(),null));
			statusList.add(commonUtilities.populateStatusList(SonarConstants.ADD_PROJECT_IN_PORTFOLIO, sonar.getAddProjectInPortfolio(),null));
			sonar.setStatusList(statusList);
			aceService.addTransaction(connectorInput, sonar, toolResponse,envVariables);
			connectorOutputData.setTriggerConnectorStatus(sonar.getStatus());
			connectorOutputData.setToolFunctionList(statusList);
		} catch (Exception e) {
			LOGGER.error("Post transaction failed - " + e.toString());
		}
		return connectorOutputData;
	}


	@Override
	public String name() {
		return "SonarConnector";
	}

	@Override
	public ConnectorOutputData test(ConnectorInputData connectorInput) {
		// TODO Auto-generated method stub
		return null;
	}

	private org.json.simple.JSONObject jsonConversion(String keyValue) throws Exception {
		JSONParser jp = new JSONParser();
		org.json.simple.JSONObject sonarDataObj = (org.json.simple.JSONObject) jp.parse(keyValue);
		org.json.simple.JSONObject sonarProjectObj = (org.json.simple.JSONObject) sonarDataObj.get("project");
		return sonarProjectObj;

	}
}