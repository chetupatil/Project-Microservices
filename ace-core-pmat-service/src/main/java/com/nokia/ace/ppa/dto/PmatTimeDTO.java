package com.nokia.ace.ppa.dto;

import java.util.Set;

public class PmatTimeDTO {

	public String getWorkpackage() {
		return workpackage;
	}
	public void setWorkpackage(String workpackage) {
		this.workpackage = workpackage;
	}
	public Set<Long> getDate() {
		return date;
	}
	public void setDate(Set<Long> date) {
		this.date = date;
	}
	private String workpackage;
	private Set<Long> date;
}
