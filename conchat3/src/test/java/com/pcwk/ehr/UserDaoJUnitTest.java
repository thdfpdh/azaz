//package com.pcwk.ehr;
//
//import static org.hamcrest.CoreMatchers.is;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertThat;
//
//import java.sql.SQLException;
//import java.util.List;
//
//import javax.annotation.Resource;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.FixMethodOrder;
//import org.junit.Ignore;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.MethodSorters;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationContext;
//import org.springframework.dao.EmptyResultDataAccessException;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import com.pcwk.ehr.user.dao.UserDao;
//import com.pcwk.ehr.user.dao.UserDaoImpl;
//import com.pcwk.ehr.user.domain.Level;
//import com.pcwk.ehr.user.domain.UserVO;
//
//@RunWith(SpringJUnit4ClassRunner.class) // 스프링 테스트 컨텍스트 프레임워크의 JUnit 확장기능 지정
//@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml","file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml"})
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//public class UserDaoJUnitTest {
//
//	final Logger LOG = LogManager.getLogger(getClass());
//
//	@Autowired
//	UserDao dao;
//
//	// 등록
//	UserVO userVO1;
//	UserVO userVO2;
//	UserVO userVO3;
//
//	// 조회(getCount)
//	UserVO searchVO;
//
//	@Autowired // 테스트 오브젝트가 만들어지고 나면 스프링 테스트 컨텍스트에 자동으로 객체값으로 주입
//	ApplicationContext context;
//
//	@Before
//	public void setUp() throws Exception {
////		context = new GenericXmlApplicationContext("applicationContext.xml");
////		dao = context.getBean("userDao", UserDaoImpl.class);
//
//		// 등록
//		userVO1 = new UserVO("김지우-1", 123456, "1111-1", "1111", "abcd1@abcd.com", "0", 1, 1);
//		userVO2 = new UserVO("김지우-2", 123456, "1111-2", "1111", "abcd2@abcd.com", "0", 1, 1);
//		userVO3 = new UserVO("김지우-3", 123456, "1111-3", "1111", "abcd3@abcd.com", "0", 1, 1);
//
//		// 조회
//		searchVO = new UserVO();
//		searchVO.setId("김지우-");
//
//		LOG.debug("context: " + context);
//		LOG.debug("dao: " + dao);
//	}
//
//	@After
//	public void tearDown() throws Exception {
//		LOG.debug("tearDown");
//	}
//	
//	@Test
//	public void doRetrieve() throws SQLException {
//		LOG.debug("========================================");
//		LOG.debug("doRetrieve()");
//		
//		searchVO.setPageSize(10L);
//		searchVO.setPageNo(1L);
//		searchVO.setSearchDiv("30");
//		searchVO.setSearchWord("김지우");
//		
//		List<UserVO> list = dao.doRetrieve(this.searchVO);
//		assertEquals(3, list.size());
//	}
//	
////	@Ignore
//	@Test
//	public void getAll() throws SQLException {
//		LOG.debug("========================================");
//		LOG.debug("getAll()");
//		
//		dao.doDelete(userVO1);
//		dao.doDelete(userVO2);
//		dao.doDelete(userVO3);
//		assertEquals(0, dao.getCount(searchVO));
//		
//		// 데이터 입력
//		int flag = dao.doSave(userVO1);
//		
//		// 건수 확인
//		assertEquals(1, flag);
//		assertEquals(1, dao.getCount(searchVO));
//		
//		flag = dao.doSave(userVO2);
//		assertEquals(1, flag);
//		assertEquals(2, dao.getCount(searchVO));
//		
//		flag = dao.doSave(userVO3);
//		assertEquals(1, flag);
//		assertEquals(3, dao.getCount(searchVO));
//		
//		List<UserVO> list = dao.getAll(searchVO);
//		assertEquals(3, list.size());
//		
//		for(UserVO vo : list) LOG.debug("vo");
//		
//		isSameUser(userVO1, list.get(0));
//		isSameUser(userVO2, list.get(1));
//		isSameUser(userVO3, list.get(2));
//	}
//	
//	@Ignore
//	@Test
//	public void update() throws SQLException {
//		LOG.debug("======================");
//		LOG.debug("update()");
//		LOG.debug("======================");
//
//		dao.doDelete(userVO1);
//		dao.doDelete(userVO2);
//		dao.doDelete(userVO3);
//		assertEquals(0, dao.getCount(searchVO));
//
//		dao.doSave(userVO1);
//		assertEquals(1, dao.getCount(searchVO));
//
//		UserVO getVO = dao.doSelectOne(userVO1);
//		isSameUser(getVO, userVO1);
//
//		String upStr = "_U";
//		int upInt = 10;
//
//		getVO.setNm(getVO.getNm() + upStr);
//		getVO.setPw(getVO.getPw() + upStr);
////		getVO.setLevel(Level.SILVER);
//		getVO.setLogin(getVO.getLogin() + upInt);
//		getVO.setRecommend(getVO.getRecommend() + upInt);
//		getVO.setEmail(getVO.getEmail() + upStr);
//
//		int flag = dao.doUpdate(getVO);
//		assertEquals(1, flag);
//
//		UserVO vsVO = dao.doSelectOne(getVO);
//
//		isSameUser(vsVO, getVO);
//	}
//
////	@Ignore
//	@Test(timeout = 3000)
//	public void AddAndGet() throws ClassNotFoundException, SQLException {
//
//		// 1. 데이터 삭제
//		dao.doDelete(userVO1);
//		dao.doDelete(userVO2);
//		dao.doDelete(userVO3);
//		assertThat(dao.getCount(searchVO), is(0));
//
//		// 2. 데이터 등록
//		int flag = dao.doSave(userVO1);
//		int count = dao.getCount(searchVO);
//		assertThat(flag, is(1));
//		assertThat(count, is(1));
//		if (flag == 1)
//			LOG.debug("등록 성공");
//		else
//			LOG.debug("등록 실패");
//
//		flag = dao.doSave(userVO2);
//		count = dao.getCount(searchVO);
//		assertThat(flag, is(1));
//		assertThat(count, is(2));
//
//		flag = dao.doSave(userVO3);
//		count = dao.getCount(searchVO);
//		assertThat(flag, is(1));
//		assertThat(count, is(3));
//
//		// 3. 데이터 조회
//		UserVO outVO1 = dao.doSelectOne(userVO1);
//		UserVO outVO2 = dao.doSelectOne(userVO2);
//		UserVO outVO3 = dao.doSelectOne(userVO3);
//		assertNotNull(outVO1); // Not Null이면 true
//		assertNotNull(outVO2);
//		assertNotNull(outVO3);
//
//		isSameUser(userVO1, outVO1);
//		isSameUser(userVO2, outVO2);
//		isSameUser(userVO3, outVO3);
//	}
//
//	@Ignore
//	@Test(expected = EmptyResultDataAccessException.class)
//	public void getFailure() throws SQLException {
//		LOG.debug("getFailure");
//		dao.doDelete(userVO1);
//		dao.doDelete(userVO2);
//		dao.doDelete(userVO3);
//
//		userVO1.setUserId("unknown id");
//
//		dao.doSelectOne(userVO1);
//	}
//
//	private void isSameUser(UserVO userVO, UserVO outVO) {
//		assertEquals(userVO.getUserId(), outVO.getUserId());
//		assertEquals(userVO.getName(), outVO.getName());
//		assertEquals(userVO.getPassword(), outVO.getPassword());
//
//		assertEquals(userVO.getLevel(), outVO.getLevel());
//		assertEquals(userVO.getLogin(), outVO.getLogin());
//		assertEquals(userVO.getRecommend(), outVO.getRecommend());
//		assertEquals(userVO.getEmail(), outVO.getEmail());
//	}
//
//}
