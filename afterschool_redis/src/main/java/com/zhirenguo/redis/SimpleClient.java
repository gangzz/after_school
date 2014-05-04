package com.zhirenguo.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class SimpleClient {

	public static void main(String[] args) {
		JedisPoolConfig config = new JedisPoolConfig();
		JedisPool pool = new JedisPool(config, "localhost");
		Jedis jedis = pool.getResource();
		jedis.set("name", "wangyang");
		System.out.println(jedis.get("name"));
		pool.returnResource(jedis);
	}
}
