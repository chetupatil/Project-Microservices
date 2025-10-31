package com.notification.Notification_service.service;


import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl {
	
	@KafkaListener(topics="cust-prod-topic",groupId="group_id")
	public void consumeCustomerNotification(String message) {
		System.out.println("message : "+message);
	}
	

}
