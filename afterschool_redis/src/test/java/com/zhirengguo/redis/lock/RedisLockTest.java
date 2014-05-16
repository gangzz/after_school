package com.zhirengguo.redis.lock;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.zhirenguo.redis.RedisPoolHolder;
import com.zhirenguo.redis.lock.RedisLock;
import com.zhirenguo.test.UnitTestUtil;

public class RedisLockTest {

	private RedisLock lock = null;
	private RedisPoolHolder holder = null;
	
	@Before
	public void initPerTest(){
		holder = new RedisPoolHolder();
		lock = new RedisLock("test1",holder);
	}
	
	@Test
	public void testTryLock(){
		final LockStatus lockStatus = new LockStatus();
		Runnable task = new Runnable() {
			
			public void run() {
				while(!lockStatus.isStopped()){
					while(!lock.tryLock()){
						UnitTestUtil.sleepRandMillis(100);
					}
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
	
	@Test
	public void testExpire(){
		lock.setLockedTime(1);
		boolean locked = lock.tryLock();
		Assert.assertEquals(true, locked);
		UnitTestUtil.sleep(2);
		boolean unlockPerformed = lock.unlock();
		Assert.assertEquals(false, unlockPerformed);
		locked = lock.tryLock();
		UnitTestUtil.sleepMillis(500);
		unlockPerformed = lock.unlock();
		Assert.assertEquals(true, locked);
		Assert.assertEquals(true, unlockPerformed);
	}
	
	/*
	 * 娴嬭瘯鍚屼竴涓嚎绋嬪鍚僼ryLock锛岄鏈熺粨鏋滃厑璁稿娆ock
	 */
	@Test
	public void testSameThreadMultiLock(){
		boolean locked = lock.tryLock();
		Assert.assertEquals(true, locked);
		locked = lock.tryLock();
		Assert.assertEquals(true, locked);
		locked = lock.tryLock();
		Assert.assertEquals(true, locked);
		lock.unlock();
	}
	
	/*
	 * 妯℃嫙涓嶅悓杩涚▼鐨則hread鎷ユ湁鐩稿悓id锛岄鏈熺粨鏋滀笉鍏佽閿�
	 */
	@Test
	public void testDifferenProcess(){
		RedisLock rlock = new RedisLock("test1",holder);
		boolean locked = lock.tryLock();
		Assert.assertEquals(true, locked);
		locked = rlock.tryLock();
		Assert.assertEquals(false, locked);
		locked = rlock.unlock();
		Assert.assertEquals(false, locked);
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
