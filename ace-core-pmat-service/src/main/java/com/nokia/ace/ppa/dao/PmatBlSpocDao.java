package com.nokia.ace.ppa.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nokia.ace.ppa.dto.PmatBlSpocDTO;

/**
 * @author Chetana
 *
 */
public interface PmatBlSpocDao extends JpaRepository<PmatBlSpocDTO, Integer> {

	@Query(value = "SELECT * FROM ba_spoc_drp",nativeQuery = true)
	List<Object[]> fetchBaSpocEmailList();
	
	@Query(value = "SELECT * FROM ba_spoc_drp where BL=?1", nativeQuery = true)
	List<Object[]> fetchBaSpocEmailBasedOnBl(String bl);
}
