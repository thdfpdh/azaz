package com.pcwk.ehr;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.model.TestTimedOutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class) // 스프링 테스트 컨텍스트 프레임워크의 JUnit 확장기능 지정
@ContextConfiguration(locations = "/applicationContext.xml")
public class UserDaoJUnitTest {

	final Logger LOG = LogManager.getLogger(getClass());

	UserDao dao;

	// 등록
	UserVO userVO;

	// 조회(getCount)
	UserVO searchVO;

	@Autowired // 테스트 오브젝트가 만들어지고 나면 스프링 테스트 컨텍스트에 자동으로 객체값으로 주입
	ApplicationContext context;

	@Before
	public void setUp() throws Exception {
//		context = new GenericXmlApplicationContext("applicationContext.xml");
		dao = context.getBean("userDao", UserDaoImpl.class);

		// 등록
		userVO = new UserVO("이오", 218, "song03", "1234", "thdfpdh@gmail.com", "", 3, 1, 8, 2, 10.2);

		// 조회
		searchVO = new UserVO();
		searchVO.setId("song03");

//		LOG.debug("context: " + context);
//		LOG.debug("dao: " + dao);
	}

	@After
	public void tearDown() throws Exception {
		LOG.debug("tearDown");
	}
	
	@Ignore
	@Test
	public void getAll() throws SQLException {
		LOG.debug("========================================");
		LOG.debug("getAll()");
		
		dao.del(userVO);
		assertEquals(0, dao.getCount(searchVO));
		
		// 데이터 입력
		int flag = dao.add(userVO);
		
		// 번호 확인해서 넣기 //
		
		// 건수 확인
//		assertEquals(1, flag);
//		assertEquals(1, dao.getCount(searchVO));
		
		List<UserVO> list = dao.getAll(searchVO);
		assertEquals(1, list.size());
		
		for(UserVO vo : list) LOG.debug("vo");
		
		isSameUser(userVO, list.get(0));
	}
	
//	@Ignore
//	@Test
//	public void update() throws SQLException {
//		LOG.debug("======================");
//		LOG.debug("update()");
//		LOG.debug("======================");
//
//		dao.del(userVO);
//		assertEquals(0, dao.getCount(searchVO));
//
//		dao.add(userVO);
//		assertEquals(1, dao.getCount(searchVO));
//
//		UserVO getVO = dao.get(userVO);
//		isSameUser(getVO, userVO);
//
//		String upStr = "_U";
//		int upInt = 10;
//
//		getVO.setName(getVO.getName() + upStr);
//		getVO.setPassword(getVO.getPassword() + upStr);
//		getVO.setLevel(Level.SILVER);
//		getVO.setLogin(getVO.getLogin() + upInt);
//		getVO.setRecommend(getVO.getRecommend() + upInt);
//		getVO.setEmail(getVO.getEmail() + upStr);
//
//		int flag = dao.update(getVO);
//		assertEquals(1, flag);
//
//		UserVO vsVO = dao.get(getVO);
//
//		isSameUser(vsVO, getVO);
//	}

	
	@Test(timeout = 3000)
	public void AddAndGet() throws ClassNotFoundException, SQLException {

		// 1. 데이터 삭제
		dao.del(userVO);

		// 2. 데이터 등록
		int flag = dao.add(userVO);
		int count = dao.getCount(searchVO);
		assertThat(flag, is(1));
		assertThat(count, is(1));
		if (flag == 1)
			LOG.debug("등록 성공");
		else
			LOG.debug("등록 실패");

		// 3. 데이터 조회
		UserVO outVO = dao.get(userVO);
		assertNotNull(outVO); // Not Null이면 true
		
		LOG.debug(outVO.toString());

		isSameUser(userVO, outVO);
	}

//	@Ignore
//	@Test(expected = EmptyResultDataAccessException.class)
//	public void getFailure() throws SQLException {
//		LOG.debug("getFailure");
//		dao.del(userVO);
//
//		userVO.setId("unknown id");
//
//		dao.get(userVO);
//	}

	private void isSameUser(UserVO userVO, UserVO outVO) {
//		assertEquals(userVO.getNo(), outVO.getNo());
		assertEquals(userVO.getNm(), outVO.getNm());
		assertEquals(userVO.getBirth(), outVO.getBirth());
		assertEquals(userVO.getId(), outVO.getId());
		assertEquals(userVO.getPw(), outVO.getPw());
		assertEquals(userVO.getEmail(), outVO.getEmail());
//		assertEquals(userVO.getReg_ymd(), outVO.getReg_ymd());
		assertEquals(userVO.getChat(), outVO.getChat());
		assertEquals(userVO.getAct(), outVO.getAct());
		assertEquals(userVO.getTier(), outVO.getTier());
		assertEquals(userVO.getOn_line(), outVO.getOn_line());
//		assertEquals(userVO.getPop_scr(), outVO.getPop_scr());
	}

}
