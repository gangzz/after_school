package com.zhirenguo.concurrent.lock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

@RunWith(BlockJUnit4ClassRunner.class)
public class SimpleLockTest {

	private SimpleBlock block;
	
	@Test
	public void testLock(){
		block = new SimpleBlock();
		Thread thread1 = new Thread(new Runnable() {
			public void run() {
				block.lock();
				sleep(9);
				block.unLock();
			}
		});
		Thread thread2 = new Thread(new Runnable() {
			public void run() {
				while(true){
					block.lock();
					System.out.println("get lock");
					sleep(1);
				}
			}
		});
		
		thread1.start();
		thread2.start();
		sleep(10);
	}
	
	
	
	private void sleep(int sec){
		try {
			Thread.sleep(1000*sec);
		} catch (InterruptedException e) {
		};
	}
}
