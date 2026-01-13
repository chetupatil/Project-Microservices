package com.nokia.ace.connector.sonar.dto;

import java.util.ArrayList;

public class AddTransaction {
 private String toolName;
 private String transStatus;
 private ArrayList<Object> toolFunctionList;



public String getTransStatus() {
	return transStatus;
}
public void setTransStatus(String transStatus) {
	this.transStatus = transStatus;
}
public ArrayList<Object> getToolFunctionList() {
	return toolFunctionList;
}
public void setToolFunctionList(ArrayList<Object> toolFunctionList) {
	this.toolFunctionList = toolFunctionList;
}
public String getToolProjectUrl() {
	return toolProjectUrl;
}
public void setToolProjectUrl(String toolProjectUrl) {
	this.toolProjectUrl = toolProjectUrl;
}
public String getToolProjectKey() {
	return toolProjectKey;
}
public void setToolProjectKey(String toolProjectKey) {
	this.toolProjectKey = toolProjectKey;
}
public String getToolProjectName() {
	return toolProjectName;
}
public void setToolProjectName(String toolProjectName) {
	this.toolProjectName = toolProjectName;
}
public Integer getProjectToolMappingId() {
	return projectToolMappingId;
}
public void setProjectToolMappingId(Integer integer) {
	this.projectToolMappingId = integer;
}
public String getTransTime() {
	return transTime;
}
public void setTransTime(String transTime) {
	this.transTime = transTime;
}
private String toolProjectUrl;
private String toolProjectKey;
private String toolProjectName;
private Integer projectToolMappingId;
private String createdBy;

private String transTime;
/**
 * @return the toolName
 */
public String getToolName() {
	return toolName;
}
/**
 * @param toolName the toolName to set
 */
public void setToolName(String toolName) {
	this.toolName = toolName;
}
public String getCreatedBy() {
	return createdBy;
}
public void setCreatedBy(String createdBy) {
	this.createdBy = createdBy;
}

 

}
