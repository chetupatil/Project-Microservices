package com.product_service.product_service.controller;


import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product")
public class ProductController {
	
	@Value("${server.port}")
	private String port;
	
	@GetMapping("/loadBalanceConsumer")
	public ResponseEntity<String> loadBalancerConsumerMethod(){
		
		String str = "I am received the request from customer Load balancer port : "+port;
		System.out.println(str);
		return new ResponseEntity<>(str,HttpStatus.OK);
		
	}

}
