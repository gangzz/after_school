package com.zhirenguo.redis.lock;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import redis.clients.jedis.Jedis;

import com.zhirenguo.redis.RedisPoolHolder;

public class RedisLock {
	
	private static final int DEAFULT_MAX_LOCKED_TIME = 100;
	private static final String SPLITTER = "#";
	public static final String LOCK_PREFIX = "RedisLck";
	
	private final String lockId, uuidDistinction;
	private int lockedTime = DEAFULT_MAX_LOCKED_TIME;
	protected RedisPoolHolder poolHolder;
	
	public RedisLock(String lockId, RedisPoolHolder holder) {
		this.lockId = lockId;
		uuidDistinction = UUID.randomUUID().toString();
		this.poolHolder = holder;
	}

	public boolean tryLock(){
		Jedis resource = null;
		try{
			resource = poolHolder.getResource();
			boolean locked = (resource.setnx(getKey(), encodeValue()) != 0);
			//already locked by a thread
			if(!locked){
				String value = resource.get(getKey());
				locked = alreadyLocked(value);
			}
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
			String value = resource.get(getKey());
			boolean locked = alreadyLocked(value);
			if(locked){
				return resource.del(getKey()) == 1;
			}
			return false;
		} finally {
			poolHolder.returnResource(resource);
		}
	}
	
	protected String getKey(){
		return LOCK_PREFIX + SPLITTER + lockId;
	}
	
	protected String encodeValue(){
		String value = uuidDistinction + SPLITTER + Thread.currentThread().getId();
		return value;
	}
	/**
	 * 
	 * @param value
	 * @return {uuid, threadId}
	 */
	protected String[] decodeValue(String value){
		return value.split(SPLITTER);
	}
	
	protected boolean alreadyLocked(String value){
		if(StringUtils.isBlank(value)){
			return false;
		}
		String[] result = decodeValue(value);
		if(uuidDistinction.equals(result[0])){
			return Thread.currentThread().getId() == Long.parseLong(result[1]);
		}
		return false;
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
