package com.nokia.ace.connector.sonar.dto;

public class UserTextPojo {

	public String getProjectPath() {
		return projectPath;
	}
	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}
	public String getUsrTxtFilePath() {
		return usrTxtFilePath;
	}
	public void setUsrTxtFilePath(String usrTxtFilePath) {
		this.usrTxtFilePath = usrTxtFilePath;
	}
	private Boolean fileExists;
	public Boolean getFileExists() {
		return fileExists;
	}
	public void setFileExists(Boolean fileExists) {
		this.fileExists = fileExists;
	}
	/**
	 * @return the rootJenkinsFolder
	 */
	public String getRootJenkinsFolder() {
		return rootJenkinsFolder;
	}
	/**
	 * @param rootJenkinsFolder the rootJenkinsFolder to set
	 */
	public void setRootJenkinsFolder(String rootJenkinsFolder) {
		this.rootJenkinsFolder = rootJenkinsFolder;
	}
	private String projectPath;
	private String usrTxtFilePath;
	private String rootJenkinsFolder;
	
}
