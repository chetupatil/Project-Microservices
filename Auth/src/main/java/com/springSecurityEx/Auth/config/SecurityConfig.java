package com.springSecurityEx.Auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//		http.csrf(customize -> customize.disable());
//		http.authorizeHttpRequests(request -> request.anyRequest().authenticated());
//		// this config for browser
//		//http.formLogin(Customizer.withDefaults());
//		// this config for postman
//		http.httpBasic(Customizer.withDefaults());
//		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//		
		
		return http.csrf(customize -> customize.disable())
				.authorizeHttpRequests(request -> request.anyRequest().authenticated())
		        .httpBasic(Customizer.withDefaults())
		        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		        .build();
		
	}
	
//	@Bean 
//	public UserDetailsService userDetailsService() {
//		UserDetails user1 = User.withDefaultPasswordEncoder()
//				.username("chetana")
//				.password("patil")
//				.build();
//		return new InMemoryUserDetailsManager(user1);
//		
//	}
	
	
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProv = new DaoAuthenticationProvider();
		authProv.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
		authProv.setUserDetailsService(userDetailsService);
		return authProv;
		
	}
	
	

}
