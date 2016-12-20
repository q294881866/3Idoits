package org.jiumao.talentMarket.dao;

import org.jiumao.talentMarket.domain.User;
import org.jiumao.talentMarket.domain.UserSql;

import base.BaseDaoImpl;


public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao {

	@Override
	public User login(String userName, String password) {
		try {
			return (User) mySqlWriterSessionFactory.getObject(
					UserSql.findUserByUserNameAndPassword, User.class,
					userName, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}




}
