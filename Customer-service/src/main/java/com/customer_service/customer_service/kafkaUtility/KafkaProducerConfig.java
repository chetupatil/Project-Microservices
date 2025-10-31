package com.customer_service.customer_service.kafkaUtility;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import com.fasterxml.jackson.databind.ser.std.StringSerializer;

import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;

@Configuration
public class KafkaProducerConfig {
	
	@Value("${spring.kafka.bootstrap-servers}")
	private String serverName;


    @Bean
    public ProducerFactory<String, Object> produceFactory(){
		Map<String,Object> producerConfig = new HashMap<String,Object>();
		producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		producerConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		producerConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		return new DefaultKafkaProducerFactory<String, Object>(producerConfig);
	}

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate(){
		return new KafkaTemplate<String, Object>(produceFactory());
		
	}
}
