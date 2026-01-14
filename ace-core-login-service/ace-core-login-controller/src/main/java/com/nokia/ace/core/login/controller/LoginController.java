/*
 * Copyright (c) Nokia Solutions and Networks. All rights reserved.  
 * Project Name: ACE AUTOMATE
 * File name: LoginController.java
 *
 * Description: Login Class that authorizes users.
 *
 * Creation Date: 01-Mar-2019
 * Author: Akshit
 * 
 * History:
 */

package com.nokia.ace.core.login.controller;

import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.keycloak.representations.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import com.nokia.ace.core.login.data.DomainMasterDto;
import com.nokia.ace.core.login.data.UserDetails;
import com.nokia.ace.core.login.data.model.maria.DomainMaster;
import com.nokia.ace.core.login.service.LoginService;

import io.swagger.annotations.Api;

/**
 * Login Class that authorizes users.
 * 
 * @author AK20023939
 * @version [version number, YYYY-MM-DD]
 * @see []
 * @since [19.6/1]
 */

@Controller
@RequestMapping("/")
@Api(tags = "login")
public class LoginController {
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

	@Value("${keycloak.resource}")
	String resource;
	@Value("${spring.redirect}")
	String url;
	@Autowired
	private AccessToken accessToken;

	@Autowired
	private LoginService loginService;

	/**
	 * Call this method to logout the user
	 * 
	 * @param request
	 * 
	 * @return [Redirection after the user signs out]
	 */
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request) throws ServletException {
		request.logout();
		return "redirect:" + "/";
	}

	/**
	 * Method to sign in a user according to roles
	 * 
	 * @param
	 * 
	 * @return [url where the user will be redirected]
	 */
	@RequestMapping("/api/login")
	public RedirectView login() {
		try {
			accessToken.getPreferredUsername();

		} catch (Exception e) {
			LOGGER.error("Access token could not be retrieved");
		}
		try {
			Set<String> roles = accessToken.getResourceAccess(resource).getRoles();
			LOGGER.info("USER_LOGIN : " + accessToken.getPreferredUsername());
			RedirectView redirectView = new RedirectView();
			if ((!roles.isEmpty())) {
				redirectView.setUrl("/home?loginEvent=success");
			}
			return redirectView;
		} catch (NullPointerException e) {
			LOGGER.info("USER_LOGIN (NO ROLE ASSIGNED) :" + accessToken.getPreferredUsername());
			RedirectView redirectView = new RedirectView();
			LOGGER.info("USER_LOGIN (NO ROLE ASSIGNED) :" + accessToken.getPreferredUsername());
			redirectView.setUrl("/getAccess?loginEvent=failure");
			return redirectView;
		}
	}

	/**
	 * Method to get authorization details of the user
	 * 
	 * @param
	 * 
	 * @return [user details]
	 */
	@GetMapping("/api/authorize")
	public ResponseEntity<UserDetails> authorize() {
		UserDetails details = new UserDetails();
		try {

			details.setExp(accessToken.getResourceAccess(resource).getRoles());
			details.setUserid(accessToken.getPreferredUsername());
			details.setName(accessToken.getName());
			details.setEmail(accessToken.getEmail());
			details.setEUserId(accessToken.getPreferredUsername());
			List<DomainMasterDto> getDomain = loginService.getUserDomain(accessToken.getPreferredUsername(),
					accessToken.getEmail());
			details.setDomain(getDomain);
			return new ResponseEntity(details, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.info("Exception occurred while authorizing"+e.getMessage());
			details.setUserid(accessToken.getPreferredUsername());
			details.setName(accessToken.getName());
			details.setEmail(accessToken.getEmail());
			return new ResponseEntity(details, HttpStatus.OK);
		}
	}
}
