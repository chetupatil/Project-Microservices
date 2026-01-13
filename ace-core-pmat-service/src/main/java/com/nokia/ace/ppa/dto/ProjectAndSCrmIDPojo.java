package com.nokia.ace.ppa.dto;

import java.util.List;

public class ProjectAndSCrmIDPojo {

	private String aceProjectId;
	private String sCrmId;
	private String ppaId;
	private String bgdmName;
	private String opportunityName;
	private String cbt;
	private String ct;
	private String projectRisk;
	private String projectStatus;
	private String marketUnit;
	private String market;
	private String status;
	private String projectType;
	private String ngseetUrl;
	private String comments;
	private String plsProspectScope;
	private String enoviaProductRelease;
	private String ppaCreatedBy;
	private String ppaModifiedBy;
	private String ppaCreated;
	private String ppaModified;
	private String serviceStartDate;
	private String serviceEndDate;
	
	public String getServiceEndDate() {
		return serviceEndDate;
	}
	public void setServiceEndDate(String serviceEndDate) {
		this.serviceEndDate = serviceEndDate;
	}
	public String getServiceStartDate() {
		return serviceStartDate;
	}
	public void setServiceStartDate(String serviceStartDate) {
		this.serviceStartDate = serviceStartDate;
	}
	private List<JiraIssueTypeAndKey> jiraIssyeTypeAndKeyList;
	public List<JiraIssueTypeAndKey> getJiraIssyeTypeAndKeyList() {
		return jiraIssyeTypeAndKeyList;
	}
	public void setJiraIssyeTypeAndKeyList(List<JiraIssueTypeAndKey> jiraIssyeTypeAndKeyList) {
		this.jiraIssyeTypeAndKeyList = jiraIssyeTypeAndKeyList;
	}
	public String getPpaCreated() {
		return ppaCreated;
	}
	public void setPpaCreated(String ppaCreated) {
		this.ppaCreated = ppaCreated;
	}
	public String getPpaModified() {
		return ppaModified;
	}
	public void setPpaModified(String ppaModified) {
		this.ppaModified = ppaModified;
	}
	public String getPpaModifiedBy() {
		return ppaModifiedBy;
	}
	public void setPpaModifiedBy(String ppaModifiedBy) {
		this.ppaModifiedBy = ppaModifiedBy;
	}
	public String getPpaCreatedBy() {
		return ppaCreatedBy;
	}
	public void setPpaCreatedBy(String ppaCreatedBy) {
		this.ppaCreatedBy = ppaCreatedBy;
	}
	public String getEnoviaProductRelease() {
		return enoviaProductRelease;
	}
	public void setEnoviaProductRelease(String enoviaProductRelease) {
		this.enoviaProductRelease = enoviaProductRelease;
	}
	public String getPlsProspectScope() {
		return plsProspectScope;
	}
	public void setPlsProspectScope(String plsProspectScope) {
		this.plsProspectScope = plsProspectScope;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getNgseetUrl() {
		return ngseetUrl;
	}
	public void setNgseetUrl(String ngseetUrl) {
		this.ngseetUrl = ngseetUrl;
	}
	public String getProjectType() {
		return projectType;
	}
	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
//	private List<String> keys;
//
//
//
//	public List<String> getKeys() {
//		return keys;
//	}
//	public void setKeys(List<String> keys) {
//		this.keys = keys;
//	}
	public String getMarketUnit() {
		return marketUnit;
	}
	public void setMarketUnit(String marketUnit) {
		this.marketUnit = marketUnit;
	}
	public String getCbt() {
		return cbt;
	}
	public void setCbt(String cbt) {
		this.cbt = cbt;
	}
	public String getCt() {
		return ct;
	}
	public void setCt(String ct) {
		this.ct = ct;
	}
	public String getProjectRisk() {
		return projectRisk;
	}
	public void setProjectRisk(String projectRisk) {
		this.projectRisk = projectRisk;
	}
	public String getProjectStatus() {
		return projectStatus;
	}
	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}
	public String getBgdmName() {
		return bgdmName;
	}
	public void setBgdmName(String bgdmName) {
		this.bgdmName = bgdmName;
	}

	public String getOpportunityName() {
		return opportunityName;
	}
	public void setOpportunityName(String opportunityName) {
		this.opportunityName = opportunityName;
	}
	public String getAceProjectId() {
		return aceProjectId;
	}
	public void setAceProjectId(String aceProjectId) {
		this.aceProjectId = aceProjectId;
	}
	public String getsCrmId() {
		return sCrmId;
	}
	public void setsCrmId(String sCrmId) {
		this.sCrmId = sCrmId;
	}
	public String getPpaId() {
		return ppaId;
	}
	public void setPpaId(String ppaId) {
		this.ppaId = ppaId;
	}
	public String getMarket() {
		return market;
	}
	public void setMarket(String market) {
		this.market = market;
	}

}
