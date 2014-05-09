package com.zhirenguo.concurrent.lock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SimpleBlockingQueue {
	
	private List<Object> buffer;
	private int bufferSize = 5;
	private Lock lock;
	private Condition notFull, notEmpty;
	
	public SimpleBlockingQueue() {
		
		this(false);
	}
	
	public SimpleBlockingQueue(boolean fairness) {
		
		lock = new ReentrantLock(fairness);
		notFull = lock.newCondition();
		notEmpty = lock.newCondition();
		buffer = new ArrayList<Object>(bufferSize);
	}
	
	public void push(Object o) throws InterruptedException{
		lock.lock();
		try{
			while(buffer.size() == bufferSize){
				notFull.await();
			}
			buffer.add(o);
			notEmpty.signal();
		} finally {
			lock.unlock();
		}
	}
	
	public Object poll() throws InterruptedException {
		Object o = null;
		lock.lock();
		try{
			while(buffer.isEmpty()){
				notEmpty.await();
			}
			o = buffer.remove(0);
			notFull.signal();
		} finally {
			lock.unlock();
		}
		return o;
	}

}
