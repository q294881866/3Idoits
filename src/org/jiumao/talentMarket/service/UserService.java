package org.jiumao.talentMarket.service;

import org.jiumao.talentMarket.domain.User;

import base.BaseService;

public interface UserService extends BaseService<User>{

	/**
	 * login
	 * 
	 * @param userName
	 * @param password
	 * @return 
	 */
	User login(String userName, String password);
	

}
