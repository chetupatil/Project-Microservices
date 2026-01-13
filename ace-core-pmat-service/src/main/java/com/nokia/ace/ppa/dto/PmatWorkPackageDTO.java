package com.nokia.ace.ppa.dto;

import java.util.List;

public class PmatWorkPackageDTO {
	
	private String prospectType;
	private String safUniqueId;
	public String getProspectType() {
		return prospectType;
	}
	public void setProspectType(String prospectType) {
		this.prospectType = prospectType;
	}
	public String getOpportunityNumber() {
		return opportunityNumber;
	}
	public void setOpportunityNumber(String opportunityNumber) {
		this.opportunityNumber = opportunityNumber;
	}
	public String getSafProjectId() {
		return safProjectId;
	}
	public void setSafProjectId(String safProjectId) {
		this.safProjectId = safProjectId;
	}
	public String getPpaId() {
		return ppaId;
	}
	public void setPpaId(String ppaId) {
		this.ppaId = ppaId;
	}
	public String getServiceOffering() {
		return serviceOffering;
	}
	public void setServiceOffering(String serviceOffering) {
		this.serviceOffering = serviceOffering;
	}
	public String getServiceSubType() {
		return serviceSubType;
	}
	public void setServiceSubType(String serviceSubType) {
		this.serviceSubType = serviceSubType;
	}
	public String getGic() {
		return gic;
	}
	public void setGic(String gic) {
		this.gic = gic;
	}
	public String getBussinessLine() {
		return bussinessLine;
	}
	public void setBussinessLine(String bussinessLine) {
		this.bussinessLine = bussinessLine;
	}
	public String getBussinessUnit() {
		return bussinessUnit;
	}
	public void setBussinessUnit(String bussinessUnit) {
		this.bussinessUnit = bussinessUnit;
	}
	public String getAggregateEfforts() {
		return aggregateEfforts;
	}
	public void setAggregateEfforts(String aggregateEfforts) {
		this.aggregateEfforts = aggregateEfforts;
	}
	public String getSafUniqueId() {
		return safUniqueId;
	}
	public void setSafUniqueId(String safUniqueId) {
		this.safUniqueId = safUniqueId;
	}
	public String getWpStartDate() {
		return wpStartDate;
	}
	public void setWpStartDate(String wpStartDate) {
		this.wpStartDate = wpStartDate;
	}
	public String getWpEndDate() {
		return wpEndDate;
	}
	public void setWpEndDate(String wpEndDate) {
		this.wpEndDate = wpEndDate;
	}
	public String getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	private String opportunityNumber;
	private String workpackageName;
	public String getWorkpackageName() {
		return workpackageName;
	}
	public void setWorkpackageName(String workpackageName) {
		this.workpackageName = workpackageName;
	}
	private String safProjectId;
	private String ppaId;
	private String serviceOffering;
	private String serviceSubType;
	private String gic;
	private String bussinessLine;
	private String bussinessUnit;
	private String aggregateEfforts;
	private String wpStartDate;
	private String wpEndDate;
	private String lastModifiedDate;
	private String isDeleted;
	public String getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

}
