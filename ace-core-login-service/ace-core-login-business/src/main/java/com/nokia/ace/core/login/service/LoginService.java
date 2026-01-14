package com.nokia.ace.core.login.service;

import java.util.List;

import com.nokia.ace.core.login.data.DomainMasterDto;
import com.nokia.ace.core.login.data.model.maria.DomainMaster;

public interface LoginService {

	List<DomainMasterDto> getUserDomain(String userName, String email);

}
