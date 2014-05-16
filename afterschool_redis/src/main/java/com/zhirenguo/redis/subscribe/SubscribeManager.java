package com.zhirenguo.redis.subscribe;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import com.zhirenguo.redis.RedisPoolHolder;

public class SubscribeManager {

	private JedisPubSub sub = new DemoSubscribe();
	private RedisPoolHolder pool = new RedisPoolHolder();
	
	public void attachChannel(String channel){
		Jedis jedis = pool.getResource();
		try{
			jedis.subscribe(sub, channel);
		} finally {
			pool.returnResource(jedis);
		}
	}
}
