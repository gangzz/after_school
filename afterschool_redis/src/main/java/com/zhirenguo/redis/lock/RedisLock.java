package com.zhirenguo.redis.lock;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.zhirenguo.redis.SimpleClient;

public class RedisLock {
	
	private String Id;
	private String lockPrefix = "lock:";
	private JedisPool pool;
	
	public RedisLock(String id) {
		this.Id = id;
	}

	public synchronized boolean tryLock(){
		Jedis resource = null;
		try{
			resource = getPool().getResource();
			Long threadId = Thread.currentThread().getId();
			boolean locked = (resource.setnx(getKey(), threadId.toString()) != 0);
			return locked;
		} finally {
			if(resource != null){
				getPool().returnResource(resource);
			}
		}
		
	}
	
	public boolean unlock(){
		Jedis resource = null;
		try{
			resource = getPool().getResource();
			Long threadId = Thread.currentThread().getId();
			boolean locked = (threadId.toString().equals(resource.get(getKey() )));
			if(locked){
				return resource.del(getKey()) == 1;
			}
			return false;
		} finally {
			getPool().returnResource(resource);
		}
	}
	
	private String getKey(){
		return lockPrefix + Id;
	}
	
	public JedisPool getPool(){
		if(pool == null)
			pool = SimpleClient.getPool();
		return pool;
	}
}
