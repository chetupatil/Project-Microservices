package com.springAI.chatAI.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.availability.ApplicationAvailability;
import org.springframework.boot.availability.LivenessState;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

@Component
public class SpringApplicationAvaliability implements CommandLineRunner{

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Application is started .......... ");
		
	}
	

//	ApplicationAvailability ava;
//	 
//	
//	public void checkApplicationAvailability() {
//		
//		LivenessState live = ava.getLivenessState();
//		ReadinessState read = ava.getReadinessState();
//		
//		System.out.println(live);
//		System.out.println(read);
//	}

}
