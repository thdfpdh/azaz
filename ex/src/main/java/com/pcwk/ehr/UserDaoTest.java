package com.pcwk.ehr;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class UserDaoTest {
	UserDao dao;
	UserVO  userVO;
	ApplicationContext context;
	
	public UserDaoTest() {
		
		context = new AnnotationConfigApplicationContext(DaoFactory.class);
		
		dao = context.getBean("userDao",UserDao.class);
		System.out.println("====================");
		System.out.println("=context="+context);
		System.out.println("=dao="+dao);
		System.out.println("====================");
		
		userVO = new UserVO("a188", "호우날두", "1818");

	}
	
	public void add() {
		try {
			int flag = dao.add(userVO);
			if(1==flag) {
				System.out.println("등록 성공!");
			}else {
				System.out.println("등록 실패!");
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void get() {
		try {
			UserVO outVO = dao.get(userVO);
			if(null !=outVO) {
				System.out.println("단건조회 성공");

			} else {
				System.out.println("단건 조회 실패");
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public static void main(String[] args) {
		UserDaoTest  main=new UserDaoTest();
		//main.getConnection();
		main.add();
		main.get();
	}

}
