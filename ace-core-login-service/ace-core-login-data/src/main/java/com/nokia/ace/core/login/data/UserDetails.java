package com.nokia.ace.core.login.data;

import java.util.Base64;
import java.util.List;
import java.util.Set;


public class UserDetails {

	private String userid;
	private String email;
	private String name;
	private List<DomainMasterDto> domain;
	private Set<String> exp;
	private String eUserid;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<String> getExp() {
		return exp;
	}

	public void setExp(Set<String> exp) {
		this.exp = exp;
	}

	public List<DomainMasterDto> getDomain() {
		return domain;
	}

	public void setDomain(List<DomainMasterDto> domain) {
		this.domain = domain;
	}

	public void setEUserId(String userid) {
	      this.eUserid = Base64.getEncoder().encodeToString(userid.getBytes());
	}

	public String getEUserId() {
	    return  eUserid;
	}
	

}
