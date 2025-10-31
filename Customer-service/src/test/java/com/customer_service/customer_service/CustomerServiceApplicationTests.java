package com.customer_service.customer_service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.customer_service.customer_service.service.CustomerService;

@SpringBootTest
class CustomerServiceApplicationTests {
	
	@Autowired
	CustomerService custService;
	
	@Before
	void initialTest() {
		
	}

	@Test
	void contextLoads() {
		int addNum = custService.addTwo(2, 2);
		assertEquals(4, addNum);
		
	}
	@Test
	void illgalArgumentExceptionTest() {
		assertThrows(IllegalArgumentException.class, () ->{
			custService.addTwo(0, 0);
		});
	}
	
	@After
	void endTest() {
		
	}

}
