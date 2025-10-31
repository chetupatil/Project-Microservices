package com.customer_service.customer_service.service;

import java.util.List;

import com.customer_service.customer_service.dto.request.CustomerRequestDto;
import com.customer_service.customer_service.dto.response.CustomerResponseDto;


public interface CustomerService {
	
	public List<CustomerResponseDto> fetchAllCustomers();
	
	public CustomerResponseDto createNewCustomer(CustomerRequestDto custDto);
	
	
	public Integer addTwo(Integer a, Integer b);

}
