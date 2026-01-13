package com.nokia.ace.connector.sonar.constants;

public interface SonarConstants {

	public static final String CREATE_PROJECT = "createProject";
	public static final String CREATE_PROFILE = "createProfile";
	public static final String ASSIGN_ROLES = "assignUserRoles";
	public static final String ADD_PROJECT_IN_PORTFOLIO = "addProjectInPortfolio";
	
	public static final String NSWPS = "NSWPS_";

	final Object PROJECT_NAME = "projectName";

	public static final String CONTENT_TYPE = "Content-Type";
	public static final String ACCEPT = "Accept";
	public static final String CHARSET = "charset";
	public static final String CONTENT_LENGTH = "Content-Length";
	public static final String ACCESS_TOKEN = "access_token";
	public static final String APPLICATION_JSON = "application/json";
	public static final String HTTP_CONTENT_TYPE = "application/x-www-form-urlencoded";
	public static final String X_WWW_FROM_URLENCODED = "application/x-www-form-urlencoded";
	public static final String BEARER = "Bearer";
	public static final String AUTHORIZATION = "Authorization";
	public static final String X_VAULT_TOKEN = "X-Vault-Token";
	public static final String UTF_8 = "UTF-8";
	public static final String VAULT = "vault";
	public static final String SUCCESS = "SUCCESS";
	public static final String FAILURE = "FAILURE";
	public static final String CREATION = "PROJECT_CREATION";
	public static final String TOOL_NAME = "sonar";
	
	// API
	public static final String CREATE_SONAR_QUBE_PROJECT_API = "/api/projects/create?";
	public static final String SONAR_QUBE_ADD_USER_API = "/api/permissions/add_user?";
	public static final String SONAR_QUBE_ADD_PORTFOLIOS = "/api/views/add_project?";
	public static final String SONAR_QUBE_REMOVE_USER_API = "/api/permissions/remove_user?";
	public static final String SONAR_TOOL_KEY = "/dashboard?id=";
	
	//Quality profile URL
	public static final String SONAR_QUALITY_PROFILE_ADD_PROJECT_URL = "/api/qualityprofiles/add_project?";
	
	//Quality profile language and profile info
	public static final String JAVA_LANGUAGE = "java";
	public static final String PYTHON_LANGUAGE = "py";
	public static final String JAVA_QUALITY_PROFILE = "CSF";
	public static final String PYTHON_QUALITY_PROFILE = "CSF%20with%20pylint";
	public static final String SONAR_MAINTAINER_USER = "Maintainer";
	public static final String SONAR_DEVELOPERS_USER = "Developers";
	
}
