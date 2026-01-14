package com.nokia.ace.core.login.data.dao.maria;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nokia.ace.core.login.data.model.maria.DomainMaster;

@Repository
public interface UserDomainDetailDao extends JpaRepository<DomainMaster, Integer> {

	@Query(value = "SELECT DISTINCT dm.id,domain_name, display_name,dm.status FROM blueprint b\r\n" + 
			"JOIN business_unit_domain_mapping budm ON  b.business_unit_domain_mapping_id = budm.id\r\n" + 
			"JOIN domain_master dm ON budm.domain_master_id = dm.id WHERE created_by= ?1\r\n" + 
			"UNION\r\n" + 
			"SELECT DISTINCT dm.id,domain_name, display_name,dm.status FROM project_user_mapping pum\r\n" + 
			"JOIN project p ON  pum.project_id = p.id JOIN blueprint b ON p.blueprint_id = b.id\r\n" + 
			"JOIN business_unit_domain_mapping budm  ON  b.business_unit_domain_mapping_id = budm.id\r\n" + 
			"JOIN domain_master dm ON budm.domain_master_id = dm.id WHERE user_mail=?2", nativeQuery = true)
	List<DomainMaster> fetchBlueprintUserDomain(String username, String email);

}
