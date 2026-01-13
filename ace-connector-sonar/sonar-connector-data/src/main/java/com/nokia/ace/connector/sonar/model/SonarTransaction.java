package com.nokia.ace.connector.sonar.model;

import java.util.List;

import com.nokia.ace.connectors.common.model.transaction.ToolFunction;

public class SonarTransaction {
	private Boolean reusable;
	private String aceServerIp;
	private String aceProject;
	private String projectKey;
	private Integer projectId;
	private Integer projectToolMappingId;
	private List<ToolFunction> statusList;
	private String toolProjKey;
	private String status;
	private Boolean createProject;
	private Boolean createProfile;
	private Boolean addProjectsInPortfolio;
	public Boolean getAddProjectsInPortfolio() {
		return addProjectsInPortfolio;
	}

	public void setAddProjectsInPortfolio(Boolean addProjectsInPortfolio) {
		this.addProjectsInPortfolio = addProjectsInPortfolio;
	}

	public Boolean getCreateProject() {
		return createProject;
	}

	public void setCreateProject(Boolean createProject) {
		this.createProject = createProject;
	}

	public Boolean getCreateProfile() {
		return createProfile;
	}

	public void setCreateProfile(Boolean createProfile) {
		this.createProfile = createProfile;
	}

	private Boolean assignRoleStatus;
	private String aceProjectId;

	public SonarTransaction() {
		this.assignRoleStatus = false;
	}

	public Boolean getAssignRoleStatus() {
		return assignRoleStatus;
	}

	public void setAssignRoleStatus(Boolean assignRoleStatus) {
		this.assignRoleStatus = assignRoleStatus;
	}

	public String getAceProjectId() {
		return aceProjectId;
	}

	public void setAceProjectId(String aceProjectId) {
		this.aceProjectId = aceProjectId;
	}

	public String getAceServerIp() {
		return aceServerIp;
	}

	public void setAceServerIp(String aceServerIp) {
		this.aceServerIp = aceServerIp;
	}

	public String getAceProject() {
		return aceProject;
	}

	public void setAceProject(String aceProject) {
		this.aceProject = aceProject;
	}

	/**
	 * @return the projectKey
	 */
	public String getProjectKey() {
		return projectKey;
	}

	/**
	 * @param projectKey the projectKey to set
	 */
	public void setProjectKey(String projectKey) {
		this.projectKey = projectKey;
	}

	public Integer getProjectToolMappingId() {
		return projectToolMappingId;
	}

	public void setProjectToolMappingId(Integer projectToolMappingId) {
		this.projectToolMappingId = projectToolMappingId;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	/**
	 * @return the statusList
	 */
	public List<ToolFunction> getStatusList() {
		return statusList;
	}

	/**
	 * @param statusList the statusList to set
	 */
	public void setStatusList(List<ToolFunction> statusList) {
		this.statusList = statusList;
	}

	/**
	 * @return the toolProjKey
	 */
	public String getToolProjKey() {
		return toolProjKey;
	}

	/**
	 * @param toolProjKey the toolProjKey to set
	 */
	public void setToolProjKey(String toolProjKey) {
		this.toolProjKey = toolProjKey;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the reusable
	 */
	public Boolean getReusable() {
		return reusable;
	}

	/**
	 * @param reusable the reusable to set
	 */
	public void setReusable(Boolean reusable) {
		this.reusable = reusable;
	}

}
