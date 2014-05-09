package com.zhirenguo.concurrent.lock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.zhirenguo.test.UnitTestUtil;

@RunWith(BlockJUnit4ClassRunner.class)
public class SimpleVolatileTest {

	private SimpleVolatile test = new SimpleVolatile();
	
	@Test
	public void testRun(){
		Runnable run = new Runnable() {
			public void run() {
				while(true){
					test.doIfNotDown();
				}
			}
		};
		Thread thread1 = new Thread(run, "invoker1");
		Thread thread2 = new Thread(run, "invoker2");
		Thread thread3 = new Thread(run, "invoker3");
		Thread thread4 = new Thread(run, "invoker4");
		Thread th = new Thread(){
			@Override
			public void run() {
				UnitTestUtil.sleep(1);
				test.setDown();
				System.err.print("Down");
			}
		};
		thread1.start();
		thread2.start();
		th.start();
		thread3.start();
		thread4.start();
		UnitTestUtil.sleep(4);
		
	}
}
