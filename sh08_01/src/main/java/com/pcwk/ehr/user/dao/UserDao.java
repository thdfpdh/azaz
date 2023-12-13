package com.pcwk.ehr.user.dao;

import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;

import com.pcwk.ehr.user.domain.UserVO;

public interface UserDao {

	List<UserVO> getAll(UserVO inVO) throws SQLException;

	int update(UserVO inVO) throws SQLException;

	int getCount(UserVO inVO) throws SQLException;

	int del(UserVO inVO) throws SQLException;

	UserVO get(UserVO inVO) throws SQLException, EmptyResultDataAccessException;

	/**
	 * 단건등록
	 * 
	 * @param inVO
	 * @return1(성공)/0(실패) @throws SQLException
	 * @throws ClassNotFoundException
	 */
	int add(UserVO inVO) throws SQLException;

}