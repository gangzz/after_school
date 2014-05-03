package com.zhirenguo.concurrent.semaphore;

import com.zhirenguo.concurrent.semaphore.ResourcePool;

public class BallPlayingKid implements Runnable {
	
	private String kidName;
	ResourcePool<Ball> pool;
	
	public BallPlayingKid(String name, ResourcePool<Ball> pool) {
		this.pool = pool;
		kidName = name;
	}

	public void run() {
		while(true){
			System.out.println("Kid " + kidName + " wants a boll");
			Ball ball = pool.getResouce();
			ball.play();
			pool.realeseReource(ball);
		}

	}

}
