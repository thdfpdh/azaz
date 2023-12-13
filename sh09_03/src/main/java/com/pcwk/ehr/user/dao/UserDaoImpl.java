package com.pcwk.ehr.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.pcwk.ehr.user.domain.Level;
import com.pcwk.ehr.user.domain.UserVO;

//c+s+F:자바소스정리//웹에서는 X
public class UserDaoImpl implements UserDao {
	final Logger LOG = LogManager.getLogger(UserDaoImpl.class);

	private JdbcTemplate jdbcTemplate;

	private DataSource dataSource;

	public UserDaoImpl() {
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		// jdbcTemplate객체 생성(db연결정보를 넣어서)
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List<UserVO> getAll(UserVO inVO) throws SQLException{
		List<UserVO> outList = new ArrayList<UserVO>();
		StringBuilder sb = new StringBuilder(300);
		sb.append(" SELECT  t1.user_id,                                        \n");
		sb.append("         t1.name,                                           \n");
		sb.append("         t1.password,                                       \n");
		sb.append("         t1.u_level,                                        \n");
		sb.append("         t1.login,                                          \n");
		sb.append("         t1.recommend,                                      \n");
		sb.append("         t1.email,                                          \n");
		sb.append("         TO_CHAR(t1.reg_dt,'YYYY-MM-DD HH24:MI:SS') reg_dt  \n");
		sb.append(" FROM                                                       \n");
		sb.append(" 	users t1                                               \n");
		sb.append(" WHERE user_id LIKE ?||'%'                                  \n");
		sb.append(" ORDER BY t1.user_id                                        \n");
		
		LOG.debug("1.sql \n" + sb.toString());
		LOG.debug("2.param \n" + inVO.toString());
		
		// param
		Object[] args = { inVO.getUserId()};
				
		outList = this.jdbcTemplate.query(sb.toString(), args, new RowMapper<UserVO>() {
			@Override
			public UserVO mapRow(ResultSet rs, int rowNum) throws SQLException {
				UserVO out = new UserVO();

				out.setUserId(rs.getString("user_id"));
				out.setName(rs.getString("name"));
				out.setPassword(rs.getString("password"));
				// 2023.12.01 추가 -----------------------------------------------
				out.setLevel(Level.valueOf(rs.getInt("u_level")));
				out.setLogin(rs.getInt("login"));
				out.setRecommend(rs.getInt("recommend"));
				out.setEmail(rs.getString("email"));
				out.setRegDt(rs.getString("reg_dt"));
				// -------------------------------------------------------------

				return out;
			}
		});
		
		
		return outList;
	}

	@Override
	public int update(UserVO inVO) throws SQLException {
		int flag = 0;
		StringBuilder sb = new StringBuilder(200);
		sb.append(" UPDATE users             \n");
		sb.append(" SET  name 		= ?      \n");
		sb.append("     ,password   = ?      \n");
		sb.append("     ,u_level    = ?      \n");
		sb.append("     ,login 		= ?      \n");
		sb.append("     ,recommend  = ?      \n");
		sb.append("     ,email		= ?      \n");
		sb.append("     ,reg_dt 	= SYSDATE      \n");
		sb.append(" WHERE                    \n");
		sb.append("     user_id = ?          \n");
		LOG.debug("1.sql 	\n" + sb.toString());
		LOG.debug("2.param 	\n" + inVO.toString());

		// param
		Object[] args = { inVO.getName(), 
				          inVO.getPassword(), 
				          inVO.getLevel().intValue(), 
				          inVO.getLogin(),
				          inVO.getRecommend(), 
				          inVO.getEmail(), 
				          inVO.getUserId() };

		flag = this.jdbcTemplate.update(sb.toString(), args);
		LOG.debug("3.flag 	\n" + flag);
		return flag;
	}

	@Override
	public int getCount(UserVO inVO) throws SQLException {
		int count = 0;

		StringBuilder sb = new StringBuilder(300);
		sb.append(" SELECT                      \n ");
		sb.append("     COUNT(*) cnt            \n ");
		sb.append(" FROM                        \n ");
		sb.append("     users                   \n ");
		sb.append(" WHERE user_id LIKE ?||'%'   \n ");

		// LOG.debug("1.sql \n" + sb.toString());
		LOG.debug("2.param \n" + inVO.toString());

		Object[] args = { inVO.getUserId() };
		count = this.jdbcTemplate.queryForObject(sb.toString(), new RowMapper<Integer>() {

			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				int count = 0;
				count = rs.getInt("cnt");

				return count;
			}

		}, args);
		LOG.debug("3.count  \n" + count);
		return count;
	}

	@Override
	public int del(final UserVO inVO) throws SQLException {
		int flag = 0;

		// -------------------------------------------------------------
		StringBuilder sb = new StringBuilder(200);

		sb.append(" DELETE FROM users \n");
		sb.append(" WHERE user_id = ? \n");

		LOG.debug("1.sql	\n" + sb.toString());
		LOG.debug("2.param	\n" + inVO.toString());

		// param
		Object[] args = { inVO.getUserId() };

		// jdbcTemplate.update DML(insert, update, delete)
		flag = this.jdbcTemplate.update(sb.toString(), args);
		LOG.debug("3.flag	\n" + flag);
		// -------------------------------------------------------------

		return flag;
	}

	@Override
	public UserVO get(UserVO inVO) throws SQLException, EmptyResultDataAccessException {
		UserVO outVO = null;

		StringBuilder sb = new StringBuilder(300);

		sb.append(" SELECT            \n ");
		sb.append("     user_id,      \n ");
		sb.append("     name,         \n ");
		sb.append("     password,     \n ");

		// 2023.12.01 추가 -------------------------------------------------------
		sb.append("     u_level,     									 \n ");
		sb.append("     login,     	  									 \n ");
		sb.append("     recommend,    									 \n ");
		sb.append("     email,     	  									 \n ");
		sb.append("     TO_CHAR(reg_dt, 'yyyy-mm-dd HH24:MI:SS') reg_dt	 \n ");
		// ---------------------------------------------------------------------

		sb.append(" FROM              \n ");
		sb.append("     users         \n ");
		sb.append(" where user_id = ? \n ");

		LOG.debug("1.sql \n" + sb.toString());
		LOG.debug("2.param \n" + inVO.toString());

		// param
		Object[] args = { inVO.getUserId() };

		// jdbcTemplate.queryForObject
		outVO = this.jdbcTemplate.queryForObject(sb.toString(), new RowMapper<UserVO>() {

			@Override
			public UserVO mapRow(ResultSet rs, int rowNum) throws SQLException {
				UserVO out = new UserVO();

				out.setUserId(rs.getString("user_id"));
				out.setName(rs.getString("name"));
				out.setPassword(rs.getString("password"));
				// 2023.12.01 추가 -----------------------------------------------
				out.setLevel(Level.valueOf(rs.getInt("u_level")));
				out.setLogin(rs.getInt("login"));
				out.setRecommend(rs.getInt("recommend"));
				out.setEmail(rs.getString("email"));
				out.setRegDt(rs.getString("reg_dt"));
				// -------------------------------------------------------------

				return out;
			}
		}, args);
		LOG.debug("3.outVO \n" + outVO);

		return outVO;
	}

	/**
	 * 단건등록
	 * 
	 * @param inVO
	 * @return1(성공)/0(실패) @throws SQLException
	 * @throws ClassNotFoundException
	 */
	@Override
	public int add(UserVO inVO) throws SQLException {
		int flag = 0;
		// ----------------------------------------------------------
		StringBuilder sb = new StringBuilder(200);

		sb.append(" INSERT INTO users ( \n ");
		sb.append("     user_id,        \n ");
		sb.append("     name,           \n ");
		sb.append("     password,       \n ");

		// 2023.12.01추가-----------------------------------------------
		sb.append("     u_level,     		 \n ");
		sb.append("     login,     			 \n ");
		sb.append("     recommend,     		 \n ");
		sb.append("     email,     		 	 \n ");
		sb.append("     reg_dt      	 	 \n ");
		sb.append(" ) VALUES (          	 \n ");
		sb.append("     ?,              	 \n ");
		sb.append("     ?,              	 \n ");
		sb.append("     ?,              	 \n ");
		sb.append("     ?,               	 \n ");
		sb.append("     ?,               	 \n ");
		sb.append("     ?,              	 \n ");
		sb.append("     ?,              	 \n ");
		sb.append("     SYSDATE              \n ");
		sb.append(" )                   	 \n ");
		LOG.debug("1.sql 	\n" + sb.toString());
		LOG.debug("2.param 	\n" + inVO.toString());
		// ----------------------------------------------------------

		// param
		Object[] args = { inVO.getUserId(), inVO.getName(), inVO.getPassword(), inVO.getLevel().intValue(),
				inVO.getLogin(), inVO.getRecommend(), inVO.getEmail() };

		// jdbcTemplate.update DML(insert,update,delete,merge)
		flag = this.jdbcTemplate.update(sb.toString(), args);
		LOG.debug("3.flag 	\n" + flag);

		return flag;
	}

}