package com.nokia.ace.connector.sonar.dto;

import java.util.Map;

public class ConnectionRequest {

	String token;
	String connType;
	String connUrl;
	String contentType;
	String acceptType;
	String username;
	String password;
	String postData;
	String dataLength;
	String tokenType;
	String requestType;

	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getConnType() {
		return connType;
	}
	public void setConnType(String connType) {
		this.connType = connType;
	}
	public String getConnUrl() {
		return connUrl;
	}
	public void setConnUrl(String connUrl) {
		this.connUrl = connUrl;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getAcceptType() {
		return acceptType;
	}
	public void setAcceptType(String acceptType) {
		this.acceptType = acceptType;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPostData() {
		return postData;
	}
	public void setPostData(String postData) {
		this.postData = postData;
	}
	public String getDataLength() {
		return dataLength;
	}
	public void setDataLength(String dataLength) {
		this.dataLength = dataLength;
	}
	public String getTokenType() {
		return tokenType;
	}
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
	 public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	/**
     * Method to pass connection parameters
     * 
     * @param url
     * @param credentials
     * @param postDataLength
     * @param postData
     * @param contentType
     * 
     * @return
     */
    public ConnectionRequest getConnection(String url, Map<String,String> credentials, String postDataLength,String postData, String contentType, String token)
    {
   	 ConnectionRequest request= new ConnectionRequest();
   	 
   	 request.setAcceptType("application/json");
   	 if(contentType!=null) {
   		 request.setContentType(contentType);
   	 }
    
     request.setConnUrl(url);
     if(postDataLength != null)
     {
    	 request.setDataLength(postDataLength);
     }
     if(credentials != null) {
        request.setUsername(credentials.get("userName"));
       request.setPassword(credentials.get("password"));                                             
     }
     if(postData != null) {
     request.setPostData(postData); 
        }
     
     if(token!=null) {
    	 request.setToken(token);
    	
     }
     return request;
   
    }
 
//    /**
//     * Method to pass connection parameters
//     * 
//     * @param url
//     * @param token
//     * @param contentType
//     * @param tokenType
//     *
//     * @return
//     */
//    public ConnectionRequest getConnection(String url, String token, String contentType, String tokenType)
//    {
//   	 ConnectionRequest request= new ConnectionRequest();
//   	 request.setAcceptType("application/json");
//     request.setContentType(contentType);
//     request.setConnUrl(url);
//     request.setToken(token);
//     request.setTokenType(tokenType);
//     return request;
//   
//    }
    
  

}
