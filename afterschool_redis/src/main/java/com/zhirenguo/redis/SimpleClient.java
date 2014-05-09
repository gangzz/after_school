package com.zhirenguo.redis;

import com.zhirenguo.test.PropertiesUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class SimpleClient {

	public static void main(String[] args) {
		JedisPool pool = getPool();
		Jedis jedis = pool.getResource();
		jedis.set("name", "wangyang");
		System.out.println(jedis.get("name"));
		pool.returnResource(jedis);
		pool.destroy();
	}
	
	public static JedisPool getPool(){
		JedisPoolConfig config = new JedisPoolConfig();
		String host = PropertiesUtil.loadFromClasspath("redis.properties").getProperty("host");
		JedisPool pool = new JedisPool(config, host);
		return pool;
	}
	
}
