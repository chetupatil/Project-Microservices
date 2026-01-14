package com.nokia.ace.core.login;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.nokia.ace.core.commons.service.DatabaseService;
import com.nokia.ace.core.commons.service.VersionService;
import com.nokia.ace.core.commons.serviceImpl.DatabaseServiceImpl;
import com.nokia.ace.core.commons.serviceImpl.VersionServiceImpl;
import com.nokia.ace.core.commons.utils.FileStorageProperties;




@SpringBootApplication(scanBasePackages = { "com.nokia.ace.core.login",
		"com.nokia.ace.core.commons.service",
		"com.nokia.ace.core.commons.serviceImpl",
		"com.nokia.ace.core.commons.utils",
		"com.nokia.ace.core.login.service"})
@EnableAutoConfiguration
@EnableConfigurationProperties({ FileStorageProperties.class })
/*@EnableJpaRepositories(basePackages= {"com.nokia.ace.core.blueprint.dao.maria", "com.nokia.ace.dao.maria"})
@EntityScan(basePackages= {"com.nokia.ace.core.blueprint.model.maria", "com.nokia.ace.model.maria"})*/
public class BootLoginService {
	
	public static void main(String[] args) throws IOException {
/*		ApplicationContext applicationContext = SpringApplication.run(BootBlueprintService.class, args);
		ACEApplicationContext aceApplicationContext = new ACEApplicationContext();
		aceApplicationContext.setApplicationContext(applicationContext);*/
		ApplicationContext applicationContext = SpringApplication.run(BootLoginService.class, args);
        VersionService service = applicationContext.getBean(VersionServiceImpl.class);
        service.appSetup();
        DatabaseService dataService = applicationContext.getBean(DatabaseServiceImpl.class);
        dataService.appSetup();
	}
}