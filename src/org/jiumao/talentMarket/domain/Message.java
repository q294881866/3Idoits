package org.jiumao.talentMarket.domain;

import base.BaseBean;

public class Message extends BaseBean{
	private Integer id;
	private String type;
	private String content;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	
}
