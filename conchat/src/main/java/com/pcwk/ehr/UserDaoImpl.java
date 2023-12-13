package com.pcwk.ehr;

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

public class UserDaoImpl implements UserDao {

	final Logger LOG = LogManager.getLogger(UserDaoImpl.class);

	private JdbcTemplate jdbcTemplate;

	private DataSource dataSource;

	public UserDaoImpl() {
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;

		// JdbcTemplate 객체 생성 (DB 연결 정보)
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<UserVO> getAll(UserVO inVO) throws SQLException {
		List<UserVO> outList = new ArrayList<UserVO>();
		StringBuilder sb = new StringBuilder(300);

		sb.append(" SELECT                                                                          \n");
		sb.append("     no,                                                                           \n");
		sb.append("     nm,                                                                          \n");
		sb.append("     birth,                                                                        \n");
		sb.append("     id,                                                                           \n");
		sb.append("     pw,                                                                          \n");
		sb.append("     email,                                                                       \n");
		sb.append("     TO_CHAR(reg_ymd, 'YYYY-MM-DD HH24:MI:SS') reg_ymd,    \n");
		sb.append("     chat,                                                                        \n");
		sb.append("     act,                                                                          \n");
		sb.append("     tier,                                                                          \n");
		sb.append("     on_line,                                                                     \n");
		sb.append("     pop_scr                                                                     \n");
		sb.append(" FROM                                                                           \n");
		sb.append("     users                                                                        \n");
		sb.append(" WHERE id = ?                                                                 \n");
		sb.append(" ORDER BY id                                                                  \n");

		LOG.debug("1. SQL  \n" + sb.toString());
		LOG.debug("2. param \n" + inVO.toString());

		Object[] args = { inVO.getId() };

		outList = this.jdbcTemplate.query(sb.toString(), args, new RowMapper<UserVO>() {

			@Override
			public UserVO mapRow(ResultSet rs, int rowNum) throws SQLException {
				UserVO out = new UserVO();
				out.setNo(rs.getInt("no"));
				out.setNm(rs.getString("nm"));
				out.setBirth(rs.getInt("birth"));
				out.setId(rs.getString("id"));
				out.setPw(rs.getString("pw"));
				out.setEmail(rs.getString("email"));
				out.setReg_ymd(rs.getString("reg_ymd"));
				out.setChat(rs.getInt("chat"));
				out.setAct(rs.getInt("act"));
				out.setTier(rs.getInt("tier"));
				out.setOn_line(rs.getInt("on_line"));
				out.setPop_scr(rs.getDouble("pop_scr"));

				return out;
			}

		});

		return outList;
	}

//	@Override
//	public int update(UserVO inVO) throws SQLException {
//		int flag = 0;
//		StringBuilder sb = new StringBuilder();
//
//		sb.append(" UPDATE users       \n");
//		sb.append(" SET name = ?       \n");
//		sb.append("     password = ?   \n");
//		sb.append("     u_level = ?    \n");
//		sb.append("     login = ?      \n");
//		sb.append("     recommend = ?  \n");
//		sb.append("     email = ?      \n");
//		sb.append("     reg_dt = SYSDATE     \n");
//		sb.append(" WHERE              \n");
//		sb.append("     user_id = ?    \n");
//
//		LOG.debug("1. SQL  \n" + sb.toString());
//		LOG.debug("2. param \n" + inVO.toString());
//
//		Object[] args = { inVO.getName(), inVO.getPassword(), inVO.getLevel().intValue(), inVO.getLogin(),
//				inVO.getRecommend(), inVO.getEmail(), inVO.getUser_id() };
//
//		flag = this.jdbcTemplate.update(sb.toString(), args);
//
//		LOG.debug("3. flag: " + flag);
//
//		return flag;
//	}

	/*
	 * 단건 등록
	 * 
	 * @param inVO
	 * 
	 * @return 1(성공)/0(실패)
	 */
	@Override
	public int add(UserVO inVO) throws SQLException {
		int flag = 0;

		StringBuilder sb = new StringBuilder(200);

		sb.append(" INSERT INTO users(   \n");
		sb.append("     nm,                   \n");
		sb.append("     birth,                 \n");
		sb.append("     id,                     \n");
		sb.append("     pw,                    \n");
		sb.append("     email,                 \n");
		sb.append("     reg_ymd,             \n");
		sb.append("     chat,                  \n");
		sb.append("     act,                   \n");
		sb.append("     tier,                   \n");
		sb.append("     on_line,              \n");
		sb.append("     pop_scr              \n");
		sb.append(" ) VALUES (              \n");
		sb.append(" 			?,              \n");
		sb.append(" 			?,              \n");
		sb.append(" 			?,              \n");
		sb.append(" 			?,              \n");
		sb.append(" 			?,              \n");
		sb.append(" 			SYSDATE,    \n");
		sb.append(" 			?,              \n");
		sb.append(" 			?,              \n");
		sb.append(" 			?,              \n");
		sb.append(" 			?,              \n");
		sb.append(" 			?              \n");
		sb.append(" )                          \n");

		LOG.debug("1. SQL  \n" + sb.toString());
		LOG.debug("2. param \n" + inVO.toString());

		Object[] args = { inVO.getNm(), inVO.getBirth(), inVO.getId(), inVO.getPw(), inVO.getEmail(),
				inVO.getChat(), inVO.getAct(), inVO.getTier(), inVO.getOn_line(), inVO.getPop_scr() };

		flag = this.jdbcTemplate.update(sb.toString(), args);

		LOG.debug("3. flag \n" + flag);

		return flag;
	}

	/*
	 * 단건 조회
	 * 
	 * @param inVO
	 * 
	 * @return outVO
	 */
	@Override
	public UserVO get(UserVO inVO) throws SQLException, EmptyResultDataAccessException {
		UserVO outVO = null;
		StringBuilder sb = new StringBuilder(100);

		sb.append(" SELECT                                                                          \n");
		sb.append("     no,                                                                           \n");
		sb.append("     nm,                                                                          \n");
		sb.append("     birth,                                                                        \n");
		sb.append("     id,                                                                           \n");
		sb.append("     pw,                                                                          \n");
		sb.append("     email,                                                                       \n");
		sb.append("     TO_CHAR(reg_ymd, 'YYYY-MM-DD HH24:MI:SS') reg_ymd,    \n");
		sb.append("     chat,                                                                        \n");
		sb.append("     act,                                                                          \n");
		sb.append("     tier,                                                                          \n");
		sb.append("     on_line,                                                                     \n");
		sb.append("     pop_scr                                                                     \n");
		sb.append(" FROM                                                                           \n");
		sb.append("     users                                                                        \n");
		sb.append(" WHERE id = ?                                                                \n");

		LOG.debug("1. SQL \n" + sb.toString());
		LOG.debug("2. param \n" + inVO.toString());

		Object[] args = { inVO.getId() };

		outVO = this.jdbcTemplate.queryForObject(sb.toString(), new RowMapper<UserVO>() {
			@Override
			public UserVO mapRow(ResultSet rs, int rowNum) throws SQLException {
				UserVO out = new UserVO();
				out.setNo(rs.getInt("no"));
				out.setNm(rs.getString("nm"));
				out.setBirth(rs.getInt("birth"));
				out.setId(rs.getString("id"));
				out.setPw(rs.getString("pw"));
				out.setEmail(rs.getString("email"));
				out.setReg_ymd(rs.getString("reg_ymd"));
				out.setChat(rs.getInt("chat"));
				out.setAct(rs.getInt("act"));
				out.setTier(rs.getInt("tier"));
				out.setOn_line(rs.getInt("on_line"));
				out.setPop_scr(rs.getDouble("pop_scr"));

				return out;
			}

		}, args);

		LOG.debug("3. outVO \n" + outVO.toString());	// db에서 no 가져와서 출력하는 부분 !!

		return outVO;
	}

	@Override
	public int del(final UserVO inVO) throws SQLException {
		int flag = 0;

		StringBuilder sb = new StringBuilder(200);
		sb.append(" DELETE FROM users  \n");
		sb.append(" WHERE id = ?         \n");

		LOG.debug("1. SQL  \n" + sb.toString());
		LOG.debug("2. param \n" + inVO.toString());

		Object[] args = { inVO.getId() };

		flag = this.jdbcTemplate.update(sb.toString(), args);

		LOG.debug("3. flag \n" + flag);

		return flag;
	}

	@Override
	public int getCount(UserVO inVO) throws SQLException {
		int count = 0;

		StringBuilder sb = new StringBuilder(100);

		sb.append(" SELECT COUNT(*) cnt           \n");
		sb.append(" FROM                               \n");
		sb.append("     users                            \n");
		sb.append(" WHERE id = ?                     \n");

		LOG.debug("1. SQL  \n" + sb.toString());
		LOG.debug("2. param \n" + inVO.toString());
		LOG.debug("2. getId \n" + inVO.getId() );

		Object[] args = { inVO.getId() };

		count = this.jdbcTemplate.queryForObject(sb.toString(), new RowMapper<Integer>() {

			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				int result = 0;
				result = rs.getInt("cnt");

				return result;
			}
		}, args);

		LOG.debug("3. count: " + count);

		return count;
	}
}