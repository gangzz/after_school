package com.zhirenguo.redis.lock;

import com.zhirenguo.redis.RedisPoolHolder;
import com.zhirenguo.redis.lock.notify.LockNotifySubscriber;

public class BlockableRedisLock extends RedisLock {
	
	private LockNotifySubscriber sub;
	
	BlockableRedisLock(String lockId, RedisPoolHolder holder) {
		super(lockId, holder);
	}
	
	public void lock(){
		while(true){
			boolean locked = tryLock();
			if(locked){
				break;
			}
			sub.waitForSignal(getLockId());
		}
	}
	
	public void setSub(LockNotifySubscriber sub) {
		this.sub = sub;
	}
	
	
	
}
