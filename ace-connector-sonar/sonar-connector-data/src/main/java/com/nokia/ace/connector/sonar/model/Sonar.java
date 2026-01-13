package com.nokia.ace.connector.sonar.model;

import java.util.List;

import com.nokia.ace.connectors.common.model.transaction.ToolFunction;

public class Sonar {

	private static final long serialVersionUID = 6716889912970733784L;
	/**
	 * module Contains details required for Sonar tool
	 */

	private String orgId;
	private String domainId;
	private String aceServerIp;
	private String aceProject;
	private String projectKey;
	private String baseRepo;
	private String credentialId;
	private Integer projectId;
	private Integer projectToolMappingId;
	private List<ToolFunction> statusList;
	private String toolProjKey;
	private String status;
	private Boolean createProject;
	private Boolean createProfile;
	private Boolean assignRoleStatus;
	private Boolean addProjectInPortfolio;
	
	public Boolean getAddProjectInPortfolio() {
		return addProjectInPortfolio;
	}

	public void setAddProjectInPortfolio(Boolean addProjectInPortfolio) {
		this.addProjectInPortfolio = addProjectInPortfolio;
	}

	private String branch;
	private String projectRepository;
	private String existingRepository;

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getDomainId() {
		return domainId;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
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

	public String getProjectKey() {
		return projectKey;
	}

	public void setProjectKey(String projectKey) {
		this.projectKey = projectKey;
	}

	public String getBaseRepo() {
		return baseRepo;
	}

	public void setBaseRepo(String baseRepo) {
		this.baseRepo = baseRepo;
	}

	public String getCredentialId() {
		return credentialId;
	}

	public void setCredentialId(String credentialId) {
		this.credentialId = credentialId;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getProjectToolMappingId() {
		return projectToolMappingId;
	}

	public void setProjectToolMappingId(Integer projectToolMappingId) {
		this.projectToolMappingId = projectToolMappingId;
	}

	public List<ToolFunction> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<ToolFunction> statusList) {
		this.statusList = statusList;
	}

	public String getToolProjKey() {
		return toolProjKey;
	}

	public void setToolProjKey(String toolProjKey) {
		this.toolProjKey = toolProjKey;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public Boolean getAssignRoleStatus() {
		return assignRoleStatus;
	}

	public void setAssignRoleStatus(Boolean assignRoleStatus) {
		this.assignRoleStatus = assignRoleStatus;
	}
	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getProjectRepository() {
		return projectRepository;
	}

	public void setProjectRepository(String projectRepository) {
		this.projectRepository = projectRepository;
	}

	public String getExistingRepository() {
		return existingRepository;
	}

	public void setExistingRepository(String existingRepository) {
		this.existingRepository = existingRepository;
	}

}
