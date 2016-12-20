package org.jiumao.talentMarket.dao;

import org.jiumao.talentMarket.domain.User;

import base.BaseDao;

public interface UserDao extends BaseDao<User>{

	User login(String userName, String password);
	
}
