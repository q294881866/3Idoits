package org.jiumao.login;

import java.util.HashMap;
import java.util.Map;


/**
 * 登录通用功能处理
 * @author Administrator
 *
 */
public class LoginTerms {
	
	//添加报名功能到文本聊天目录中
	static{
			conditions = new HashMap<String, Class>();
			choices = new StringBuilder();
	}
	private static Map<String, Class> conditions ;
	public static StringBuilder choices ;
	//读写锁
//	private static ReadWriteLock lock = new ReentrantReadWriteLock();
	
	/**
	 * 如果需要设置新的处理文本条件
	 * 那么以前的处理条件将失效
	 * 设置处理文本的各种规则
	 * 如：聊天，聊天关键字处理程序
	 * @param conditions
	 */
	public static void setConditions(Map<String,Class> condition) {
//		lock.writeLock().lock();
		conditions = condition;
//		lock.writeLock().unlock();
	}
	 
	 /**
	  * 添加新的处理条件
	  * @param key 处理的关键字
	  * @param condition 处理的条件
	  */
	public static void addCondition(String key,Class condition) {
//		lock.writeLock().lock();
		 conditions.put(key, condition);
		 choices.append(key+"　");//全角空格
//		 lock.writeLock().unlock();
	}
	
	/**
	 * 用户选择系统的功能
	 * 如用户选择用户登录、管理员登录等功能
	 */
	public static LoginProcess doChoice(String content){
//		lock.readLock().lock();
		Class clazz = conditions.get(content);
		System.out.println("clazz== "+clazz+"|| choices=="+choices);
//		lock.readLock().unlock();
		try {
			//返回一个
			if (null!=clazz) {
				return (LoginProcess) clazz.newInstance();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 	
		return null;
	}
	
	
}

