package com.zhirengguo.afterschool.redis.lock;

import org.junit.Assert;
import org.junit.Test;

import com.zhirenguo.redis.lock.RedisLock;
import com.zhirenguo.test.UnitTestUtil;

public class RedisLockTest {

	private RedisLock lock = new RedisLock("1");
	
	@Test
	public void testMulThread(){
		final LockStatus lockStatus = new LockStatus();
		Runnable task = new Runnable() {
			
			public void run() {
				while(!lockStatus.isStopped()){
					while(!lock.tryLock()){
						UnitTestUtil.sleepRandMillis(200);
					}
					Assert.assertEquals(false, lockStatus.isLocked());
					lockStatus.setLocked(true);
					UnitTestUtil.sleepRandMillis(1000);
					lockStatus.setLocked(false);
					lock.unlock();
					UnitTestUtil.sleep(1);
				}
			}
		};
		
		Thread th1 = new Thread(task, "th1");
		Thread th2 = new Thread(task, "th2");
		Thread th3 = new Thread(task, "th3");
		th1.start();
		th2.start();
		th3.start();
		UnitTestUtil.sleep(10);
		lockStatus.setStopped(true);
		UnitTestUtil.sleep(2);
	}
	
	private class LockStatus {
		private boolean locked;
		private boolean stopped = false;
		
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

		
		
	}
	
}
