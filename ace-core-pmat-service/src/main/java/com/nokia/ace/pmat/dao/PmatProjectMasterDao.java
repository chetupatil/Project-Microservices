package com.nokia.ace.pmat.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nokia.ace.pmat.dto.PmatProjectMaster;

public interface PmatProjectMasterDao extends JpaRepository<PmatProjectMaster, Integer>{
	
	@Query(value="select * from pmat_project_master group by id;",nativeQuery=true)
	List<Object[]> fetchPmatProjectdata();
	
	@Query(value="SELECT distinct Opportunity_Number_Condition FROM pmat_project_master",nativeQuery=true)
	List<Object[]> fetchSCRMIds();
	
	@Query(value="SELECT project_identifier,Opportunity_Number_Condition FROM pmat_project_master where Opportunity_Number_Condition = ?1",nativeQuery=true)
	List<Object[]> fetchPmatIds(String scrmId);
	
	@Query(value="SELECT id, Opportunity_Number_Condition, BGDM, MarketPMAT, MU, CBT, CT,Execution_Country, Products, Project_Status,Project_Owner, Project_Created, Modified, Customer_Name, Start_Date,Finish_Date,Project_Name FROM pmat_project_master where Opportunity_Number_Condition = ?1 and Project_Identifier = ?2",nativeQuery=true)
	List<Object[]> fetchPmatdata(String scrmId, String pmatId);
	
	@Query(value = "SELECT COUNT(*) FROM pmat_project_master",nativeQuery = true)
	int countPmatProjectMaster();
	
	@Query(value = "SELECT COUNT(*) FROM pmat_project_master WHERE project_identifier=?1 AND Opportunity_Number_Condition=?2",nativeQuery=true)
	int checkIfRecordExists(String pmatId, String scrmId);
	
	@Query(value = "ALTER TABLE pmat_project_master CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci",nativeQuery=true)
	void alterCharacterset();
	
	@Query(value = "UPDATE pmat_project_master SET AIM=?3 , BGDM=?4 , CBT=?5 , CT=?6 , Customer_Legal_Id=?7 , Customer_Name=?8 , D2C=?9 , Delivery_Type=?10 ,"
			+ " ERPWBSs=?11 , Execution_Country=?12 , Finish_Date=?13 , GEOL1=?14 , GEOL2=?15 , GEOL3=?16 , GEOL4=?17 , JIRAKey=?18 , Last_Published=?19 ,"
			+ " LeadBG=?20 , LeadBU=?21 , Market=?22 , MarketPMAT=?23 , MU=?24 , Modified=?25 , Opportunity_Number=?26 , PBS=?27 , PCube_Financials=?28,"
			+ " PMATFinancials=?29 , PMView_Name=?30 , POROAvailability=?31 , Products=?32 , Project_Classification=?33 , Project_Created=?34 , Project_Name=?35 ,"
			+ " Project_Owner=?36 , Project_Status=?37 , Project_Structure=?38 , RootID=?39 , SG6Approval_Date=?40 , SG6Planned_Date=?41 , Start_Date=?42 WHERE project_identifier =?1 AND Opportunity_Number_Condition=?2",nativeQuery=true)
	void updateRow(String projectIdentifier, String opportunityNumberCondition, String aim, String bgdm, String cbt,
			String ct, String customerLegalID, String customerName, String d2c, String deliveryType, String erpwbSs,
			String executionCountry, String finishDate, String geol1, String geol2, String geol3, String geol4,
			String jiraKey, String lastPublished, String leadBG, String leadBU, String market, String marketPMAT,
			String marketUnit, String modified, String opportunityNumber, String pbs, String pCubeFinancials,
			String pmatFinancials, String pmViewName, String poroAvailability, String products,
			String projectClassification, String projectCreated, String projectName, String projectOwner,
			String projectStatus, String projectStructure, String rootID, String sg6ApprovalDate, String sg6PlannedDate,
			String startDate);
}
