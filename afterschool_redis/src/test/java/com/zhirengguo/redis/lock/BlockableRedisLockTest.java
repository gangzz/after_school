package com.zhirengguo.redis.lock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.zhirenguo.redis.RedisPoolHolder;
import com.zhirenguo.redis.lock.BlockableRedisLock;
import com.zhirenguo.redis.lock.BlockableRedisLockManager;
import com.zhirenguo.test.UnitTestUtil;

@RunWith(BlockJUnit4ClassRunner.class)
public class BlockableRedisLockTest {

	private BlockableRedisLock lock1;
	private BlockableRedisLock lock2;
	
	@Before
	public void init(){
		RedisPoolHolder holder = new RedisPoolHolder();
		BlockableRedisLockManager manager = new BlockableRedisLockManager(holder);
		lock1 = manager.newLock("test1");
		lock2 = manager.newLock("test2");
	}
	
	@Test
	public void testTryLock(){
		final LockStatus lockStatus = new LockStatus();
		Runnable task = new Runnable() {
			
			public void run() {
				while(!lockStatus.isStopped()){
					lock1.lock();
					assertEquals(false, lockStatus.isLocked());
					lockStatus.setLocked(true);
					UnitTestUtil.sleepRandMillis(400);
					lockStatus.incrementValue();
					lockStatus.setLocked(false);
					lock1.unlock();
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
		assertEquals(true, lockStatus.getValue() > 9);
		lockStatus.setStopped(true);
		UnitTestUtil.sleep(1);
	}
	
	@Test
	public void testExpire(){
		LockStatus lockStatus = new LockStatus();
		lock1.setLockedTime(2);
		Thread th = createLockThread(lockStatus);
		lock1.lock();
		th.start();
		UnitTestUtil.sleep(20);
		assertEquals(true, lockStatus.isLocked());
		assertFalse(lock1.unlock());
	}
	
	@Test
	public void testMoreThanOneLock(){
		LockStatus lockStatus1 = new LockStatus();
		LockStatus lockStatus2 = new LockStatus();
		Thread th1 = createLockThread(lockStatus1);
		Thread th2 = createLockThread(lockStatus2);
		lock1.lock();
		lock2.lock();
		th1.start();
		th2.start();
		assertFalse(lockStatus1.isLocked());
		assertFalse(lockStatus2.isLocked());
		lock1.unlock();
		UnitTestUtil.sleepMillis(100);
		assertTrue(lockStatus1.isLocked());
		lock2.unlock();
		UnitTestUtil.sleepMillis(100);
		assertTrue(lockStatus2.isLocked());
	}
	
	private Thread createLockThread(final LockStatus lockStatus){
		Thread th = new Thread(new Runnable() {
			
			public void run() {
				lock1.lock();
				lockStatus.setLocked(true);
				lock1.unlock();
			}
		}, "RedisLockTestThread");
		return th;
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
