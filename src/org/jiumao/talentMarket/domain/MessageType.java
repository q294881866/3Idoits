package org.jiumao.talentMarket.domain;

import base.BaseBean;

public class MessageType extends BaseBean{
	private Integer id;
	private String type;
	private Integer peopleId;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getPeopleId() {
		return peopleId;
	}
	public void setPeopleId(Integer peopleId) {
		this.peopleId = peopleId;
	}

}
