package com.nokia.ace.connector.sonar.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nokia.ace.connector.sonar.ACEServiceSonar;
import com.nokia.ace.connector.sonar.constants.SonarConstants;
import com.nokia.ace.connector.sonar.model.Sonar;
import com.nokia.ace.connector.sonar.model.SonarConnectorInputData;
import com.nokia.ace.connectors.common.constants.ConnectorConstants;
import com.nokia.ace.connectors.common.environmentVariables.EnvironmentVariables;
import com.nokia.ace.connectors.common.httpClient.AceConnectorHttpClient;
import com.nokia.ace.connectors.common.input.ConnectorInputData;
import com.nokia.ace.connectors.common.keycloak.KeycloakServiceImpl;
import com.nokia.ace.connectors.common.model.httpClient.ConnectionRequest;
import com.nokia.ace.connectors.common.model.toolDetails.ToolResponse;
import com.nokia.ace.connectors.common.model.transaction.ToolFunction;
import com.nokia.ace.connectors.common.output.ConnectorOutputData;
import com.nokia.ace.connectors.common.user.roles.UserRoleDefaultImpl;
import com.nokia.ace.connectors.common.util.CommonUtilities;
import com.nokia.ace.core.commons.model.BlueprintDto;
import com.nokia.ace.core.commons.model.UserRoles;
import com.nokia.ace.core.commons.model.sonar.SonarQube;
import com.nokia.ace.core.commons.roles.RoleMappingStatus;
import com.nokia.ace.core.commons.roles.User;
import com.nokia.ace.core.commons.roles.UserRoleStatus;
import com.nokia.ace.core.commons.roles.UserRolesWithStatus;
import com.nokia.ace.core.commons.utils.EmailValidator;

@Component
public class SonarUserRoleMappingServiceImpl extends UserRoleDefaultImpl {

	@Autowired
	private KeycloakServiceImpl keycloakServiceImpl;
	@Autowired
	private CommonUtilities commonUtilities;
	@Autowired
	AceConnectorHttpClient aceConnectorHttpClient;
	@Autowired
	private ACEServiceSonar aceService;

	private Logger LOGGER = LoggerFactory.getLogger(SonarUserRoleMappingServiceImpl.class);

	/**
	 * Method: Map user role to sonar project.
	 * 
	 * @param: connectorInput
	 * @param: envVariables
	 * @param: blueprintDto
	 * @param: toolResponse
	 * 
	 * @author chetana
	 */
	@Override
	public ConnectorOutputData mapUserRoles(BlueprintDto blueprintDto, ConnectorInputData connectorInputData,
			EnvironmentVariables envVariables, ToolResponse toolResponse) throws Exception {
		Sonar sonarTransaction = new Sonar();
		String projectKey = envVariables.getSonarQubeProjectPattern() + StringUtils.EMPTY + ((SonarConnectorInputData) connectorInputData).getProjectName();


		ConnectorOutputData connectorOutputData = callUserRoleMapping(blueprintDto,connectorInputData,envVariables,toolResponse,projectKey);

		List<ToolFunction> statusList = new ArrayList<>();
		ObjectMapper obj = new ObjectMapper(); 
		String defects = null;
		try {
			defects = obj.writeValueAsString(connectorOutputData.getBlueprint().getSonar().getRoles());
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
//		boolean isAssignRoleValid = false;
//		if(connectorOutputData.getUserRoleMappingStatus().equalsIgnoreCase("SUCCESS")) {
//			isAssignRoleValid = true;
//		}
		statusList.add(commonUtilities.populateStatusList(SonarConstants.ASSIGN_ROLES, connectorOutputData.getUserRoleMappingStatus(),defects));
		sonarTransaction.setStatusList(statusList);
		sonarTransaction.setStatus(connectorOutputData.getUserRoleMappingStatus());
		sonarTransaction.setProjectKey(projectKey);
		aceService.addTransaction(connectorInputData, sonarTransaction, toolResponse, envVariables);
		return connectorOutputData;
	}

	/**
	 *  This method used to add member and revoke member from sonar.
	 * 
	 * @param: connectorInput
	 * @param: envVariables
	 * @param: blueprintDto
	 * @param: toolResponse
	 * @param: projectKey
	 * 
	 * @author chetana
	 */
	public ConnectorOutputData callUserRoleMapping(BlueprintDto blueprintDto, ConnectorInputData connectorInputData,
			EnvironmentVariables envVariables, ToolResponse toolResponse, String projectKey) throws Exception {	
		List<UserRoles> failedRoles = new ArrayList();
		List<UserRolesWithStatus> userRolesWithStatusList = new ArrayList();
		Boolean assignRoleStatus = false;
		Boolean deleteMemberStatus = false;
		String token = null;
		try {  
			SonarQube sonar = blueprintDto.getSonar();
			List<UserRoles> userRole = sonar.getRoles();

			token = keycloakServiceImpl.getToken(envVariables, connectorInputData.getProjectName());

			Map<String, List<String>> userRolesToDelete = generateUserRolesToDelete(userRole, envVariables,connectorInputData,token);
			List<UserRoles> newUserRolesToAdd = generateUserRolesToAdd(userRole, envVariables,connectorInputData,token);

			Boolean isAddUser = isNewUsersRoleValid(newUserRolesToAdd);
			Boolean isDeleteUser = isDeleteUserValid(userRolesToDelete);
			// add operation
			if(isAddUser) {
				LOGGER.info("Add or Edit the user permission from sonar");
				for(UserRoles uRole:newUserRolesToAdd) {
					UserRolesWithStatus userRolesWithStatus = new UserRolesWithStatus();
					List<String> failedUserAssignment = new ArrayList();

					String roleName = uRole.getRoleName();
					userRolesWithStatus.setRoleName(roleName);

					assignRoleStatus = assignRoleToUsers(uRole.getUsers(), failedUserAssignment, toolResponse,
							envVariables, connectorInputData, userRolesWithStatus,projectKey,token,roleName);

					setDeletedRolesStatus(userRolesWithStatus, userRolesToDelete, assignRoleStatus, roleName);
					userRolesWithStatusList.add(userRolesWithStatus);

					UserRoles failedRole = new UserRoles();
					failedRole.setRoleName(roleName);
					failedRole.setUsers(failedUserAssignment);
					failedRoles.add(failedRole);	
				}
			}

			// edit operation
			if(isDeleteUser && isAddUser) {
				userRolesToDelete = editSonarUserMapping(userRolesToDelete,newUserRolesToAdd,connectorInputData,envVariables,token,
						userRolesWithStatusList,failedRoles);
			}

			// delete operation
			if(isDeleteUser) {
				for(UserRoles role: userRole) {
					if(!CollectionUtils.isEmpty(userRolesToDelete.get(role.getRoleName()))) {
						LOGGER.info("Delete the user permission from sonar");
						UserRolesWithStatus userRolesWithStatus = new UserRolesWithStatus();
						List<String> failedUserAssignment = new ArrayList();

						String roleName = role.getRoleName();
						userRolesWithStatus.setRoleName(roleName);


						deleteMemberStatus = deleteUserRole(userRolesToDelete.get(role.getRoleName()), failedUserAssignment, toolResponse,
								envVariables, connectorInputData, userRolesWithStatus,projectKey,roleName,token);

					//	setDeletedRolesStatus(userRolesWithStatus, userRolesToDelete, assignRoleStatus, roleName);
						userRolesWithStatusList.add(userRolesWithStatus);

						UserRoles failedRole = new UserRoles();
						failedRole.setRoleName(roleName);
						failedRole.setUsers(failedUserAssignment);
						failedRoles.add(failedRole);	
					}

				}
			}
		}catch(Exception e) {
			LOGGER.error("Unable to add or delete the permission to user: Exception "+e.getMessage());
			assignRoleStatus = false;
			deleteMemberStatus = false;
		}

		storeRolesInAceDb(connectorInputData, envVariables, token, userRolesWithStatusList);
		return getConnectorOutputData(failedRoles,assignRoleStatus,deleteMemberStatus);

	}
	
	private Map<String, List<String>> editSonarUserMapping(Map<String, List<String>> userRolesToDelete,List<UserRoles> newUserRolesToAdd,ConnectorInputData connectorInputData,
			EnvironmentVariables envVariables,String token,List<UserRolesWithStatus> userRolesWithStatusList,List<UserRoles> failedRoles) {
		UserRolesWithStatus userRolesWithStatus = new UserRolesWithStatus();
		List<String> failedUserAssignment = new ArrayList();
		
		Map<String, List<String>> userRolesToDeleteLocal = userRolesToDelete;

		List<String> uString = userRolesToDeleteLocal.get(SonarConstants.SONAR_DEVELOPERS_USER);

		String roleName = SonarConstants.SONAR_DEVELOPERS_USER;
		userRolesWithStatus.setRoleName(roleName);

		for(UserRoles uRole: newUserRolesToAdd) {
			if(uRole.getRoleName().equalsIgnoreCase(SonarConstants.SONAR_MAINTAINER_USER)) {
				List<User> userList = new ArrayList();
				for(String uR: uRole.getUsers()) {
					if(uString.contains(uR)) {

						User user = new User();
						String userEmail =uR;
						String userId;

						if(isEmailCheck(userEmail)) {
							user.setUserMailId(userEmail);
							userId = keycloakServiceImpl.getUserName(token, envVariables, userEmail,connectorInputData.getProjectName());
						} else { 
							userId= userEmail;
							user.setUserMailId(keycloakServiceImpl.getUserEmail(token, envVariables, userId,connectorInputData.getProjectName()));
						}

						if (userId.equalsIgnoreCase(ConnectorConstants.FAILURE) || StringUtils.isEmpty(userId)) {
							//assignRoleStatus = false;
							failedUserAssignment.add(userEmail);
							user.setUserRoleStatus(UserRoleStatus.FAILED);
						} 
						user.setStatus(RoleMappingStatus.INACTIVE);
						userList.add(user);
						uString.remove(uR);
					}
				}
				userRolesWithStatus.setUsers(userList);
			}

		}
		setDeletedRolesStatus(userRolesWithStatus, userRolesToDelete, true, roleName);
		userRolesWithStatusList.add(userRolesWithStatus);
		UserRoles failedRole = new UserRoles();
		failedRole.setRoleName(roleName);
		failedRole.setUsers(failedUserAssignment);
		failedRoles.add(failedRole);	

		userRolesToDeleteLocal.put(SonarConstants.SONAR_DEVELOPERS_USER, uString);
		
		userRolesToDelete = userRolesToDeleteLocal;
		
		return userRolesToDelete;
	}



	/**
	 * Method: Assign role to users
	 * 
	 * @param: users
	 * @param: failedUserAssignment
	 * @param: connectorInputData
	 * @param: envVariables
	 * @param: userRolesWithStatus
	 * @param: toolResponse
	 * @param: projectKey
	 * @param: token
	 * 
	 * @author chetana
	 * @param roleName 
	 */
	private Boolean assignRoleToUsers(List<String> users, List<String> failedUserAssignment,
			ToolResponse toolResponse, EnvironmentVariables envVariables,
			ConnectorInputData connectorInputData, UserRolesWithStatus userRolesWithStatus,String projectKey, String token, String roleName) {

		Boolean assignRoleStatus = true;
		List<String> userIdList = new ArrayList<>();
		List<User> userList = new ArrayList();
		ListIterator iterator2 = users.listIterator();
		while (iterator2.hasNext()) {
			User user = new User();
			String userEmail = iterator2.next().toString();
			String userId;

			if(isEmailCheck(userEmail)) {
				user.setUserMailId(userEmail);
				userId = keycloakServiceImpl.getUserName(token, envVariables, userEmail,connectorInputData.getProjectName());
			} else { 
				userId= userEmail;
				user.setUserMailId(keycloakServiceImpl.getUserEmail(token, envVariables, userId,connectorInputData.getProjectName()));
			}

			if (userId.equalsIgnoreCase(ConnectorConstants.FAILURE) || StringUtils.isEmpty(userId)) {
				assignRoleStatus = false;
				failedUserAssignment.add(userEmail);
				user.setUserRoleStatus(UserRoleStatus.FAILED);
			} else {
				userIdList.add(userId);
			}
			user.setStatus(RoleMappingStatus.ACTIVE);
			userList.add(user);
		}
		if (!CollectionUtils.isEmpty(userIdList)) {
			assignRoleStatus = sonarUserMapping(connectorInputData,userIdList,projectKey,toolResponse,roleName) && assignRoleStatus;
		}
		setUserRoleStatus(userList, assignRoleStatus);
		userRolesWithStatus.setUsers(userList);
		return assignRoleStatus;
	}

	/**
	 * Method:  delete users from sonar project
	 * 
	 * @param: users
	 * @param: failedUserAssignment
	 * @param: connectorInputData
	 * @param: envVariables
	 * @param: userRolesWithStatus
	 * @param: toolResponse
	 * @param: projectKey
	 * @param: token
	 * @param: roleName
	 * 
	 * @author chetana
	 */
	private Boolean deleteUserRole(List<String> users, List<String> failedUserAssignment, ToolResponse toolResponse,
			EnvironmentVariables envVariables, ConnectorInputData connectorInputData, UserRolesWithStatus userRolesWithStatus, String projectKey, String roleName, String token) {
		Boolean assignRoleStatus = true;
		List<String> userIdList = new ArrayList<>();
		List<User> userList = new ArrayList();
		ListIterator iterator2 = users.listIterator();
		while (iterator2.hasNext()) {
			User user = new User();
			String userEmail = iterator2.next().toString();
			String userId;

			if(isEmailCheck(userEmail)) {
				user.setUserMailId(userEmail);
				userId = keycloakServiceImpl.getUserName(token, envVariables, userEmail,connectorInputData.getProjectName());
			} else { 
				userId= userEmail;
				user.setUserMailId(keycloakServiceImpl.getUserEmail(token, envVariables, userId,connectorInputData.getProjectName()));
			}

			if (userId.equalsIgnoreCase(ConnectorConstants.FAILURE) || StringUtils.isEmpty(userId)) {
				assignRoleStatus = false;
				failedUserAssignment.add(userEmail);
				user.setUserRoleStatus(UserRoleStatus.FAILED);
			} else {
				userIdList.add(userId);
			}
			user.setStatus(RoleMappingStatus.INACTIVE);
			userList.add(user);
		}
		if (!CollectionUtils.isEmpty(userIdList)) {
			assignRoleStatus = deleteUsers(connectorInputData,envVariables,projectKey,toolResponse,userIdList,roleName,token) && assignRoleStatus;
		}
		setUserRoleStatus(userList, assignRoleStatus);
		userRolesWithStatus.setUsers(userList);
		return assignRoleStatus;
	}

	public Boolean sonarUserMapping(ConnectorInputData connectorInput,List<String> userIdList, String projectKey, ToolResponse toolResponse, String roleName) {
		Boolean isSonarUserValid = false;
		int countUserSuccessResult = 0;
		try {
			String[] logMsgParams = { connectorInput.getProjectName(), "Sonarqube User Mapping." };

			Map<String, Map<String, String>> sonarUserPermissionMap =  connectorInput.getSonarUserPermissionList();

			for(String sonarUserEntry : userIdList) {

				if(!roleName.equalsIgnoreCase(SonarConstants.SONAR_MAINTAINER_USER) && connectorInput.getCreatedBy().equalsIgnoreCase(sonarUserEntry)) {
					isSonarUserValid = false;
					LOGGER.info("Unable to demote the access for user :  "+sonarUserEntry);
					break;
				}

				if(sonarUserPermissionMap.containsKey(sonarUserEntry)) {  

					Map<String, String> sonarMap = sonarUserPermissionMap.get(sonarUserEntry);

					for(Entry<String, String> sonarIter :   sonarMap.entrySet()) {

						LOGGER.info("Sonar User Permission Addition with "+sonarUserEntry+" : "+sonarIter.getKey());

						StringBuilder apiStr = new StringBuilder();
						String url = toolResponse.getInstanceUrl() + SonarConstants.SONAR_QUBE_ADD_USER_API;
						apiStr.append(url);
						apiStr.append("permission=").append(sonarIter.getValue());
						apiStr.append("&projectKey=").append(projectKey);
						apiStr.append("&login=").append(sonarUserEntry);
						Map<String, String> credentials = new HashMap();
						credentials.put("userName", toolResponse.getUserName());
						credentials.put("password", toolResponse.getPassword());
						String postString = "{}";
						ConnectionRequest request = new ConnectionRequest();
						request = request.getConnection(apiStr.toString(), credentials, null, postString, SonarConstants.APPLICATION_JSON, null);
						isSonarUserValid = aceConnectorHttpClient.post(request, logMsgParams, null).containsValue(204);
						if(isSonarUserValid) {
							countUserSuccessResult++;
						}
						LOGGER.info("isSonarUserValid "+isSonarUserValid);	
					}
					LOGGER.info("countUserSuccessResult =  "+countUserSuccessResult);
					if(sonarMap.size()==countUserSuccessResult) {
						isSonarUserValid = true;
					}
				}
			}

		}catch(Exception ex) {
			LOGGER.error("unable to add sonarqube user mapping. error logs: " +ex.getMessage());
			return isSonarUserValid;
		}
		return isSonarUserValid;

	}

	public Boolean removeSonarUserMapping(ConnectorInputData connectorInput,EnvironmentVariables envVariables, ToolResponse toolResponse, String projectKey, List<String> list, Map<String, String> userAccessMap, String roleName) {
		Boolean isSonarUserValid = false;
		int countUserSuccessResult = 0;
		try {
			String[] logMsgParams = { connectorInput.getProjectName(), "Revoke Sonarqube User." };


			for(String user: list) {
				if(roleName.equalsIgnoreCase(SonarConstants.SONAR_MAINTAINER_USER) && user.equalsIgnoreCase(connectorInput.getCreatedBy())) {
					isSonarUserValid = false;
					LOGGER.info("Unable to demote the access for user :  "+user);
					break;
				}
				for(Entry<String, String> userAccess : userAccessMap.entrySet()) {
					LOGGER.info("Sonar User Permission Revoke with "+user+" : "+userAccess.getKey());
					StringBuilder apiStr = new StringBuilder();
					String url = toolResponse.getInstanceUrl() + SonarConstants.SONAR_QUBE_REMOVE_USER_API;
					apiStr.append(url);
					apiStr.append("permission=").append(userAccess.getValue());
					apiStr.append("&projectKey=").append(projectKey);
					apiStr.append("&login=").append(user);
					Map<String, String> credentials = new HashMap();
					credentials.put("userName", toolResponse.getUserName());
					credentials.put("password", toolResponse.getPassword());
					String postString = "{}";
					ConnectionRequest request = new ConnectionRequest();
					request = request.getConnection(apiStr.toString(), credentials, null, postString, SonarConstants.APPLICATION_JSON, null);
					isSonarUserValid = aceConnectorHttpClient.post(request, logMsgParams, null).containsValue(204);
					if(isSonarUserValid) {
						countUserSuccessResult++;
					}

					LOGGER.info("isSonarUserValid "+isSonarUserValid);
				}
				LOGGER.info("countUserSuccessResult =  "+countUserSuccessResult);
				if(userAccessMap.size() == countUserSuccessResult) {
					isSonarUserValid = true;
				}

			}

		}catch(Exception ex) {
			LOGGER.error("unable to remove sonarqube user. error logs: " +ex.getMessage());
			return isSonarUserValid;
		}
		return isSonarUserValid;

	}

	private Boolean deleteUsers(ConnectorInputData connectorInputData,
			EnvironmentVariables envVariables, String projectKey, ToolResponse toolResponse,List<String> userIdList, String roleName, String token) {
		//Boolean deleteMemberStatus = false;
		Map<String,String> userAccessMap = fetchUserRoles(connectorInputData,envVariables,token,roleName);
		Boolean deleteMemberStatus = removeSonarUserMapping(connectorInputData,envVariables,toolResponse,projectKey,userIdList,userAccessMap,roleName);
		return deleteMemberStatus;
	}


	private Map<String, List<String>> generateUserRolesToDelete(List<UserRoles> roles,
			EnvironmentVariables envVariables, ConnectorInputData connectorInputData, String token) {
		BlueprintDto rolesInDb = fetchUserRoleFromDb(connectorInputData, envVariables, token);
		if (rolesInDb.getSonar() != null) {
			List<UserRoles> userRolesInDb = new ArrayList();
			userRolesInDb = rolesInDb.getSonar().getRoles();
			return getUserRolesToDelete(userRolesInDb, roles, "sonar");
		} else {
			return null;
		}
	}

	private List<UserRoles> generateUserRolesToAdd(List<UserRoles> roles,EnvironmentVariables envVariables, ConnectorInputData connectorInputData, String token) {
		BlueprintDto rolesInDb = fetchUserRoleFromDb(connectorInputData, envVariables, token);
		if (rolesInDb.getSonar() != null) {
			List<UserRoles> userRolesInDb = new ArrayList();
			userRolesInDb = rolesInDb.getSonar().getRoles();
			return getUserRolesToAdd(userRolesInDb, roles, "sonar");
		} else {
			return null;
		}
	}
	private ConnectorOutputData getConnectorOutputData(List<UserRoles> failedRoles, Boolean assignRoleStatus, Boolean deleteMemberStatus) {
		ConnectorOutputData connectorOutputData = new ConnectorOutputData();
		try {
			if (assignRoleStatus) {
				connectorOutputData.setUserRoleMappingStatus(SonarConstants.SUCCESS);
			} else if(deleteMemberStatus){
				connectorOutputData.setUserRoleMappingStatus(SonarConstants.SUCCESS);
			}else {	
				connectorOutputData.setUserRoleMappingStatus(SonarConstants.FAILURE);
			}
			SonarQube sonarWithFailedRoles = new SonarQube();
			sonarWithFailedRoles.setRoles(failedRoles);

			BlueprintDto outputWithRoles = new BlueprintDto();
			outputWithRoles.setSonar(sonarWithFailedRoles);
			connectorOutputData.setBlueprint(outputWithRoles);
		} catch (Exception e) {
			LOGGER.error("Error occured while generating connectorOutputData" + e.toString());
		}
		return connectorOutputData;
	}
	private boolean isEmailCheck(String email) {
		if (email == null) {
			return false;
		}
		return EmailValidator.validate(email);
	}

	private Boolean isNewUsersRoleValid(List<UserRoles> userRoles) {
		int roleCount = 0;
		if(!CollectionUtils.isEmpty(userRoles)) {
			for(UserRoles uRoles: userRoles) {
				if(!uRoles.getUsers().isEmpty()) {
					roleCount++;
				}
			}
		}
		return roleCount>0;
	}

	private Boolean isDeleteUserValid(Map<String,List<String>> deleteUsers) {
		int deleteCount = 0;
		Set<Entry<String, List<String>>> deleteUserEntry = deleteUsers.entrySet();
		for(Entry<String, List<String>> userEntry : deleteUserEntry) {
			if(!userEntry.getValue().isEmpty()) {
				deleteCount++;
			}
		}

		return deleteCount>0;
	}
	private void storeRolesInAceDb( ConnectorInputData connectorInputData,EnvironmentVariables envVariables,String token,List<UserRolesWithStatus> userRolesWithStatusList) {
		try {
			storeRolesInDb(connectorInputData, envVariables, token, ConnectorConstants.SONAR, userRolesWithStatusList);
		} catch (Exception e) {
			LOGGER.error("Unable to store the roles in ace db "+e.getMessage());
		}
	}
}
