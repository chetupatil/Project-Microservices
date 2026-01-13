package com.nokia.ace.ppa.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nokia.ace.ppa.dto.PmatMaster;
/**
 * @author Chetana
 *
 */
public interface PmatMasterDao extends JpaRepository<PmatMaster, Integer> {
	
	
	@Query(value = "INSERT INTO pmat_master(id, OpportunityNumber, OpportunityName, "
			+ "BGDMName, Description, Market, MarketUnit, CBT, CT,Country, CustomerName, IncludedBUs,"
			+ "PSEstimationGroup, Risk, Status, ServiceStartDate, ServiceEndDate, Comments, EnoviaProductRelease, "
			+ "PLsProspectScope, ngSEET, ngSEETURL, XSU, XSUUrl, CreatedBy, ModifiedBy, Created,"
			+ " Modified,IsDeleted,IsProspectCreated) VALUES(?1,?2,?3,?4,?5,?6,?7,?8,?9,?10,?11,?12,?13,?14,?15,?16,?17,?18,?19,"
			+ "?20,?21,?22,?23,?24,?25,?26,?27,?28,?29,?30)", nativeQuery = true)
	void insertPmatMaster(int id, String st1,String st2, String st3, String st4, String st5,  String st6, 
			 String st7,  String st8,  String st9, String country, String st10,  String st11,  String st12,
			 String st13,  String st14,  String st15,  String st16, String endDate,  String st17,  String st18,  String st19
			 ,String st20,  String st21,  String st22,  String st23,  String st24,  String st25,String st26,String st27);
	
	
//	@Query(value = "SELECT id, OpportunityNumber,Market,Status FROM pmat_master",nativeQuery = true)
//	List<Object[]> findByOpportunityNumber();
	
	@Query(value = "SELECT id, OpportunityNumber,OpportunityName,BGDMName,Market,MarketUnit,CBT,CT,Risk,Status,Comments,EnoviaProductRelease,PLsProspectScope,ngSEETURL,CreatedBy,ModifiedBy,Created,Modified,ServiceStartDate FROM pmat_master",nativeQuery = true)
	List<Object[]> findByOpportunityNumber();
	
	
	@Query(value = "SELECT id, OpportunityNumber,OpportunityName, BGDMName, IsDeleted FROM pmat_master",nativeQuery = true)
	List<Object[]> findByOpportunity();
	
	@Query(value = "SELECT COUNT(*) FROM pmat_master",nativeQuery = true)
	int countPmatMaster();
	
	@Query(value = "SELECT ServiceStartDate,PLsProspectScope,Market FROM pmat_master WHERE id =?1",nativeQuery = true)
	List<Object[]> fetchStartDate(String ppaId);
	
	@Query(value = "SELECT id,OpportunityNumber,ServiceStartDate FROM pmat_master WHERE Modified > ?1 AND Modified < ?2",nativeQuery = true)
	List<Object[]> fetchLatestStartDate(String yesterday,String today);
	


}
