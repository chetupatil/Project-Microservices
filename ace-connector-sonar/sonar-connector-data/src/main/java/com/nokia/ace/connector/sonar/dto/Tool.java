package com.nokia.ace.connector.sonar.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class Tool {
 String projectName;
String  toolName;
String uri;
public String getProjectName() {
	return projectName;
}
public void setProjectName(String projectName) {
	this.projectName = projectName;
}
public String getToolName() {
	return toolName;
}
public void setToolName(String toolName) {
	this.toolName = toolName;
}
public String getUri() {
	return uri;
}
public void setUri(String uri) {
	this.uri = uri;
}

}
