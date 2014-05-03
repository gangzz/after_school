package com.zhirenguo.concurrent.semaphore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.zhirenguo.concurrent.semaphore.ResourcePool;

public class RunTest {

	public static void main(String[] args) throws IOException{
		
		List<Ball> balls = new ArrayList<Ball>();
		Ball basket = new Ball("BaskateBall");
		Ball socker = new Ball("socker");
		balls.add(basket);
		balls.add(socker);
		ResourcePool<Ball> pool = new ResourcePool<Ball>(2);
		pool.setResources(balls);
		Thread th1 = new Thread(new BallPlayingKid("ALi", pool));
		Thread th2 = new Thread(new BallPlayingKid("Tencent", pool));
		Thread th3 = new Thread(new BallPlayingKid("NetEasy", pool));
		
		th1.start();
		th2.start();
		th3.start();
		
		System.in.read();
			
	}
}
