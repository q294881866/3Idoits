package org.jiumao.talentMarket.service;

import org.jiumao.talentMarket.dao.UserDao;
import org.jiumao.talentMarket.domain.User;

import base.BaseServiceImpl;
import spring.SpringSingletonFactory;

public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

	public UserServiceImpl() {
		System.out.println("userService");
	}

	UserDao userDao = (UserDao) SpringSingletonFactory.getBean("userDao");

	@Override
	public User login(String userName, String password) {
		// TODO Auto-generated method stub
		return userDao.login(userName, password);
	}

}
