package com.zhirenguo.redis.lock.notify;

import redis.clients.jedis.JedisPubSub;

public class NotifySubscripter extends JedisPubSub {

	private Object notifier;
	
	public NotifySubscripter() {
		notifier = new Object();
	}
	
	public void waitForSignal(){
		synchronized(notifier){
			try {
				notifier.wait();
			} catch (InterruptedException e) {
			}
		}
	}

	@Override
	public void onMessage(String channel, String message) {
		synchronized (notifier) {
			notifier.notify();
		}

	}

	@Override
	public void onPMessage(String pattern, String channel, String message) {

	}

	@Override
	public void onSubscribe(String channel, int subscribedChannels) {

	}

	@Override
	public void onUnsubscribe(String channel, int subscribedChannels) {

	}

	@Override
	public void onPUnsubscribe(String pattern, int subscribedChannels) {

	}

	@Override
	public void onPSubscribe(String pattern, int subscribedChannels) {

	}

}
