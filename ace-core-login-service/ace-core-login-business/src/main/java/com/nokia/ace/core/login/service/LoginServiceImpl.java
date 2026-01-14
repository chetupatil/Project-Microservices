package com.nokia.ace.core.login.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokia.ace.core.login.data.DomainMasterDto;
import com.nokia.ace.core.login.data.dao.maria.UserDomainDetailDao;
import com.nokia.ace.core.login.data.model.maria.DomainMaster;

@Service
class LoginServiceImpl implements LoginService {

	@Autowired
	private UserDomainDetailDao userDomainDetailDao;

	@Override
	public List<DomainMasterDto> getUserDomain(String userName, String email) {

		
		List<DomainMaster> fetchUserDomain = userDomainDetailDao.fetchBlueprintUserDomain(userName, email);
  
		List<DomainMasterDto> DomainMasterDtoList = new ArrayList<DomainMasterDto>();
		
		for( DomainMaster userDomain : fetchUserDomain )
		{
			
			
			DomainMasterDto domainMasterDto = new DomainMasterDto();
			domainMasterDto.setDisplayName(userDomain.getDisplayName());
			domainMasterDto.setDomainName(userDomain.getDomainName());
			
			DomainMasterDtoList.add(domainMasterDto);

			
		}
		
		
		return DomainMasterDtoList;
		

	}

}
