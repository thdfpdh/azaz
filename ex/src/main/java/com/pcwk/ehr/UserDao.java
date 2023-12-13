package com.pcwk.ehr;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserDao {

	private DataSource dataSource;

	final Logger LOG = LogManager.getLogger(UserDao.class);

	public UserDao() {
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public int getCount(UserVO inVO) throws SQLException {
		int count = 0;

		// 1.Connection연결
		// 2.PreparedStatement,Statement(X)
		// 3.SQL run
		// 4.결과
		// 5.자원반납

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder(100);

		conn = dataSource.getConnection();
		sb.append(" SELECT                      \n");
		sb.append("     COUNT(*) cnt            \n");
		sb.append(" FROM                        \n");
		sb.append(" 	users                   \n");
		sb.append(" WHERE user_id LIKE ?||'%'   \n");
		
		LOG.debug("1.sql   \n" + sb.toString());
		LOG.debug("2.param \n" + inVO.toString());
		
		pstmt = conn.prepareStatement(sb.toString());
		
		pstmt.setString(1, inVO.getUserId());
		
		rs = pstmt.executeQuery();
		
		if(rs.next() == true) {
			count=rs.getInt("cnt");
		}
		LOG.debug("3. count:" + count);
		
		
		// 자원 반납
		rs.close();
		pstmt.close();
		conn.close();

		return count;
	}
	
	public int del(UserVO inVO) throws SQLException {
		int flag = 0;
		// 1.Connection연결
		// 2.PreparedStatement,Statement(X)
		// 3.SQL run
		// 4.결과
		// 5.자원반납

		Connection conn = null;
		PreparedStatement pstmt = null;
		StringBuilder sb = new StringBuilder(200);

		conn = dataSource.getConnection();
		sb.append(" DELETE FROM users    \n");
		sb.append(" WHERE  user_id = ?   \n");

		LOG.debug("1.sql   \n" + sb.toString());
		LOG.debug("2.param \n" + inVO.toString());

		pstmt = conn.prepareStatement(sb.toString());

		pstmt.setString(1, inVO.getUserId());

		flag = pstmt.executeUpdate();
		LOG.debug("3.flag " + flag);

		pstmt.close();
		conn.close();
		return flag;
	}
	
	public UserVO get(UserVO inVO) throws SQLException, ClassNotFoundException {
		UserVO outVO = null;
		// 1.Connection연결
		// 2.PreparedStatement,Statement(X)
		// 3.SQL run
		// 4.결과
		// 5.자원반납
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder(100);

		conn = dataSource.getConnection();
		sb.append(" SELECT             \n");
		sb.append("     user_id,       \n");
		sb.append("     name,          \n");
		sb.append("     password       \n");
		sb.append(" FROM               \n");
		sb.append("     users          \n");
		sb.append(" WHERE user_id = ?  \n");

		System.out.println("1.sql   \n" + sb.toString());
		System.out.println("2.param \n" + inVO.toString());

		pstmt = conn.prepareStatement(sb.toString());

		pstmt.setString(1, inVO.getUserId());

		rs = pstmt.executeQuery();

		if (rs.next() == true) {
			outVO = new UserVO();

			outVO.setUserId(rs.getString("user_id"));
			outVO.setName(rs.getString("name"));
			outVO.setPassword(rs.getString("password"));
		}
		System.out.println("3. outVO:" + outVO.toString());

		rs.close();
		pstmt.close();
		conn.close();

		return outVO;
	}

	/**
	 * 단건 등록
	 * 
	 * @param inVO
	 * @return 1(성공)/0(실패)
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public int add(UserVO inVO) throws SQLException, ClassNotFoundException {
		int flag = 0;

		// 1.Connection연결
		// 2.PreparedStatement,Statement(X)
		// 3.SQL run
		// 4.결과
		// 5.자원반납

		Connection conn = null;
		PreparedStatement pstmt = null;
		StringBuilder sb = new StringBuilder(200);

		conn = dataSource.getConnection();


		sb.append(" INSERT INTO users (  \n");
		sb.append("     user_id,         \n");
		sb.append("     name,            \n");
		sb.append("     password         \n");
		sb.append(" ) VALUES (           \n");
		sb.append("     ?,               \n");
		sb.append("     ?,               \n");
		sb.append("     ?                \n");
		sb.append(" )                    \n");

		System.out.println("1.sql   \n" + sb.toString());
		System.out.println("2.param \n" + inVO.toString());

		pstmt = conn.prepareStatement(sb.toString());

		pstmt.setString(1, inVO.getUserId());
		pstmt.setString(2, inVO.getName());
		pstmt.setString(3, inVO.getPassword());

		flag = pstmt.executeUpdate();
		System.out.println("3.flag " + flag);

		pstmt.close();
		conn.close();

		return flag;
	}
}
