package com.zhirenguo.concurrent.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SimpleBlock {

	private Lock lock = new ReentrantLock();
	
	public void lock(){
		lock.lock();
	}
	
	public void methodNeedLock(){
		boolean getLock = lock.tryLock();
		if(getLock){
			System.out.println("do something");
		} else {
			System.out.println("do nothing.");
		}
		if(getLock)
			lock.unlock();
	}
	
	public void unLock(){
		lock.unlock();
	}
}
