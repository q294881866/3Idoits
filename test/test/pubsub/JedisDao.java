package test.pubsub;

import org.jiumao.redis.JedisPoolUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class JedisDao {

	private Jedis jedis;
	private JedisPubSub jedisPubSub;
	
	public JedisDao() {
		jedis = getSession();
		jedisPubSub = new Subscriber();
	}
	
	public Jedis getSession(){
		return  JedisPoolUtils.getConnection();
	}
	
	public void publish(String channel,String message ){
		jedis.publish(channel, message);
	}
	
	public void listener(String... channels){
		getSession().subscribe(jedisPubSub, channels);
		jedisPubSub.unsubscribe();//关闭
	}
}
