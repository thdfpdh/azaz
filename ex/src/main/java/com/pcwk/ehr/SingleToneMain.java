package com.pcwk.ehr;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SingleToneMain {

	public static void main(String[] args) {
		DaoFactory factory = new DaoFactory();
		
		UserDao userDao01 = factory.userDao();
		UserDao userDao02 = factory.userDao();
		
		System.out.println("==============================");
		System.out.println("=userDao01="+userDao01);//userDao01=com.pcwk.ehr.UserDao@15db9742
		System.out.println("=userDao02="+userDao02);//userDao02=com.pcwk.ehr.UserDao@6d06d69c
		System.out.println("==============================");
		
		ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
		UserDao userDao03 = context.getBean("userDao", UserDao.class);
		UserDao userDao04 = context.getBean("userDao", UserDao.class);
		//SingleTon
		System.out.println("===============================");
		System.out.println("=userDao03="+userDao03);//=userDao03=com.pcwk.ehr.UserDao@62e136d3
		System.out.println("=userDao04="+userDao04);//=userDao04=com.pcwk.ehr.UserDao@62e136d3
		System.out.println("===============================");
		
	}

}
