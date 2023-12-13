package com.pcwk.ehr.user.service;

import java.sql.SQLException;

import com.pcwk.ehr.user.domain.UserVO;

public interface UserService {

	/**
	 * 최초 로그인시 등급은 basic
	 * @param inVO
	 * @throws SQLException
	 */
	public void add(UserVO inVO) throws SQLException;
	
	/**
	 * 회원등업
	 * @param inVO
	 * @throws SQLException
	 */
	public void upgradeLevels(UserVO inVO) throws SQLException;
}
