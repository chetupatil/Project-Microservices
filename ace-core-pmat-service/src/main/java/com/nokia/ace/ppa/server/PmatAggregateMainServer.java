package com.nokia.ace.ppa.server;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.drools.core.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.nokia.ace.connectors.common.environmentVariables.EnvironmentVariables;
import com.nokia.ace.connectors.common.environmentVariables.ReadEnvironmentVariables;
import com.nokia.ace.connectors.common.input.ConnectorInputData;
import com.nokia.ace.connectors.common.model.toolDetails.ToolResponse;
import com.nokia.ace.ppa.dto.ProjectAndSCrmIDPojo;
import com.nokia.ace.ppa.jira.PmatJiraHelper;
import com.nokia.ace.ppa.jira.PmatProspectHelper;
import com.nokia.ace.ppa.sharepoint.PmatSpFileUtils;
import com.nokia.ace.utils.constants.PmatConstants;
import com.nokia.ace.utils.sharepoint.PmatSPTokenGenerator;
import com.nokia.ace.utils.util.ToolDetailImpl;

@Service
public class PmatAggregateMainServer {

	@Autowired
	private PmatSPTokenGenerator pmatSpTokenGen;

	@Autowired
	private PmatJiraHelper pmatJiraHelper;

	@Autowired
	private PmatSpFileUtils pmatSpFileUtils;

	@Autowired
	private PmatProspectHelper pmatProspectHelper;

	@Autowired
	private ToolDetailImpl toolDetailsImpl;

	@Autowired
	private ReadEnvironmentVariables readEnvironmentVariables;

	@Autowired
	private Environment env;

	private static final Logger LOGGER = LoggerFactory.getLogger(PmatAggregateMainServer.class);

	@Scheduled(cron = "${cronExpressionConv}")
	//@Scheduled(fixedRate = 50000)
	public void cronJobForProspectConversion() {
		try {
			LOGGER.info("Job scheduled for Prospect conversion:");
			String formDigestValue = "";
			ToolResponse spToolResponse = getToolResponse(PmatConstants.TOOL_SHAREPOINT);
			List<String> cookies =  pmatSpTokenGen.spTokenGen(spToolResponse);
 			if(!CollectionUtils.isEmpty(cookies)) {
				formDigestValue = pmatSpTokenGen.fetchDigestValueForSp(cookies,spToolResponse);		
			}
			if(!StringUtils.isEmpty(formDigestValue)) {
				pmatSpFileUtils.fetchPpaDumpAndDownloadFileByDefPath(cookies,formDigestValue,spToolResponse.getInstanceUrl());
			}
			if(env.getActiveProfiles()!=null && Arrays.asList(env.getActiveProfiles()).contains(PmatConstants.PROFILE_ENV)) {
				ToolResponse jiraToolResponse = getToolResponse(PmatConstants.TOOL_JIRA);
				pmatProspectHelper.convertOpportunityToProspect(jiraToolResponse);
				pmatProspectHelper.updateProjectDate(jiraToolResponse);
			}
		}catch(Exception e) {
			LOGGER.error("Exception occure while processing the cron job: "+e.getMessage());
		}
	}

	@Scheduled(cron = "${cronExpressionDump}")
	//@Scheduled(fixedRate = 50000)
	public void cronJobForSafDump() {
		try {
			LOGGER.info("Job scheduled for SAF Dump:");
			String formDigestValue = "";
			if(env.getActiveProfiles()!=null && Arrays.asList(env.getActiveProfiles()).contains(PmatConstants.PROFILE_ENV)) {
				ToolResponse spToolResponse = getToolResponse(PmatConstants.TOOL_SHAREPOINT);
				List<String> cookies =  pmatSpTokenGen.spTokenGen(spToolResponse);
				if(!CollectionUtils.isEmpty(cookies)) {
					formDigestValue = pmatSpTokenGen.fetchDigestValueForSp(cookies,spToolResponse);		
				}
				ToolResponse jiraToolResponse = getToolResponse(PmatConstants.TOOL_JIRA);
				List<ProjectAndSCrmIDPojo> projAndsCRMIds = pmatJiraHelper.fetchsCrmId();
				String file = pmatJiraHelper.retrieveOpportunitiesAndSaveIntoFile(projAndsCRMIds,jiraToolResponse);
				if(!StringUtils.isEmpty(formDigestValue)) {
					pmatSpFileUtils.dumpFileIntoSpCSV(cookies, formDigestValue,new File(file),spToolResponse.getInstanceUrl());
				}
			}
		}catch(Exception e) {
			LOGGER.error("Exception occure while processing the cron job: "+e.getMessage());
		}
	}

	private ToolResponse getToolResponse(String toolName) {
		ConnectorInputData jiraInput = new ConnectorInputData();
		jiraInput.setProjectName(PmatConstants.PROJECT_NAME);
		jiraInput.setServerIp(PmatConstants.SERVER_IP);
		EnvironmentVariables envVariables = readEnvVariables();
		ToolResponse toolResponse = toolDetailsImpl.getToolDetails(toolName, jiraInput,envVariables);
		return toolResponse;
	}

	public EnvironmentVariables readEnvVariables() {
		EnvironmentVariables envVariables = new EnvironmentVariables();
		envVariables = readEnvironmentVariables.readEnvVariables(envVariables);
		return envVariables;
	}

}
