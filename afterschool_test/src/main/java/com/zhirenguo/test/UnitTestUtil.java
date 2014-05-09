package com.zhirenguo.test;

import java.util.Random;

public class UnitTestUtil {

	private static Random random = new Random();
	
	public static void sleep(int num){
		sleepMillis(num * 1000);
	}
	
	public static void sleepMillis(int num){
		try{
			Thread.sleep(num);
		} catch(Exception e){
			
		}
	}
	
	public static void sleepRandMillis(int max){
		int num = random.nextInt(max);
		sleepMillis(num);
	}
}
