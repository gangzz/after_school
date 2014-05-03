package com.zhirenguo.concurrent.semaphore;

import java.util.List;
import java.util.concurrent.Semaphore;

public class ResourcePool<R extends Resource> {
	
	private Semaphore semaphore;
	private List<R> resources;
	
	public ResourcePool(int num) {
		
		semaphore = new Semaphore(num, true);
	}
	
	public R getResouce(){
		try {
			semaphore.acquire();
			for(R resource : resources){
				if(!resource.isAcquired()){
					resource.acquire();
					return resource;
				}
			}
		} catch (InterruptedException e) {
			//ignore
		}
		
		return null;
	}
	
	public void realeseReource(R resource){
		if(resources.contains(resource)){
			resource.release();
		}
		semaphore.release();
	}

	public List<R> getResources() {
		return resources;
	}

	public void setResources(List<R> resources) {
		this.resources = resources;
	}
	
	

}
