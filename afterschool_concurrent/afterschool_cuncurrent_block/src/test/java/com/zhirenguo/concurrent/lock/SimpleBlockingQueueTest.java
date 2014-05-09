package com.zhirenguo.concurrent.lock;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.zhirenguo.test.UnitTestUtil;

@RunWith(BlockJUnit4ClassRunner.class)
public class SimpleBlockingQueueTest {

	private SimpleBlockingQueue queue;
	
	@Before
	public void init(){
		queue = new SimpleBlockingQueue();
	}
	
	@Test
	public void testPutAndGet(){
		Thread pusher = new Thread(new Runnable() {
			public void run() {
				int i = 0;
				while(i < 10){
					try{
						queue.push("number " + i);
					} catch(Exception e){
						
					}
					i++;
					UnitTestUtil.sleep(1);
				}
			}
		});
		
		Runnable poll = new Runnable() {
			public void run() {
				while(true){
					try {
						Object value = queue.poll();
						System.out.println(Thread.currentThread().getName() + " polled " + value);
					} catch (InterruptedException e) {
					}
				}
			}
		};
		
		Thread puller1 = new Thread(poll, "poller1");
		Thread puller2 = new Thread(poll, "poller2");
		Thread puller3 = new Thread(poll, "poller3");
		pusher.start();
		puller1.start();
		puller2.start();
		puller3.start();
		UnitTestUtil.sleep(11);
		
	}
}
