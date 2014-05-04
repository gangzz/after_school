package com.zhirenguo.afterschool.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User{

	@Column(name="id", nullable=false)
	@Id
	private int id;
	
	@Column(name="username", nullable=false)
	private String name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
