package com.nokia.ace.core.login.data.model.maria;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "domain_master")
public class DomainMaster {
	@Id
	private int id;
	@Column(name = "domain_name")
	private String domainName;
	@Column(name = "display_name")
	private String displayName;
	@Column(name= "status")
	private String status;

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	

}
