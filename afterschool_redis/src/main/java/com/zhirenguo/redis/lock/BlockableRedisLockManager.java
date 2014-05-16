package com.zhirenguo.redis.lock;

import redis.clients.jedis.Jedis;

import com.zhirenguo.redis.RedisPoolHolder;
import com.zhirenguo.redis.lock.notify.LockNotifySubscriber;

public class BlockableRedisLockManager {

	private static final String KEYSPACE_EVENT_CHANNEL = "__keyspace@0__:";
	private LockNotifySubscriber subscripter;
	private RedisPoolHolder pool;
	
	public BlockableRedisLockManager(RedisPoolHolder pool) {
		super();
		this.subscripter =  new LockNotifySubscriber(KEYSPACE_EVENT_CHANNEL + RedisLock.LOCK_PREFIX + "#");
		this.pool = pool;
		initializeNotifier();
	}

	public BlockableRedisLock newLock(String lockId){
		BlockableRedisLock lock = new BlockableRedisLock(lockId, pool);
		subscripter.registLock(lockId);
		lock.setSub(subscripter);
		return lock;
	}
	
	private void initializeNotifier(){
		final Jedis notifyJedis = pool.getResource();
		Thread subThread = new Thread(new Runnable(){
			public void run(){
				notifyJedis.psubscribe(subscripter, KEYSPACE_EVENT_CHANNEL + RedisLock.LOCK_PREFIX + "#*");
			}
			
		}, "RedisNotifierSubscripter");
		subThread.start();
	}
}
