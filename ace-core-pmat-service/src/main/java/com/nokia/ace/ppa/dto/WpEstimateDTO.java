package com.nokia.ace.ppa.dto;

public class WpEstimateDTO {
	
	private String aggregateEfforts;
	private Long wpStartDate;
	private Long wpEndDate;
	private Long lastModifiedDate;
	public String getAggregateEfforts() {
		return aggregateEfforts;
	}
	public void setAggregateEfforts(String aggregateEfforts) {
		this.aggregateEfforts = aggregateEfforts;
	}
	public Long getWpStartDate() {
		return wpStartDate;
	}
	public void setWpStartDate(Long wpStartDate) {
		this.wpStartDate = wpStartDate;
	}
	public Long getWpEndDate() {
		return wpEndDate;
	}
	public void setWpEndDate(Long wpEndDate) {
		this.wpEndDate = wpEndDate;
	}
	public Long getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(Long lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}


}
