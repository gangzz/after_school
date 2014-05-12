package com.zhirenguo.redis.lock;

import redis.clients.jedis.Jedis;

import com.zhirenguo.redis.RedisPoolHolder;
import com.zhirenguo.util.uuid.UUIDUtil;

public class RedisLock {
	
	private static final int DEAFULT_MAX_LOCKED_TIME = 10;
	private static final String SPLITTER = "#";
	
	private final String lockId;
	private String lockPrefix = "lock:";
	private String uuidDistinction;
	private int lockedTime = DEAFULT_MAX_LOCKED_TIME;
	protected RedisPoolHolder poolHolder;
	
	public RedisLock(String lockId, RedisPoolHolder holder) {
		this.lockId = lockId;
		uuidDistinction = UUIDUtil.getUUIDStr();
		this.poolHolder = holder;
	}

	public boolean tryLock(){
		Jedis resource = null;
		try{
			resource = poolHolder.getResource();
			Long threadId = Thread.currentThread().getId();
			boolean locked = (resource.setnx(getKey(), threadId.toString()) != 0);
			if(locked){
				resource.expire(getKey(), lockedTime);
			}
			return locked;
		} finally {
			if(resource != null){
				poolHolder.returnResource(resource);
			}
		}
		
	}
	
	public boolean unlock(){
		Jedis resource = null;
		try{
			resource = poolHolder.getResource();
			Long threadId = Thread.currentThread().getId();
			boolean locked = (threadId.toString().equals(resource.get(getKey() )));
			if(locked){
				return resource.del(getKey()) == 1;
			}
			return false;
		} finally {
			poolHolder.returnResource(resource);
		}
	}
	
	protected String getKey(){
		return lockPrefix + lockId;
	}
	
	protected String encodeValue(){
		String value = uuidDistinction + SPLITTER + Thread.currentThread().getId();
		return value;
	}
	
	protected String[] decodeValue(String value){
		return value.split(SPLITTER);
	}

	public String getLockId() {
		return lockId;
	}

	public int getLockedTime() {
		return lockedTime;
	}

	public void setLockedTime(int lockedTime) {
		this.lockedTime = lockedTime;
	}

	public RedisPoolHolder getPoolHolder() {
		return poolHolder;
	}

	public void setPoolHolder(RedisPoolHolder poolHolder) {
		this.poolHolder = poolHolder;
	}
	
	
}
