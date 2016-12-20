package org.jiumao.login;

public interface LoginProcess<T extends People> {

	/**
	 * 接收用户登录请求：
	 * 字段。账号密码，还有用户类型
	 * @return 
	 * 		people的子类
	 */
	T  doLogin (People people);
	
}
