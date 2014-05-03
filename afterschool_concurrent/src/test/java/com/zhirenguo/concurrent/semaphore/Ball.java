package com.zhirenguo.concurrent.semaphore;

import java.util.Random;

import com.zhirenguo.concurrent.semaphore.Resource;

public class Ball extends Resource {
	
	private String name;
	private Random random;
	
	public Ball(String name){
		this.name = name;
		random = new Random();
	}

	public void play(){
		int nextNum = random.nextInt(10);
		System.out.println("Thread " + name +" sleep " + nextNum + " secondes");
		try2Sleep(nextNum);
	}
	
	private void try2Sleep(int i){
		try{
			Thread.sleep(1000 * i);
		} catch (InterruptedException e){
			
		}
	}
}
