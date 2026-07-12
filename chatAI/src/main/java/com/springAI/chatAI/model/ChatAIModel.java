package com.springAI.chatAI.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ai_Gen_collection")
public class ChatAIModel {
	
	@Id
	private String id;
	private String llm_model;
	private String mode_llm;
	private String message;
	private String roles;
	private Date created;
	private Date modified;
	private String created_by;
	private String modified_by;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLlm_model() {
		return llm_model;
	}
	public void setLlm_model(String llm_model) {
		this.llm_model = llm_model;
	}
	public String getMode_llm() {
		return mode_llm;
	}
	public void setMode_llm(String mode_llm) {
		this.mode_llm = mode_llm;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getRoles() {
		return roles;
	}
	public void setRoles(String roles) {
		this.roles = roles;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getModified() {
		return modified;
	}
	public void setModified(Date modified) {
		this.modified = modified;
	}
	public String getCreated_by() {
		return created_by;
	}
	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}
	public String getModified_by() {
		return modified_by;
	}
	public void setModified_by(String modified_by) {
		this.modified_by = modified_by;
	}
	
	

}
