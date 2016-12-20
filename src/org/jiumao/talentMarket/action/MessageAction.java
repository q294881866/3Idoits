package org.jiumao.talentMarket.action;

import org.jiumao.talentMarket.domain.Message;
import org.jiumao.talentMarket.service.MessageService;
import org.jiumao.talentMarket.service.UserService;

import base.BaseAction;
import controller.ActionSupport;

public class MessageAction extends BaseAction<Message>{
	

	MessageService service = (MessageService) baseService;
	
	public void add(){
		Message message=super.getBean();
		
		//1.保存发布的消息
		
		//2.如果课程或者消息添加成功
		if (true) {
			
			//返回消息给用户
			
		}
	}
	
	

}
