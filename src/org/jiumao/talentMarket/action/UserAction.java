package org.jiumao.talentMarket.action;


import org.jiumao.login.LoginProcess;
import org.jiumao.login.People;
import org.jiumao.talentMarket.domain.User;
import org.jiumao.talentMarket.service.UserService;

import base.BaseAction;
import spring.SpringSingletonFactory;
import controller.ActionSupport;

/**
 * 用户相关的请求
 * @author Administrator
 *			返回json字符串
 */
public class UserAction extends BaseAction<User> implements LoginProcess<User>{
	
	
	UserService userService = (UserService) baseService;
	
	public UserAction() {
		super();
	}
	/**
	 * 用户登录
	 */
	public void login(){
		User u = super.getBean();
		System.out.println(u.getUserName());
		User s = doLogin(u);
		System.err.println(s);
		String jsonString = super.bean2JsonString(s);
		responseJsonResult(jsonString);
	}
	
	
	@Override
	public User doLogin(People people) {
		System.out.println(people.getUserName());
		return userService.login(people.getUserName(), people.getPassword());
	}
	
	
}
