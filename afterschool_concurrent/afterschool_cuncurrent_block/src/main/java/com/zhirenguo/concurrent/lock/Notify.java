package com.zhirenguo.concurrent.lock;

import com.zhirenguo.test.UnitTestUtil;

public class Notify {
	
	private static Object notifier = new Object();

	public static void main(String[] args) {
		Runnable runWill = new TalkerRunner("will");
		Runnable runHey = new TalkerRunner("willard");
		Thread thw = new Thread(runWill);
		Thread thy = new Thread(runHey);
		thw.start();
		thy.start();
		UnitTestUtil.sleep(2);
		synchronized(notifier){
			notifier.notify();
		}
		UnitTestUtil.sleep(2);
		synchronized(notifier){
			notifier.notify();
		}
		
	}
	
	private static void waitForConfigtion(){
		try {
			synchronized(notifier){
			notifier.wait();
			}
		} catch (InterruptedException e) {
		}
	}
	
	private static class TalkerRunner implements Runnable{

		private String name;
		
		public TalkerRunner(String name) {

			this.name = name;
		}
		
		public void run() {
			waitForConfigtion();
			System.out.println(this.name);
			
		}
		
	}
}
