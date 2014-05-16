package com.zhirenguo.redis.lock.notify;

import java.util.Hashtable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.JedisPubSub;

public class LockNotifySubscriber extends JedisPubSub {
	
	private String prefix;
	
	private static final String[] unlockMsgTypes = {"del", "expired"};

	private Hashtable<String, Object> notifiers;
	
	private static final Logger logger = LoggerFactory.getLogger(LockNotifySubscriber.class);
	
	public LockNotifySubscriber(String prefix) {
		notifiers = new Hashtable<String, Object>();
		this.prefix = prefix;
	}
	
	public void waitForSignal(String lockId){
		Object notifier = notifiers.get(lockId);
		if(notifier == null){
			throw new RuntimeException("Thread not registed yet! lock id " +
						lockId + "thread name " + Thread.currentThread().getName());
		}
		synchronized(notifier){
			try {
				logger.info("Thread Locked thread name is " + Thread.currentThread().getName() + "lock Id is " + lockId);
				notifier.wait();
			} catch (InterruptedException e) {
			}
		}
	}
	
	public void registLock(String lockId){
		Object lock = notifiers.get(lockId);
		if(lock == null){
			notifiers.put(lockId, new Object());
		}
	}

	@Override
	public void onMessage(String channel, String message) {
		if(!isInArray(message, unlockMsgTypes)){
			return;
		}
		String lockId = channel.substring(prefix.length());
		Object notifier = notifiers.get(lockId);
		if(notifier == null){
			logger.warn("No notifier found for channel " + channel + ", msg :" + message);
			return;
		}
		synchronized (notifier) {
			logger.info("Thread notify all on lock with id " + lockId );
			notifier.notifyAll();
		}

	}

	@Override
	public void onPMessage(String pattern, String channel, String message) {
		onMessage(channel, message);
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
	
	private boolean isInArray(String key, String[] array){
		for(int i = 0; i < array.length; i++){
			if(array[i].equalsIgnoreCase(key)){
				return true;
			}
		}
		return false;
	}

}
