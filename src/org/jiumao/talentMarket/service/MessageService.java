package org.jiumao.talentMarket.service;

import org.jiumao.talentMarket.domain.Message;

import base.BaseService;

public interface MessageService extends BaseService<Message> {

	/**
	 * 发送推送消息给用户
	 * @param type
	 * 		消息类型
	 * @param id
	 * 		发送消息者id，
	 */
	void sendMessage(String type,Integer id);
}
