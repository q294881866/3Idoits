package test;

import javax.annotation.Resource;

import org.jiumao.talentMarket.service.UserServiceImpl;

public class Test4Annocation {

	@Resource
	UserServiceImpl userService;
	
	public static void main(String[] args) {
//		new Test4Annocation().userService.list();
	}
}
