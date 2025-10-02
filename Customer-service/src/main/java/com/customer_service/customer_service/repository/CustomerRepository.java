package com.customer_service.customer_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.customer_service.customer_service.model.CustomerModel;


@Repository
public interface CustomerRepository extends JpaRepository<CustomerModel, Integer> {

}
