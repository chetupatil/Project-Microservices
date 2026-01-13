package com.nokia.ace.utils.constants;

/**
 * @author Chetana
 *
 */

public interface PmatConstants {
	
	final String SHAREPOINT_URL = "https://login.microsoftonline.com/extSTS.srf";
	final String USERNAME = "${sharepoint_email}";
	final String PASSWORD = "${sharepoint_password}";
	final String W_SIGN_IN_SP = "/_forms/default.aspx?wa=wsignin1.0";
	final String SET_COOKIES = "Set-Cookie";
	final String WST_REQUESTED_SECURITY_TOKEN = "wst:RequestedSecurityToken";
	final String CONTEXT_INFO_SP = "/_api/contextinfo";
	final String CONTEXT_WEB_INFO = "GetContextWebInformation";
	final String FORM_DIGEST_VALUE = "FormDigestValue";
	final String SP_CONTENT_TYPE = "application/json;odata=verbose";
	final String SP_ACCEPT = "application/json;odata=verbose";
	final String APP_FORM_URL = "application/x-www-form-urlencoded";
	final String JIRA_SEARCH_ISSUE_URL = "/rest/api/2/search";
	final String CUSTOM_JIRA_FIELDS = "&fields=key,customfield_38071,customfield_38479,customfield_38035,customfield_37415,customfield_38267,customfield_37285,customfield_38219,customfield_38139,customfield_28093,customfield_10232,created,updated,aggregatetimeoriginalestimate,customfield_43430";
	
	final String JIRA_FILTER_PARA = "?jql=cf[38139]='Opportunity'%20%20AND%20'cf[38479]'%20~%20'";
	final String JIRA_WITHOUT_OPP_FILTER = "?jql='cf[38479]'%20~%20'";
	final String JIRA_FILTER_PARA_WP_01 = "?jql=cf[38139]='";
	final String JIRA_FILTER_PARA_WP_02 = "'%20%20AND%20'cf[38479]'%20~%20'";
	final String JIRA_FILTER_PARA_WP_03 = "%20AND%20'cf[10232]'%20~%20'";
	
	final String JIRA_SCRM_ID = "?jql='cf[38479]'%20~%20'";
	final String JIRA_SAF_PROJECT_ID = "'%20%20AND%20'cf[10232]'%20~%20'";
	final String ACE_PROJECT_ID = "?jql='cf[10232]'%20~%20'";
	final String CUSTOM_JIRA_ISSUE_FIELDS = "&fields=key,customfield_10232";
	final String SEARCH_CUSTOM_JIRA_ISSUE_FIELDS = "&fields=key,customfield_10232,issuetype";
	
	final String SAF_PROJECT_ID = "customfield_10232";
	final String GIC = "customfield_37415";
	final String OPPORTUNITY_NUMBER = "customfield_38479";
	final String PROSPECT_TYPE = "customfield_38139";
	final String SERVICE_OFFERING = "customfield_38035";
	final String SERVICE_SUB_TYPE = "customfield_28093";
	final String BUSSINESS_LINE = "customfield_43430";
	final String PROJECT_KEY  = "customfield_10232";
	final String UPDATED = "updated";
	final String CREATED = "created";
	final String AGGREGATE_ESTIMATION_TIME = "aggregatetimeoriginalestimate";
	final String FIELDS = "fields";
	final String dateFormate = "yyyy-MM-dd'T'HH:mm:ss.SSS";
	final String dateFormate1 = "yyyy-MM-dd HH:mm:ss.SSS";
	final String JiraDateFormate = "yyyy-MM-dd";
	final String dateFormateSlash = "MM/dd/yyyy HH:mm:ss";
	final String WORKPACKAGE = "customfield_38219";
	final String CSV_HEADER_PROJECT_TYPE = "Project Type";
	final String CSV_HEADER_SAF_UNIQUE_ID = "SAF Unique Id";
	final String CSV_HEADER_PPA_ID = "PPA ID";
	final String CSV_HEADER_WORKPACKAGE_NAME = "Workpackage Name";
	final String CSV_HEADER_SAF_PROJECT_ID = "SAF Project Id";
	final String CSV_HEADER_SERVICE_OFFERING = "ServiceOffering";
	final String CSV_HEADER_SERVICE_SUB_TYPE = "Service SubType";
	final String CSV_HEADER_AGGREGATE_EFFORTS = "Aggregate Effort (Days)";
	final String CSV_HEADER_OPPORTUNITY_NUMBER = "Opportunity Number";
	final String CSV_HEADER_GIC = "GIC";
	final String CSV_HEADER_LAST_MODIFIED = "Last Modified (Month/Date/Year HH:MM:SS)";
	final String CSV_HEADER_WP_START_DATE = "WP Start Date (Month/Date/Year HH:MM:SS)";
	final String CSV_HEADER_WP_END_DATE = "WP End Date (Month/Date/Year HH:MM:SS)";
	final String CSV_HEADER_BUSSINESS_LINE = "BusinessLine";
	final String CSV_HEADER_BUSSINESS_UNIT = "BusinessUnit";
	final String CSV_HEADER_IS_DELETED = "Is Deleted";
	final Object MODIFY_ISSUE = "/rest/api/2/issue/";
	final String APPLICATION_JSON = "application/json";
	final String ACE_MAILID = "nswps.ca_nswps_ace@nokia.com";
	
	final String EUR = "EUR";
	final String NAM = "NAM";
	final String APJ = "APJ";
	final String GCHN = "GCHN";
	final String MEA = "MEA";
	final String LAT = "LAT";
	final String IND = "IND";
	
	final String MARKET_EUR = "Market Europe";
	final String MARKET_NAM = "Market North America";
	final String MARKET_APJ = "Market Asia Pacific & Japan";
	final String MARKET_GCHN = "Market Greater China";
	final String MARKET_MEA = "Market Middle East and Africa";
	final String MARKET_LAT = "Market Latin America";
	
	final String PMAT_MARKET_EUR = "Region Europe";
	final String PMAT_MARKET_APJ = "Region Asia-Pacific";
	final String PMAT_MARKET_NAM = "Region North America";
	final String PMAT_MARKET_GCHN = "Region Greater China";
	final String PMAT_MARKET_MEA = "Region Middle East & Africa";
	final String PMAT_MARKET_LAT = "Region Latin America";
	final String PMAT_MARKET_IND = "Region India";
	
	final String SCOPE_ANALYTICS = "Analytics";
	final String SCOPE_DIGITAL_OPERATION = "Digital Operations";
	final String SCOPE_MEDIATION = "Mediation";
	final String SCOPE_CHARGING = "Charging";
	final String SCOPE_DEVICE_MANAGMENT = "Device Management";
	final String SCOPE_SECURITY = "Security";
	final String SCOPE_CC_DDI = "";
	final String SCOPE_S_S = "";
	
	final String SP_GET_FILE_BY_URL = "/sites/NSW_PMAT_TEST/_api/Web/GetFolderByServerRelativeUrl('ACEPMAT')/Files('ProspectPlanning.csv')/$value";
	final String SP_GET_FILE_BY_URL_YESTERDAY = "/sites/NSW_PMAT_TEST/_api/Web/GetFolderByServerRelativeUrl('ACEPMAT')/Files('SAF_DUMP.csv')/$value";
	final String SP_UPLOAD_FILE_URL = "/sites/NSW_PMAT_TEST/_api/web/GetFolderByServerRelativeUrl('/sites/NSW_PMAT_TEST/ACEPMAT')/Files/";
	
//	final String SP_GET_FOLDER_FILE_BY_URL = "/sites/NSW_PMAT_TEST/_api/Web/GetFolderByServerRelativeUrl('ACEPMAT/PPA_to_SAF')/Files('ProspectPlanning.csv')/$value";
//	final String SP_GET_FOLDER_FILE_BY_URL_YESTERDAY = "/sites/NSW_PMAT_TEST/_api/Web/GetFolderByServerRelativeUrl('ACEPMAT/SAF_to_PMAT')/Files('SAF_DUMP.csv')/$value";
//	final String SP_UPLOAD_FOLDER_FILE_URL = "/sites/NSW_PMAT_TEST/_api/web/GetFolderByServerRelativeUrl('/sites/NSW_PMAT_TEST/ACEPMAT/SAF_to_PMAT')/Files/";
	
	
	final String SP_GET_FOLDER_FILE_BY_URL_1 = "/_api/Web/GetFolderByServerRelativeUrl('";
    final String SP_GET_FOLDER_FILE_BY_URL_2 = "')/Files('";
    final String SP_GET_FOLDER_FILE_BY_URL_3 =  "')/$value";
   
   
   final String SP_GET_FOLDER_FILE_BY_URL_YESTERDAY_1 = "/_api/Web/GetFolderByServerRelativeUrl('";
   final String SP_GET_FOLDER_FILE_BY_URL_YESTERDAY_2 = "')/Files('";
   final String SP_GET_FOLDER_FILE_BY_URL_YESTERDAY_3 = "')/$value";
   
   final String SP_UPLOAD_FOLDER_FILE_URL_1 = "/_api/web/GetFolderByServerRelativeUrl('";
   final String SP_UPLOAD_FOLDER_FILE_URL_2 = "')/Files/";
	
	final String SP_DOWNLOAD_URL = "";
	final String SP_UPLOAD_URL = "";
	final String SP_GET_URL = "";
	
	final String FETCH_TOOL_DETAILS_URL = "/api/project/fetchToolDetailByTool";
	
	final String FETCH_INSERT_URL = "/api/project/ngseet/fetch/insertcustomer";
	
	final String OPPORTUNITY = "Opportunity";
	final String PROJECT = "Project";
	final String PROSPECT = "Prospect";
	final String STATUS_CLOSED = "Closed / Project Started";
	
	final String EMAIL_TEMPLATE_PPA = "email-template-ppa.html";
	
    final long MILLIS_IN_A_DAY = (1000 * 60 * 60 * 24) * 2;
    
    final String TOOL_SHAREPOINT = "sharepoint";
    final String TOOL_JIRA = "jira";
    final String SERVER_IP = "http://localhost:9000";
    final String PROJECT_NAME = "PPA_PROJECT";
    final String PROFILE_ENV = "prod";
    
    final String ACTUAL_START_DATE = "customfield_38071";
	final String ACTUAL_END_DATE = "customfield_37285";
	
	final String PPA_FULL_LOAD_FLAG_COMPLETE = "complete";
	final String PPA_FULL_LOAD_FLAG_PARTIAL = "partial";
	
}
