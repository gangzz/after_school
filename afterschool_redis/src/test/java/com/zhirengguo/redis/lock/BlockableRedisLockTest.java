package com.zhirengguo.redis.lock;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.zhirenguo.redis.RedisPoolHolder;
import com.zhirenguo.redis.lock.BlockableRedisLock;
import com.zhirenguo.test.UnitTestUtil;

public class BlockableRedisLockTest {

	private BlockableRedisLock lock ;
	
	@Before
	public void init(){
		lock = new BlockableRedisLock("1", "channel1", new RedisPoolHolder());
	}
	
	@Test
	public void testTryLock(){
		final LockStatus lockStatus = new LockStatus();
		Runnable task = new Runnable() {
			
			public void run() {
				while(!lockStatus.isStopped()){
					lock.lock();
					Assert.assertEquals(false, lockStatus.isLocked());
					lockStatus.setLocked(true);
					UnitTestUtil.sleepRandMillis(400);
					lockStatus.incrementValue();
					lockStatus.setLocked(false);
					lock.unlock();
					UnitTestUtil.sleepMillis(100);
				}
			}
		};
		
		Thread th1 = new Thread(task, "th1");
		Thread th2 = new Thread(task, "th2");
		Thread th3 = new Thread(task, "th3");
		th1.start();
		th2.start();
		th3.start();
		UnitTestUtil.sleep(5);
		Assert.assertEquals(true, lockStatus.getValue() > 9);
		lockStatus.setStopped(true);
		UnitTestUtil.sleep(1);
	}
	
	@Test(expected=RuntimeException.class)
	public void testExpire(){
		final LockStatus lockStatus = new LockStatus();
		lock.setLockedTime(2);
		Thread th = new Thread(new Runnable() {
			
			public void run() {
				lock.lock();
				lockStatus.setLocked(true);
				lock.unlock();
			}
		}, "RedisLockTestThread");
		lock.lock();
		th.start();
		UnitTestUtil.sleep(3);
		Assert.assertEquals(true, lockStatus.isLocked());
		lock.unlock();
	}
	
	private class LockStatus {
		private boolean locked;
		private boolean stopped = false;
		private int value = 0;
		
		public boolean isStopped() {
			return stopped;
		}

		public void setStopped(boolean stopped) {
			this.stopped = stopped;
		}

		public boolean isLocked() {
			return locked;
		}

		public void setLocked(boolean locked) {
			this.locked = locked;
		}

		public int getValue(){
			return value;
		}
		
		public void incrementValue (){
			value ++;
		}
		
	}
	
}
