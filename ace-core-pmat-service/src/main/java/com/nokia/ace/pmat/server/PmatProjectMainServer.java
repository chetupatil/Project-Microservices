package com.nokia.ace.pmat.server;

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
import com.nokia.ace.pmat.dao.PmatProjectMasterDao;
import com.nokia.ace.ppa.jira.PmatProspectHelper;
import com.nokia.ace.utils.constants.PmatConstants;
import com.nokia.ace.utils.sharepoint.PmatSPTokenGenerator;
import com.nokia.ace.utils.util.ToolDetailImpl;
import com.nokia.ace.pmat.sharepoint.pmatSharepointUtils;

@Service
public class PmatProjectMainServer {
	
	@Autowired
	private ToolDetailImpl toolDetailsImpl;
	
	@Autowired
	private ReadEnvironmentVariables readEnvironmentVariables;
	
	@Autowired
	private PmatSPTokenGenerator pmatSpTokenGen;

	@Autowired
	private PmatProspectHelper pmatProspectHelper;
	
	@Autowired
	private Environment env;
	
	@Autowired
	private pmatSharepointUtils pmatSharepointUtils;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PmatProjectMainServer.class);
	
	@Scheduled(cron = "${pmatCronExpression}")
	//@Scheduled(fixedRate = 50000)
	public void cronJobForProspectConversion() {
		try {
			LOGGER.info("Job scheduled for getting pmat dump:");
			String formDigestValue = "";
			ToolResponse spToolResponse = getToolResponse(PmatConstants.TOOL_SHAREPOINT);
					//new ToolResponse();
			//spToolResponse.setInstanceUrl("https://nokia.sharepoint.com");
			//spToolResponse.setPassword("zzzlstqpyrdkmbgz");
			//spToolResponse.setUserName("nswps.ca_nswps_ace@nokia.com");
			//spToolResponse.setUseremail("nswps.ca_nswps_ace@nokia.com");
			List<String> cookies =  pmatSpTokenGen.spTokenGen(spToolResponse);
 			if(!CollectionUtils.isEmpty(cookies)) {
				formDigestValue = pmatSpTokenGen.fetchDigestValueForSp(cookies,spToolResponse);		
			}
			if(!StringUtils.isEmpty(formDigestValue)) {
				pmatSharepointUtils.fetchPmatDumpAndDownloadFileByDefPath(cookies,formDigestValue,spToolResponse.getInstanceUrl());
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
