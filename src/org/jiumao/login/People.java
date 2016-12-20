package org.jiumao.login;

import java.math.BigInteger;

import base.BaseBean;

/**
 * 登录实体需要继承的类
 * @author Administrator
 *			登录必备的一些元素
 *				用户名、密码
 */
public abstract class People extends BaseBean{

	private BigInteger id;
	private String userName;
	private String password;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public BigInteger getId() {
		return id;
	}
	public void setId(BigInteger id) {
		this.id = id;
	}
}
