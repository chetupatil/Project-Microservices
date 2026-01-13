package com.nokia.ace.connector.sonar.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.nokia.ace.connectors.common.input.ConnectorInputData;
import com.nokia.ace.core.commons.model.BlueprintDto;

@JsonInclude(Include.NON_NULL)
public class SonarConnectorInputData extends ConnectorInputData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String projectName;
	private int projectId;
	private String projectKey;
	private String jiraKey;
	private BlueprintDto projectCdmlInput;
	private String operationType;
	private String loginUser;
	public String getLoginUser() {
		return loginUser;
	}
	public void setLoginUser(String loginUser) {
		this.loginUser = loginUser;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	public String getProjectKey() {
		return projectKey;
	}
	public void setProjectKey(String projectKey) {
		this.projectKey = projectKey;
	}
	public String getJiraKey() {
		return jiraKey;
	}
	public void setJiraKey(String jiraKey) {
		this.jiraKey = jiraKey;
	}
	public BlueprintDto getProjectCdmlInput() {
		return projectCdmlInput;
	}
	public void setProjectCdmlInput(BlueprintDto projectCdmlInput) {
		this.projectCdmlInput = projectCdmlInput;
	}
	public String getOperationType() {
		return operationType;
	}
	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}
	
	

}