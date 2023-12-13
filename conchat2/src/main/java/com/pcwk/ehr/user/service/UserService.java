package com.pcwk.ehr.user.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;

import com.pcwk.ehr.user.domain.UserVO;

public interface UserService {
	
	public List<UserVO> doRetrieve(UserVO inVO) throws SQLException;
	
	public int doSave(UserVO inVO) throws SQLException;
	
	public UserVO doSelectOne(UserVO inVO) throws SQLException, EmptyResultDataAccessException;
	
	public int doDelete(final UserVO inVO) throws SQLException;
	
	public int getCount(UserVO inVO) throws SQLException;
	
	public int doUpdate(UserVO inVO) throws SQLException;
	
	public List<UserVO> getAll(UserVO inVO) throws SQLException;
	
	/*
	 * 최초 로그인 시 BASIC
	 */
	public void add(UserVO inVO) throws SQLException;
	
	/*
	 * 회원 등업
	 * @param inVO
	 * @throws SQLException
	 */
//	public void upgradeLevels(UserVO inVO) throws Exception;
}