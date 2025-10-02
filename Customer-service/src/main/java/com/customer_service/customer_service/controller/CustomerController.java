package com.customer_service.customer_service.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.customer_service.customer_service.dto.request.CustomerRequestDto;
import com.customer_service.customer_service.dto.response.CustomerResponseDto;
import com.customer_service.customer_service.service.CustomerServiceImp;


@RestController
@RequestMapping("/api/customers")
public class CustomerController {
	
	@Autowired
	private CustomerServiceImp CustomerService;
	
	
	@GetMapping("/")
	public ResponseEntity<List<CustomerResponseDto>> fetchAllCustomers(){
		List<CustomerResponseDto> customers = new ArrayList<>();
		try {
			customers = CustomerService.fetchAllCustomers();
		}catch(Exception e) {
			System.out.println("Exception occure during fetching all the customers : "+e.getMessage());
		}
		return new ResponseEntity<List<CustomerResponseDto>>(customers, HttpStatus.OK);
		
	}
	
	@PostMapping("/create")
	public ResponseEntity<CustomerResponseDto> newCustomer(@RequestBody CustomerRequestDto custDto){
		CustomerResponseDto resDto = null;
		try {
			resDto = CustomerService.createNewCustomer(custDto);
			
		}catch(Exception e) {
			System.out.println("Exception occure during creating the customer : "+e.getMessage());
		}
		
		return new ResponseEntity<>(resDto,HttpStatus.OK);
		
	}
	
	
	


}
