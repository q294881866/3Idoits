package org.jiumao.redis;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.SortingParams;

public class ShardJedisPool {

	private static int initCount = 1<<4;
	private static int maxCount = 1<<12;
	private static long MaxWait = 2000l;
	int currentCount = 0;
	
	private static ShardJedisPool instance = null;
	private  Vector properties = null;  
	//数据库连接池<>表示泛型JedisShardInfo表示只存JedisShardInfo及子类
	LinkedList<JedisShardInfo> connectionsPool = new LinkedList<JedisShardInfo>();
	

	private ShardJedisPool() {
		try {
			for (int i = 0; i < initCount; i++) {
				this.connectionsPool.addLast(ShardJedis.getShardConnection());
				this.currentCount++;
			}
		} catch (SQLException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	private static synchronized void syncInit() {  
        if (instance == null) {  
            instance = new ShardJedisPool();  
        }  
    }  
	
	public static ShardJedisPool getInstance() {  
        if (instance == null) {  
            syncInit();  
        }  
        return instance;  
    }  
	
	
	/**
	 * 
	 * @return 获取新的数据库连接
	 * @throws SQLException
	 */
	public JedisShardInfo getConnection() throws SQLException {
		synchronized (connectionsPool) {
			if (this.connectionsPool.size() > 0)
				return this.connectionsPool.removeFirst();

			if (this.currentCount < maxCount) {
				this.currentCount++;
				return ShardJedis.getShardConnection();
			}

			throw new SQLException("已没有链接");
		}
	}

	public void free(JedisShardInfo conn) {
		 this.connectionsPool.addLast(conn);
	}
	
	/**
	 * 更新数据
	 */
	public void updateProperties() {  
		ShardJedisPool shadow = new ShardJedisPool();  
        properties = shadow.getProperties();  
    }

	public Vector getProperties() {
		return properties;
	}

	public void setProperties(Vector properties) {
		this.properties = properties;
	}



}