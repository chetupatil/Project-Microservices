package com.nokia.ace.connector.sonar;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.nokia.ace.connector.sonar.constants.SonarConstants;
import com.nokia.ace.connector.sonar.model.Sonar;
import com.nokia.ace.connectors.common.environmentVariables.*;
import com.nokia.ace.connectors.common.input.ConnectorInputData;
import com.nokia.ace.connectors.common.keycloak.KeycloakService;
import com.nokia.ace.connectors.common.model.toolDetails.ToolResponse;
import com.nokia.ace.connectors.common.model.transaction.FetchResponse;
import com.nokia.ace.connectors.common.model.transaction.PostRequest;
import com.nokia.ace.connectors.common.model.transaction.PostResponse;
import com.nokia.ace.connectors.common.model.transaction.TransType;
import com.nokia.ace.connectors.common.toolDetails.ToolDetails;
import com.nokia.ace.connectors.common.transaction.FetchTransaction;
import com.nokia.ace.connectors.common.transaction.PostTransaction;
import com.nokia.ace.connectors.common.util.CommonUtilities;
import com.nokia.ace.core.commons.model.BlueprintDto;
import com.nokia.ace.core.commons.model.sonar.SonarQube;

@Component
public class ACEServiceSonar implements PostTransaction<Sonar, PostResponse> {
	private Logger LOGGER = LoggerFactory.getLogger(ACEServiceSonar.class);
	JSONParser parser = new JSONParser();

	@Autowired
	@Qualifier("KeycloakService")
	KeycloakService<EnvironmentVariables> keycloakService;

	@Autowired
	ToolDetails toolDetailsImpl;
	@Autowired
	FetchTransaction fetchTransactionImpl;
	@Autowired
	CommonUtilities commonUtilities;

	/**
	 * Method to fetchTransactions from database
	 * 
	 * @param connectorInputData
	 * @param envVariables
	 * @param sonar
	 * @param sonarTool
	 * @return
	 */
	public Boolean fetchTransaction(ConnectorInputData connectorInputData, EnvironmentVariables envVariables, SonarQube sonar,
			ToolResponse sonarTool) {
		List<FetchResponse> fetchResponseList = fetchTransactionImpl.fetchTransaction(connectorInputData, envVariables,TransType.PROJECT_CREATION);
		Boolean returnStatus = false;
		Iterator i = fetchResponseList.iterator();
		if (!i.hasNext()) {
			LOGGER.error("Fetch SCM URL failed - Empty Response");
			returnStatus = false;
		}
		for (FetchResponse fetchResponse : fetchResponseList) {
		}
		return returnStatus;
	}
	public ToolResponse getToolDetails(String toolName, ConnectorInputData connectorInputData,
			EnvironmentVariables envVariables) {
		return toolDetailsImpl.getToolDetails(toolName, connectorInputData, envVariables);
	}

	/**
	 * 
	 * @param connectorInputData
	 * @param sonar
	 * @param toolResponse
	 * @param envVariables
	 * @return
	 */
	public Boolean addTransaction(ConnectorInputData connectorInputData, Sonar sonar, ToolResponse toolResponse,
			EnvironmentVariables envVariables) {
		PostRequest postRequest = createPostTransactionRequest(connectorInputData,
				connectorInputData.getProjectCdmlInput(), sonar, toolResponse);
		String token = keycloakService.getToken(envVariables, connectorInputData.getProjectName());
		return callPostTransaction(postRequest, connectorInputData, token);
	}

	/**
	 * 
	 * @param connectorInputData
	 * @param sbp
	 * @param sonar
	 * @param toolResponse
	 * @return
	 */
	@Override
	public PostRequest createPostTransactionRequest(ConnectorInputData connectorInputData, BlueprintDto sbp,
			Sonar sonar, ToolResponse toolResponse) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withLocale(Locale.getDefault()).withZone(ZoneId.systemDefault());
		String toolProjectUrl = toolResponse.getInstanceUrl() + SonarConstants.SONAR_TOOL_KEY + sonar.getProjectKey();
		List<com.nokia.ace.connectors.common.model.transaction.Defects> defectsList = new ArrayList<>();

		Integer runtimeUserId = commonUtilities.getProjectUserMappingId(connectorInputData);

		PostRequest postData = new PostRequest(SonarConstants.TOOL_NAME, connectorInputData.getProjectToolMappingId(),
				toolProjectUrl, sonar.getToolProjKey(), sonar.getToolProjKey(), defectsList, sonar.getStatus(),
				sonar.getStatusList(), "", LocalDateTime.now().format(formatter), connectorInputData.isRuntime(),
				runtimeUserId, connectorInputData.getTransType());
		return postData;
	}

}
