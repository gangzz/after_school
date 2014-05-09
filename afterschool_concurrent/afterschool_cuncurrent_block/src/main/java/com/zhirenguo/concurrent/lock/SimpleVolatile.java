package com.zhirenguo.concurrent.lock;

/**
 * volatile���ڱ�ǲ�������ˮ�Ż����߼����������Ĳ�̫���ϡ�����
 * @author wangyang <wangyang@fangdd.com>
 *
 */
public class SimpleVolatile {

	private volatile boolean isShutdown = false;
//	private AtomicInteger num = new AtomicInteger(0);
	private int num = 0;
	
	public void doIfNotDown(){
		if(!isShutdown){
			try{
				Thread.sleep(20);
			} catch(Exception e){
				
			}
			System.out.println(num++ + " : " + isShutdown);
		}
	}
	
	public void setDown(){
		isShutdown = true;
	}
}
