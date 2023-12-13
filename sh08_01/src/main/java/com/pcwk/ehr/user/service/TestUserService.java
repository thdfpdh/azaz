package com.pcwk.ehr.user.service;

import java.sql.SQLException;

import com.pcwk.ehr.user.domain.UserVO;

public class TestUserService extends UserServiceImpl {

	private String id;

	public TestUserService(String id) {
		super();
		this.id = id;
	}
<<<<<<< HEAD
	
	@Override
	protected void upgradeLevel(UserVO inVO) throws SQLException {
		if(id.equals(inVO.getUserId())) {
			throw new TestUserServiceException(id+"에서 예외 발생!");  
		}
		
		super.upgradeLevel(inVO);
		
		
	}
}
=======

	@Override
	protected void upgradeLevel(UserVO inVO) throws SQLException {
		if (id.equals(inVO.getUserId())) {
			throw new TestUserServiceException(id+"에서 예외 발생!");
		}
		
		super.upgradeLevel(inVO);
		
	}

}
>>>>>>> branch 'main' of https://github.com/thdfpdh/azaz.git
