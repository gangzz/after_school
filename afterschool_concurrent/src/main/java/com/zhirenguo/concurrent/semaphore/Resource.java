package com.zhirenguo.concurrent.semaphore;

public abstract class Resource {
	
	private boolean acquired;
	
	void acquire(){
		acquired = true;
	}
	
	void release(){
		acquired = false;
	}

	public boolean isAcquired() {
		return acquired;
	}

}
