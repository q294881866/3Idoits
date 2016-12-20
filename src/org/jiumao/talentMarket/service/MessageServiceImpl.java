package org.jiumao.talentMarket.service;

import java.util.List;

import org.jiumao.talentMarket.dao.MessageDao;
import org.jiumao.talentMarket.domain.Message;

import base.BaseServiceImpl;

public class MessageServiceImpl extends BaseServiceImpl<Message> implements MessageService{
	

	MessageDao dao =(MessageDao)baseDao;
	@Override
	public void sendMessage(String type, Integer id) {
		if ("个人".equals(type)) {
			//查找相应的用户
			List<Integer> ids = dao.findUserIdsById(id);
			
			
			//调用用户的更新方法通知收到消息
			
			
			
			
			//把消息推送给相应的用户
			
			
		}
		
	}
      
}
