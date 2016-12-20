package org.jiumao.talentMarket.dao;

import java.util.List;

import org.jiumao.talentMarket.domain.Message;

import base.BaseDao;

public interface MessageDao extends BaseDao<Message> {

	List<Integer> findUserIdsById(Integer id);

}
