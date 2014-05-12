package com.zhirenguo.redis;

import com.zhirenguo.test.PropertiesUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisPoolHolder {

	private JedisPool defaultPool;
	
	public JedisPool getPool(){
		if(defaultPool == null){
			defaultPool = createPool();
		}
		return defaultPool;
	}
	
	public synchronized Jedis getResource(){
		return getPool().getResource();
	}
	
	public void returnResource(Jedis resource){
		getPool().returnResource(resource);
	}
	
	protected JedisPool createPool(){
		JedisPoolConfig config = new JedisPoolConfig();
		String host = PropertiesUtil.loadFromClasspath("redis.properties").getProperty("host");
		JedisPool pool = new JedisPool(config, host);
		return pool;
	}
}
