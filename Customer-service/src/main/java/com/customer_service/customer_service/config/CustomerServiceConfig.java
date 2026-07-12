package com.customer_service.customer_service.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CustomerServiceConfig {


    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
