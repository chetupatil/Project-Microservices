package com.nokia.ace.ppa.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "pmat_master")
public class PmatMaster {
	
	@Id
	private int id;
	
	@Column(name= "OpportunityNumber")
	private String OpportunityNumber;
	
	@Column(name= "OpportunityName")
	private String OpportunityName;
	
	@Column(name = "BGDMName")
	private String BGDMName;
	
	@Column(name= "Description")
	private String Description;
	
	@Column(name= "Market")
	private String Market;
	
	@Column(name= "MarketUnit")
	private String MarketUnit;
	
	@Column(name= "CBT")
	private String CBT;
	
	@Column(name= "CT")
	private String CT;
	
	@Column(name = "Country")
	private String Country;

	@Column(name= "CustomerName")
	private String CustomerName;
	
	@Column(name= "IncludedBUs")
	private String IncludedBUs;
	
	@Column(name= "PSEstimationGroup")
	private String PSEstimationGroup;
	
	@Column(name= "Risk")
	private String Risk;
	
	@Column(name= "Status")
	private String Status;
	
	@Column(name= "ServiceStartDate")
	private String ServiceStartDate;

	@Column(name = "ServiceEndDate")
	private String ServiceEndDate;
	
	@Column(name= "Comments")
	private String Comments;
	
	@Column(name= "EnoviaProductRelease")
	private String EnoviaProductRelease;
	
	
	@Column(name= "PLsProspectScope")
	private String PLsProspectScope;
	
	@Column(name= "ngSEET")
	private String ngSEET;
	
	@Column(name= "ngSEETURL")
	private String ngSEETURL;
	
	@Column(name= "XSU")
	private String XSU;
	
	@Column(name= "XSUUrl")
	private String XSUURL;
	
	@Column(name= "CreatedBy")
	private String CreatedBy;
	
	@Column(name= "ModifiedBy")
	private String ModifiedBy;
	
	@Column(name= "Created")
	private String created;
	
	@Column(name= "Modified")
	private String modified;
	
	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	@Column(name= "IsDeleted")
	private String isDeleted;
	
	@Column(name = "IsProspectCreated")
	private String IsProspectCreated;
	


	public String getIsProspectCreated() {
		return IsProspectCreated;
	}

	public void setIsProspectCreated(String isProspectCreated) {
		IsProspectCreated = isProspectCreated;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOpportunityNumber() {
		return OpportunityNumber;
	}

	public void setOpportunityNumber(String opportunityNumber) {
		OpportunityNumber = opportunityNumber;
	}

	public String getOpportunityName() {
		return OpportunityName;
	}

	public void setOpportunityName(String opportunityName) {
		OpportunityName = opportunityName;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getMarket() {
		return Market;
	}

	public void setMarket(String market) {
		Market = market;
	}

	public String getMarketUnit() {
		return MarketUnit;
	}

	public void setMarketUnit(String marketUnit) {
		MarketUnit = marketUnit;
	}

	public String getCBT() {
		return CBT;
	}

	public void setCBT(String cBT) {
		CBT = cBT;
	}

	public String getCT() {
		return CT;
	}

	public void setCT(String cT) {
		CT = cT;
	}

	public String getIncludedBUs() {
		return IncludedBUs;
	}

	public void setIncludedBUs(String includedBUs) {
		IncludedBUs = includedBUs;
	}

	public String getPSEstimationGroup() {
		return PSEstimationGroup;
	}

	public void setPSEstimationGroup(String pSEstimationGroup) {
		PSEstimationGroup = pSEstimationGroup;
	}

	public String getRisk() {
		return Risk;
	}

	public void setRisk(String risk) {
		Risk = risk;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getServiceStartDate() {
		return ServiceStartDate;
	}

	public void setServiceStartDate(String serviceStartDate) {
		ServiceStartDate = serviceStartDate;
	}

	public String getServiceEndDate() {
		return ServiceEndDate;
	}

	public void setServiceEndDate(String ServiceEndDate) {
		this.ServiceEndDate = ServiceEndDate;
	}

	public String getComments() {
		return Comments;
	}

	public void setComments(String comments) {
		Comments = comments;
	}

	public String getEnoviaProductRelease() {
		return EnoviaProductRelease;
	}

	public void setEnoviaProductRelease(String enoviaProductRelease) {
		EnoviaProductRelease = enoviaProductRelease;
	}

	public String getPLsProspectScope() {
		return PLsProspectScope;
	}

	public void setPLsProspectScope(String pLsProspectScope) {
		PLsProspectScope = pLsProspectScope;
	}

	public String getNgSEET() {
		return ngSEET;
	}

	public void setNgSEET(String ngSEET) {
		this.ngSEET = ngSEET;
	}

	public String getNgSEETURL() {
		return ngSEETURL;
	}

	public void setNgSEETURL(String ngSEETURL) {
		this.ngSEETURL = ngSEETURL;
	}

	public String getXSU() {
		return XSU;
	}

	public void setXSU(String xSU) {
		XSU = xSU;
	}

	public String getXSUURL() {
		return XSUURL;
	}

	public void setXSUURL(String xSUURL) {
		XSUURL = xSUURL;
	}

	public String getCreatedBy() {
		return CreatedBy;
	}

	public void setCreatedBy(String createdBy) {
		CreatedBy = createdBy;
	}

	public String getModifiedBy() {
		return ModifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		ModifiedBy = modifiedBy;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getModified() {
		return modified;
	}

	public void setModified(String modified) {
		this.modified = modified;
	}

	public PmatMaster() {
		
	}

	public String getBGDMName() {
		return BGDMName;
	}

	public void setBGDMName(String bGDMName) {
		BGDMName = bGDMName;
	}

	public String getCustomerName() {
		return CustomerName;
	}

	public void setCustomerName(String customerName) {
		CustomerName = customerName;
	}
	
	
	public String getCountry() {
		return Country;
	}

	public void setCountry(String country) {
		Country = country;
	}
	
//	public PmatMaster(int parseInt, String string, String string2, String string3, String string4, String string5,
//			String string6, String string7, String string8, String string9, String string10, String string11,
//			String string12, String string13, String string14, String string15, String string16, String string17,
//			String string18, String string19, String string20, String string21, String string22, String string23,
//			String string24) {
//		this.id = parseInt;
//		this.OpportunityNumber = string;
//		this.OpportunityName = string2;
//		this.Description = string3;
//		this.Market = string4;
//		this.MarketUnit = string5;
//		this.CBT = string6;
//		this.CT = string7;
//		this.CustomerName = string8;
//		this.IncludedBUs = string9;
//		this.PSEstimationGroup = string10;
//		this.Risk = string11;
//		this.Status = string12;
//		this.ServiceStartDate = string13;
//		this.Comments = string14;
//		this.EnoviaProductRelease = string15;
//		this.PLsProspectScope = string16;
//		this.ngSEET = string17;
//		this.ngSEETURL = string18;
//		this.XSU = string19;
//		this.XSUURL = string20;
//		this.CreatedBy = string21;
//		this.ModifiedBy = string22;
//		this.created = string23;
//		this.modified = string24;
//	}
//

}
