package org.jiumao.redis;

import java.sql.SQLException;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;

public class JedisPoolUtils {
	private static int initCount = 1<<4;
	private static int maxCount = 2000;
	private static long MaxWait = 2000l;
	//-------------------单机连接信息
		private static String url = "127.0.0.1";
		private static int port = 6379;
		private static JedisPool jedisPool;// 非切片连接池
		
		static{
			initJedisPool();
		}
		
	/**
	 * 
	 * @return 获取新的数据库连接
	 * @throws SQLException
	 */
	public static Jedis getConnection()  {
		synchronized (jedisPool) {
				return jedisPool.getResource();
		}
	}
	
	/**
	 * 初始化非切片池
	 */
	private static void initJedisPool(){
		// 池基本配置
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxActive(maxCount);
		config.setMaxIdle(initCount);
		config.setMaxWait(MaxWait);
		config.setTestOnBorrow(false);
		jedisPool = new JedisPool(config, url, port);
	}
	
	public  static boolean free(Jedis jedis){
		
			boolean success = true;
		  try {
			  jedisPool.returnResource(jedis);
			  return success;
		    }catch (Exception e) {
		        success  = false;
		        if(jedis != null){
		        	jedisPool.returnBrokenResource(jedis);
		        }
		        return success;
		    }finally{
		        if(success && jedis != null){
		        	jedisPool.returnResource(jedis);
		        }
		    }
	}
	
	public static void main(String[] args) {
		System.out.println(getConnection());
	}
	
}
