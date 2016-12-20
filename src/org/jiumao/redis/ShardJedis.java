package org.jiumao.redis;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;

public class ShardJedis {

	

	//----------------切片连接信息
	private static String shardUrl = "127.0.0.1";
	private static int shardPort = 6379;
	private static String shardPost = "master";
	
	
	
	private ShardJedis() {
	}
	
	

	public static JedisShardInfo getShardConnection() throws SQLException {
		return new JedisShardInfo(shardUrl,shardPort, shardPost);
	}

	
	
	
}
