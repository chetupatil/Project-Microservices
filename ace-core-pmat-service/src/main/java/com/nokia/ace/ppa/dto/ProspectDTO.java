package com.nokia.ace.ppa.dto;

public class ProspectDTO {
	private String backLogTypeId;
	private String ppaId;
	private String opportunityName;
	private String bgdmName;
	private boolean isDeleted;
	
	
	public String getBgdmName() {
		return bgdmName;
	}
	public void setBgdmName(String bgdmName) {
		this.bgdmName = bgdmName;
	}
	public boolean isDeleted() {
		return isDeleted;
	}
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	public String getBackLogTypeId() {
		return backLogTypeId;
	}
	public void setBackLogTypeId(String backLogTypeId) {
		this.backLogTypeId = backLogTypeId;
	}
	public String getPpaId() {
		return ppaId;
	}
	public void setPpaId(String ppaId) {
		this.ppaId = ppaId;
	}
	public String getOpportunityName() {
		return opportunityName;
	}
	public void setOpportunityName(String opportunityName) {
		this.opportunityName = opportunityName;
	}

}
