package com.nokia.ace.pmat.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "pmat_project_master")
public class PmatProjectMaster {
	
	@Id
	private int id;	
	@Column(name= "Market")
	private String Market;
	@Column(name= "MU")
	private String MarketUnit;
	@Column(name= "CBT")
	private String CBT;
	@Column(name= "CT")
	private String CT;
	@Column(name= "Project_Name")
	private String ProjectName;
	@Column(name= "Project_Owner")
	private String ProjectOwner;
	@Column(name= "BGDM")
	private String BGDM;
	@Column(name= "Project_Identifier")
	private String ProjectIdentifier;
	@Column(name= "Project_Classification")
	private String ProjectClassification;
	@Column(name= "Project_Structure")
	private String ProjectStructure;
	@Column(name= "Project_Status")
	private String ProjectStatus;
	@Column(name= "LeadBU")
	private String LeadBU;
	@Column(name= "RootID")
	private String RootID;
	@Column(name= "PBS")
	private String PBS;
	@Column(name= "Customer_Name")
	private String CustomerName;
	@Column(name= "Customer_Legal_Id")
	private String CustomerLegalID;
	@Column(name= "Products")
	private String Products;
	@Column(name= "LeadBG")
	private String LeadBG;
	@Column(name= "Opportunity_Number_Condition")
	private String OpportunityNumberCondition;
	@Column(name= "Opportunity_Number")
	private String OpportunityNumber;
	@Column(name= "PMView_Name")
	private String PMViewName;
	@Column(name= "ERPWBSs")
	private String ERPWBSs;
	@Column(name= "MarketPMAT")
	private String MarketPMAT;
	@Column(name= "GEOL1")
	private String GEOL1;
	@Column(name= "GEOL2")
	private String GEOL2;
	@Column(name= "GEOL3")
	private String GEOL3;
	@Column(name= "GEOL4")
	private String GEOL4;
	@Column(name= "Execution_Country")
	private String ExecutionCountry;
	@Column(name= "SG6Planned_Date")
	private String SG6PlannedDate;
	@Column(name= "SG6Approval_Date")
	private String SG6ApprovalDate;
	@Column(name= "Project_Created")
	private String ProjectCreated;
	@Column(name= "Start_Date")
	private String StartDate;
	@Column(name= "Finish_Date")
	private String FinishDate;
	@Column(name= "Modified")
	private String Modified;
	@Column(name= "Last_Published")
	private String LastPublished;
	@Column(name= "JIRAKey")
	private String JIRAKey;
	@Column(name= "POROAvailability")
	private String POROAvailability;
	@Column(name= "PCube_Financials")
	private String PCubeFinancials;
	@Column(name= "PMATFinancials")
	private String PMATFinancials;
	@Column(name= "AIM")
	private String AIM;
	@Column(name= "D2C")
	private String D2C;
	@Column(name= "Delivery_Type")
	private String DeliveryType;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public String getProjectName() {
		return ProjectName;
	}
	public void setProjectName(String projectName) {
		ProjectName = projectName;
	}
	public String getProjectOwner() {
		return ProjectOwner;
	}
	public void setProjectOwner(String projectOwner) {
		ProjectOwner = projectOwner;
	}
	public String getBGDM() {
		return BGDM;
	}
	public void setBGDM(String bGDM) {
		BGDM = bGDM;
	}
	public String getProjectIdentifier() {
		return ProjectIdentifier;
	}
	public void setProjectIdentifier(String projectIdentifier) {
		ProjectIdentifier = projectIdentifier;
	}
	public String getProjectClassification() {
		return ProjectClassification;
	}
	public void setProjectClassification(String projectClassification) {
		ProjectClassification = projectClassification;
	}
	public String getProjectStructure() {
		return ProjectStructure;
	}
	public void setProjectStructure(String projectStructure) {
		ProjectStructure = projectStructure;
	}
	public String getProjectStatus() {
		return ProjectStatus;
	}
	public void setProjectStatus(String projectStatus) {
		ProjectStatus = projectStatus;
	}
	public String getLeadBU() {
		return LeadBU;
	}
	public void setLeadBU(String leadBU) {
		LeadBU = leadBU;
	}
	public String getRootID() {
		return RootID;
	}
	public void setRootID(String rootID) {
		RootID = rootID;
	}
	public String getPBS() {
		return PBS;
	}
	public void setPBS(String pBS) {
		PBS = pBS;
	}
	public String getCustomerName() {
		return CustomerName;
	}
	public void setCustomerName(String customerName) {
		CustomerName = customerName;
	}
	public String getCustomerLegalID() {
		return CustomerLegalID;
	}
	public void setCustomerLegalID(String customerLegalID) {
		CustomerLegalID = customerLegalID;
	}
	public String getProducts() {
		return Products;
	}
	public void setProducts(String products) {
		Products = products;
	}
	public String getLeadBG() {
		return LeadBG;
	}
	public void setLeadBG(String leadBG) {
		LeadBG = leadBG;
	}
	public String getOpportunityNumberCondition() {
		return OpportunityNumberCondition;
	}
	public void setOpportunityNumberCondition(String opportunityNumberCondition) {
		OpportunityNumberCondition = opportunityNumberCondition;
	}
	public String getOpportunityNumber() {
		return OpportunityNumber;
	}
	public void setOpportunityNumber(String opportunityNumber) {
		OpportunityNumber = opportunityNumber;
	}
	public String getPMViewName() {
		return PMViewName;
	}
	public void setPMViewName(String pMViewName) {
		PMViewName = pMViewName;
	}
	public String getERPWBSs() {
		return ERPWBSs;
	}
	public void setERPWBSs(String eRPWBSs) {
		ERPWBSs = eRPWBSs;
	}
	public String getMarketPMAT() {
		return MarketPMAT;
	}
	public void setMarketPMAT(String marketPMAT) {
		MarketPMAT = marketPMAT;
	}
	public String getGEOL1() {
		return GEOL1;
	}
	public void setGEOL1(String gEOL1) {
		GEOL1 = gEOL1;
	}
	public String getGEOL2() {
		return GEOL2;
	}
	public void setGEOL2(String gEOL2) {
		GEOL2 = gEOL2;
	}
	public String getGEOL3() {
		return GEOL3;
	}
	public void setGEOL3(String gEOL3) {
		GEOL3 = gEOL3;
	}
	public String getGEOL4() {
		return GEOL4;
	}
	public void setGEOL4(String gEOL4) {
		GEOL4 = gEOL4;
	}
	public String getExecutionCountry() {
		return ExecutionCountry;
	}
	public void setExecutionCountry(String executionCountry) {
		ExecutionCountry = executionCountry;
	}
	public String getSG6PlannedDate() {
		return SG6PlannedDate;
	}
	public void setSG6PlannedDate(String sG6PlannedDate) {
		SG6PlannedDate = sG6PlannedDate;
	}
	public String getSG6ApprovalDate() {
		return SG6ApprovalDate;
	}
	public void setSG6ApprovalDate(String sG6ApprovalDate) {
		SG6ApprovalDate = sG6ApprovalDate;
	}
	public String getProjectCreated() {
		return ProjectCreated;
	}
	public void setProjectCreated(String projectCreated) {
		ProjectCreated = projectCreated;
	}
	public String getStartDate() {
		return StartDate;
	}
	public void setStartDate(String startDate) {
		StartDate = startDate;
	}
	public String getFinishDate() {
		return FinishDate;
	}
	public void setFinishDate(String finishDate) {
		FinishDate = finishDate;
	}
	public String getModified() {
		return Modified;
	}
	public void setModified(String modified) {
		Modified = modified;
	}
	public String getLastPublished() {
		return LastPublished;
	}
	public void setLastPublished(String lastPublished) {
		LastPublished = lastPublished;
	}
	public String getJIRAKey() {
		return JIRAKey;
	}
	public void setJIRAKey(String jIRAKey) {
		JIRAKey = jIRAKey;
	}
	public String getPOROAvailability() {
		return POROAvailability;
	}
	public void setPOROAvailability(String pOROAvailability) {
		POROAvailability = pOROAvailability;
	}
	public String getPCubeFinancials() {
		return PCubeFinancials;
	}
	public void setPCubeFinancials(String pCubeFinancials) {
		PCubeFinancials = pCubeFinancials;
	}
	public String getPMATFinancials() {
		return PMATFinancials;
	}
	public void setPMATFinancials(String pMATFinancials) {
		PMATFinancials = pMATFinancials;
	}
	public String getAIM() {
		return AIM;
	}
	public void setAIM(String aIM) {
		AIM = aIM;
	}
	public String getD2C() {
		return D2C;
	}
	public void setD2C(String d2c) {
		D2C = d2c;
	}
	public String getDeliveryType() {
		return DeliveryType;
	}
	public void setDeliveryType(String deliveryType) {
		DeliveryType = deliveryType;
	}
	
}
