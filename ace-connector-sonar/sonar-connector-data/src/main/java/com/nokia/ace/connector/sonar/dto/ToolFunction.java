package com.nokia.ace.connector.sonar.dto;

import org.springframework.stereotype.Component;


public class ToolFunction {
private String toolFunctionName;
private String status;

/**
 * @return the status
 */
public String getStatus() {
	return status;
}
/**
 * @param status the status to set
 */
public void setStatus(String status) {
	this.status = status;
}
public String getToolFunctionName() {
	return toolFunctionName;
}
public void setToolFunctionName(String toolFunctionName) {
	this.toolFunctionName = toolFunctionName;
}
}
