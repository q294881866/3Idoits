package org.jiumao.redis;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import redis.clients.jedis.Jedis;

public class DataStore {

	//获取redis连接
	private ShardJedisPool shardJedisPool = ShardJedisPool.getInstance();
	private Jedis jedis = null;
	public DataStore() {
		try {
			 this.jedis = shardJedisPool.getConnection().createResource();
			 jedis.select(0);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * 比较俩个词同时出现的次数
	 * 比较hash值小的为第一个词
	 * 大的为第二个词
	 * @param firstWord 第一个词
	 * @param score 同时出现的次数，自动增长
	 * @param lastWord 第二个词
	 */
	public  Double setNearScore(String firstWord,String lastWord){
		
		return jedis.zincrby(firstWord, 1, lastWord);
		
	}
	
	/**
	 * 
	 * 比较俩个词同时出现的次数
	 * 比较hash值小的为第一个词
	 * 大的为第二个词
	 * @param firstWord 第一个词
	 * @param score 同时出现的次数，自动增长
	 * @param lastWord 第二个词
	 */
	public  Set<String> getNearScore(String key){
		
		return jedis.zrange(key, 0, jedis.zcard(key));
		
	}
	
	/**
	 * 倒排索引建立
	 * @param key 关键词
	 * @param members 词出现的位置
	 * @return
	 */
	public Long daoPai(String key,String... members){
		
		return jedis.sadd(key, members);
	}
	
	
	
	/**
	 * 文章的特征值加权存储
	 * @param args
	 */
	public void docStore(Map map,String docUrl){

		Iterator iter = map.entrySet().iterator();
		String[] ss = docIndex(map);
		//2.有序数组拼接字符串，为索引
		StringBuilder builder = new StringBuilder();
		for (int i = 1; i < ss.length; i++) {
			System.out.println("排序后的数组输出---"+ss[i]);
			builder.append(ss[i]);
		}
		String docIndex=builder.toString();
		
		while (iter.hasNext()) {
			Map.Entry entry = (Entry) iter.next();
			while (iter.hasNext()) {
				Object key = entry.getKey();
				
				Object value = entry.getValue();
				jedis.hset(docIndex, key.toString()+"||"+value.toString(),docUrl );
			}
			
			
			
			//ss是一个有序列表，
			for (int i = 0; i < ss.length; i++) {
				for (int j = i+1; j <ss.length; j++) {
					//增加亲密度，即同时出现的次数加1
					jedis.zincrby(ss[i], 1, ss[j]);
				}
			}
			
			//增加倒排
			for (String  s : ss) {
				System.out.println(s+"   数组排序后");
				jedis.sadd(s, docIndex);
			}
			
		}
	}
	
	
	/**
	 * 关键字的访问频率
	 * @param args
	 */
	public  void setWordCount(String word){
		jedis.incrBy(word, 1);
	}
	
	private  String[] docIndex(Map<String, Integer> map){
				Set set = map.keySet();
				//1.key放到数组中，变成有序列表
				String[] keys=new String[map.size()];
				
				int keysIndex = 0;
				//key放到数组中，变成有序列表
				for(Object o:set){
					keys[keysIndex] = o.toString();
					keysIndex++;
				}
				//排序数组
				Arrays.sort(keys);
				//索引通过||拼接字符串
				String[] ss = keys;
				return ss;
	}
	
	public static void main(String[] args) {
		
		Map m = new Hashtable();
		for (int i = 0; i < 100; i++) {
			
			m.put(Math.floor(Math.random()*100)+"", "test");
		}
		new DataStore().docStore(m, "127.0.0.1");
	}
}
