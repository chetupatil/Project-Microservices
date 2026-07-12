package com.springAI.chatAI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import com.springAI.chatAI.service.SpringApplicationAvaliability;

@SpringBootApplication
public class Application {
	
	
	

	public static void main(String[] args) {
		
//		SpringApplicationAvaliability sp = new SpringApplicationAvaliability();
//		
//	   sp.checkApplicationAvailability();
//		
		SpringApplication.run(Application.class, args);
		
		
		
//		new SpringApplicationBuilder()
//		.sources(Application.class)
//		.child(Application.class)
//		.bannerMode(Banner.Mode.OFF)
//		.run(args);
		
		
		
	}

}
