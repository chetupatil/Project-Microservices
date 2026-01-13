package com.nokia.ace.ppa.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JiraIssueFields {

	@JsonProperty("customfield_38139")
	private TypeValue projectType;
	
	@JsonProperty("customfield_29590")
	private TypeValue market;
	
	@JsonProperty("customfield_40510")
	private List<String> bgdmName;
	
	@JsonProperty("customfield_38075")
	private String marketUnit;
	
	@JsonProperty("customfield_48237")
	private String ppaId;
	
	@JsonProperty("customfield_37393")
	private String ctName;
	
	@JsonProperty("customfield_44409")
	private TypeValue risk;
	
	@JsonProperty("customfield_32045")
	private String ngseetUrl;
	
	@JsonProperty("customfield_26206")
	private TypeValue status;	
	
	@JsonProperty("customfield_38071")
	private String actualStartDate;
	
	@JsonProperty("customfield_37285")
	private String actualEndDate;

	public String getActualEndDate() {
		return actualEndDate;
	}

	public void setActualEndDate(String actualEndDate) {
		this.actualEndDate = actualEndDate;
	}

	public String getActualStartDate() {
		return actualStartDate;
	}

	public void setActualStartDate(String actualStartDate) {
		this.actualStartDate = actualStartDate;
	}

	public List<String> getBgdmName() {
		return bgdmName;
	}

	public void setBgdmName(List<String> bgdmName) {
		this.bgdmName = bgdmName;
	}

	public String getMarketUnit() {
		return marketUnit;
	}

	public void setMarketUnit(String marketUnit) {
		this.marketUnit = marketUnit;
	}

	public String getPpaId() {
		return ppaId;
	}

	public void setPpaId(String ppaId) {
		this.ppaId = ppaId;
	}

	public String getCtName() {
		return ctName;
	}

	public void setCtName(String ctName) {
		this.ctName = ctName;
	}

	public String getNgseetUrl() {
		return ngseetUrl;
	}

	public void setNgseetUrl(String ngseetUrl) {
		this.ngseetUrl = ngseetUrl;
	}

	public TypeValue getStatus() {
		return status;
	}

	public void setStatus(TypeValue status) {
		this.status = status;
	}


	public TypeValue getRisk() {
		return risk;
	}

	public void setRisk(TypeValue risk) {
		this.risk = risk;
	}


	public TypeValue getMarket() {
		return market;
	}

	public void setMarket(TypeValue market) {
		this.market = market;
	}

	public TypeValue getProjectType() {
		return projectType;
	}

	public void setProjectType(TypeValue projectType) {
		this.projectType = projectType;
	}

	
	
}
