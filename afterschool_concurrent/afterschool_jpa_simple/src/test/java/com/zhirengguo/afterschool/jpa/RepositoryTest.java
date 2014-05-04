package com.zhirengguo.afterschool.jpa;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.zhirenguo.afterschool.jpa.User;
import com.zhirenguo.afterschool.jpa.UserRepository;

public class RepositoryTest {

	public static void main(String[] args) {
		
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/data.xml");
		UserRepository repository = (UserRepository)context.getBean("userRepository");
		User user = new User();
		user.setName("hello");
		repository.save(user);
	}
}
