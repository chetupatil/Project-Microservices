/**
 * 
 */
package com.nokia.ace.utils.util;

import java.net.HttpURLConnection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nokia.ace.connectors.common.constants.ConnectorConstants;
import com.nokia.ace.connectors.common.environmentVariables.EnvironmentVariables;
import com.nokia.ace.connectors.common.httpClient.AceConnectorHttpClient;
import com.nokia.ace.connectors.common.input.ConnectorInputData;
import com.nokia.ace.connectors.common.keycloak.KeycloakServiceImpl;
import com.nokia.ace.connectors.common.model.httpClient.ConnectionRequest;
import com.nokia.ace.connectors.common.model.toolDetails.ToolRequest;
import com.nokia.ace.connectors.common.model.toolDetails.ToolResponse;
import com.nokia.ace.connectors.common.util.CommonUtilities;
import com.nokia.ace.core.commons.data.jsonPojo.ProjectLevelTools;
import com.nokia.ace.utils.constants.PmatConstants;

/**
 * @author chetana
 *
 */
@Service
public class ToolDetailImpl {

	@Autowired
	AceConnectorHttpClient aceConnectorHttpClient;
	@Autowired
	CommonUtilities commonUtilities;
	@Autowired
	KeycloakServiceImpl keycloakServiceImpl;
	
	public static final Logger LOGGER = LoggerFactory.getLogger(ToolDetailImpl.class);

	public String getApi(ConnectorInputData connectorInputData) {
		StringBuilder apiStr = new StringBuilder();
		apiStr.append(connectorInputData.getServerIp());
		apiStr.append(PmatConstants.FETCH_TOOL_DETAILS_URL);
		return apiStr.toString();
	}

	private ToolRequest getToolDetailsRequest(String toolName, ConnectorInputData connectorInputData) {
		ToolRequest postData = new ToolRequest();
		postData.setProjectName(connectorInputData.getProjectName());
		setToolName(connectorInputData,toolName,postData);
		return postData;
	}
	
	private ToolRequest setToolName(ConnectorInputData connectorInputData,String toolName,ToolRequest postData) {
		
		if(connectorInputData.getProjectCdmlInput() == null || connectorInputData.getProjectCdmlInput().getProjectLevelTools() == null) {
			postData.setToolName(toolName);
			return postData;
		}
		ProjectLevelTools projectLevelTool = connectorInputData.getProjectCdmlInput().getProjectLevelTools()
				.stream().filter(pt-> pt.getToolType().toLowerCase().contains(toolName.toLowerCase())).findAny().orElse(null);
		if(projectLevelTool != null) {
			postData.setToolName(projectLevelTool.getToolType());
			postData.setIsProjectTool(true);
		}else {
			postData.setToolName(toolName);
		}
		return postData;
	}


	public ToolResponse getToolDetails(String toolName, ConnectorInputData connectorInputData,
			EnvironmentVariables envVariables) {
		LOGGER.info("Getting Tool Details :");
		String[] logMsgParams = { connectorInputData.getProjectName(), "Get Tool details" };
		ToolResponse toolResponse = new ToolResponse();
		
		LOGGER.info("EnvironmentVariables:getAceUsername" + envVariables.getAceUsername());
		LOGGER.info("EnvironmentVariables:getAcePassword" + envVariables.getAcePassword());
		LOGGER.info("EnvironmentVariables:getKeycloakUrl" + envVariables.getKeycloakUrl());
		LOGGER.info("EnvironmentVariables:getClientId" + envVariables.getClientId());
		LOGGER.info("EnvironmentVariables:getAppEnv" + envVariables.getAppEnv());

		try {
			ObjectMapper mapper = new ObjectMapper();
			String postString = mapper.writerWithDefaultPrettyPrinter()
					.writeValueAsString(getToolDetailsRequest(toolName, connectorInputData));
			ConnectionRequest request = new ConnectionRequest();
			
			String token = keycloakServiceImpl.getToken(envVariables, connectorInputData.getProjectName());
			LOGGER.info("Tool Details Token 1:" + token);
			if (commonUtilities.validateResponse(token)) {
				request = request.getConnection(getApi(connectorInputData), null, null, postString,
						ConnectorConstants.APPLICATION_JSON, token);

				Map<String, Object> result = aceConnectorHttpClient.post(request, logMsgParams, null);
				toolResponse = readToolResponse(result);
			}else{
				LOGGER.info("Tool Details Token 2:" + token);
			}
		} catch (Exception e) {
			LOGGER.error("Error while fetching Tool Details " + e);

		}
		return toolResponse;
	}

	private ToolResponse readToolResponse(Map<String, Object> result) {
		ToolResponse toolResponse = new ToolResponse();
		ObjectMapper mapper = new ObjectMapper();
		if (result.containsValue(200) || result.containsValue(HttpURLConnection.HTTP_CREATED)) {
			String keyValue = commonUtilities.getKey(result, 200);
			LOGGER.info("readToolResponse 1:" + keyValue);
			try {
				toolResponse = mapper.readValue(keyValue, ToolResponse.class);
			} catch (Exception e) {
				LOGGER.error("Error while reading tool response" + e.getMessage());
			}

		}
		return toolResponse;
	}
	
	public ToolResponse insertCustomerData(String toolName, ConnectorInputData connectorInputData,
			EnvironmentVariables envVariables) {
		LOGGER.info("Getting Tool Details :");
		String[] logMsgParams = { connectorInputData.getProjectName(), "Get Tool details" };
		ToolResponse toolResponse = new ToolResponse();
		
		LOGGER.info("EnvironmentVariables:getAceUsername" + envVariables.getAceUsername());
		LOGGER.info("EnvironmentVariables:getAcePassword" + envVariables.getAcePassword());
		LOGGER.info("EnvironmentVariables:getKeycloakUrl" + envVariables.getKeycloakUrl());
		LOGGER.info("EnvironmentVariables:getClientId" + envVariables.getClientId());
		LOGGER.info("EnvironmentVariables:getAppEnv" + envVariables.getAppEnv());

		try {
			ObjectMapper mapper = new ObjectMapper();
//			String postString = mapper.writerWithDefaultPrettyPrinter()
//					.writeValueAsString(getToolDetailsRequest(toolName, connectorInputData));
			ConnectionRequest request = new ConnectionRequest();
			
			String token = keycloakServiceImpl.getToken(envVariables, connectorInputData.getProjectName());
			LOGGER.info("Tool Details Token 1:" + token);
			if (commonUtilities.validateResponse(token)) {
				request = request.getConnection(getApiurl(connectorInputData), null, null, null,
						ConnectorConstants.APPLICATION_JSON, token);

				Map<String, Integer> result = aceConnectorHttpClient.get(request, logMsgParams, null);
				toolResponse = readToolResponseforget(result);
			}else{
				LOGGER.info("Tool Details Token 2:" + token);
			}
		} catch (Exception e) {
			LOGGER.error("Error while fetching Tool Details " + e);

		}
		return toolResponse;
	}
	
	public String getApiurl(ConnectorInputData connectorInputData) {
		StringBuilder apiStr = new StringBuilder();
		apiStr.append(connectorInputData.getServerIp());
		apiStr.append(PmatConstants.FETCH_INSERT_URL);
		return apiStr.toString();
	}
	
	
	private ToolResponse readToolResponseforget(Map<String, Integer> result) {
		ToolResponse toolResponse = new ToolResponse();
		ObjectMapper mapper = new ObjectMapper();
		if (result.containsValue(200) || result.containsValue(HttpURLConnection.HTTP_CREATED)) {
			String keyValue = commonUtilities.getKey(result, 200);
			LOGGER.info("readToolResponse 1:" + keyValue);
			try {
				//toolResponse = mapper.readValue(keyValue, ToolResponse.class);
				LOGGER.info("Inserted Customer successfully");
			} catch (Exception e) {
				LOGGER.error("Error while reading tool response" + e.getMessage());
			}

		}
		return toolResponse;
	}
	
	
}
