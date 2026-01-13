package com.nokia.ace.connector.sonar.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class UserRoleMapping implements Serializable {

	private static final long serialVersionUID = 3337097358809885899L;

	private String applicationName;
	private String operationType;
	private List<SonarUser> users;
	private String workflowToolName;
	private String jiraKey;

	public String getApplicationName() {
		return applicationName;
	}

	public UserRoleMapping setApplicationName(String applicationName) {
		this.applicationName = applicationName;
		return this;
	}

	public String getOperationType() {
		return operationType;
	}

	public UserRoleMapping setOperationType(String operationType) {
		this.operationType = operationType;
		return this;
	}

	public List<SonarUser> getUsers() {
		return users;
	}

	public UserRoleMapping setUsers(List<SonarUser> users) {
		this.users = users;
		return this;
	}

	public String getWorkflowToolName() {
		return workflowToolName;
	}

	public UserRoleMapping setWorkflowToolName(String workflowToolName) {
		this.workflowToolName = workflowToolName;
		return this;
	}

	public String getJiraKey() {
		return jiraKey;
	}

	public UserRoleMapping setJiraKey(String jiraKey) {
		this.jiraKey = jiraKey;
		return this;
	}

}
