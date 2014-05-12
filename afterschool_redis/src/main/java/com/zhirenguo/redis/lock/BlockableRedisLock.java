package com.zhirenguo.redis.lock;

import redis.clients.jedis.Jedis;

import com.zhirenguo.redis.RedisPoolHolder;
import com.zhirenguo.redis.lock.notify.NotifySubscripter;

public class BlockableRedisLock extends RedisLock {
	
	private String channel;
	private NotifySubscripter sub;
	private static final String DEFAULT_UNLOCK_MSG = "unlockMsg";
	private static final String EXPIRED_KEYEVENT_CHANNEL = "__keyevent@0__:expired";

	public BlockableRedisLock(String lockId, String channel, RedisPoolHolder holder) {
		super(lockId, holder);
		this.channel = channel;
		sub = new NotifySubscripter();
		initializeNotifier();
	}
	
	public void lock(){
		while(true){
			boolean locked = tryLock();
			if(locked){
				break;
			}
			sub.waitForSignal();
		}
	}
	
	public boolean unlock(){
		boolean unlockPerformed = super.unlock();
		if(unlockPerformed){
			Jedis jedis = poolHolder.getResource();
			try{
				jedis.publish(channel, DEFAULT_UNLOCK_MSG);
				return true;
			} finally {
				poolHolder.returnResource(jedis);
			}
		} else {
			throw new RuntimeException("Redis unlocak failed, lock key is " + getKey());
		}
	}
	
	private void initializeNotifier(){
		final Jedis notifyJedis = poolHolder.getResource();
		final Jedis expireJedis = poolHolder.getResource();
		Thread subThread = new Thread(new Runnable(){
			public void run(){
				notifyJedis.subscribe(sub, channel);
			}
			
		}, "RedisNotifierSubscripter");
		Thread expThread = new Thread(new Runnable(){
			public void run(){
				expireJedis.subscribe(sub, EXPIRED_KEYEVENT_CHANNEL);
			}
			
		}, "RedisExpireSubscripter");
		subThread.start();
		expThread.start();
	}
	

}
