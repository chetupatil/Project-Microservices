package com.nokia.ace.connector.sonar.dto;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nokia.ace.core.commons.model.UserRoles;
import com.nokia.ace.core.commons.roles.UserRolesWithStatus;

public class RoleMappingUserData {
	
	List<UserRolesWithStatus> userRolesWithStatus;
	List<UserRoles> failedRoles;
	Map<String, List<String>> usersToAdd;

	public Map<String, List<String>> getUsersToAdd() {
		return usersToAdd;
	}

	public void setUsersToAdd(Map<String, List<String>> usersToAdd) {
		this.usersToAdd = usersToAdd;
	}

	public List<UserRoles> getFailedRoles() {
		return failedRoles;
	}

	public void setFailedRoles(List<UserRoles> failedRoles) {
		this.failedRoles = failedRoles;
	}

	public List<UserRolesWithStatus> getUserRolesWithStatus() {
		return userRolesWithStatus;
	}

	public void setUserRolesWithStatus(List<UserRolesWithStatus> userRolesWithStatus) {
		this.userRolesWithStatus = userRolesWithStatus;
	}

	Boolean getAllUsernameStatus;

	public Boolean getGetAllUsernameStatus() {
		return getAllUsernameStatus;
	}

	public void setGetAllUsernameStatus(Boolean getAllUsernameStatus) {
		this.getAllUsernameStatus = getAllUsernameStatus;
	}

	Set<String> userList;

	public Set<String> getUserList() {
		return userList;
	}

	public void setUserList(Set<String> userList) {
		this.userList = userList;
	}
}
