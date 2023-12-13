//package com.pcwk.ehr;
//
////import static com.pcwk.ehr.user.service.UserServiceImpl.MIN_LOGIN_COUNT_FOR_SILVER;
////import static com.pcwk.ehr.user.service.UserServiceImpl.MIN_RECOMMEND_COUNT_FOR_GOLD;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//
//import java.sql.SQLException;
//import java.util.Arrays;
//import java.util.List;
//
//import javax.annotation.Resource;
//import javax.sql.DataSource;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.junit.Before;
//import org.junit.FixMethodOrder;
//import org.junit.Ignore;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.MethodSorters;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.dao.EmptyResultDataAccessException;
//import org.springframework.mail.MailSender;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.transaction.PlatformTransactionManager;
//
//import com.pcwk.ehr.user.dao.UserDao;
//import com.pcwk.ehr.user.domain.Level;
//import com.pcwk.ehr.user.domain.UserVO;
//import com.pcwk.ehr.user.service.TestUserService;
//import com.pcwk.ehr.user.service.UserService;
//
//@RunWith(SpringJUnit4ClassRunner.class) // 스프링 테스트 컨텍스트 프레임워크의 JUnit 확장기능 지정
//@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml","file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml"})
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//public class UserServiceTest {
//
//	final Logger LOG = LogManager.getLogger(getClass());
//
//	@Resource(name = "dummyMailSender")
//	MailSender mailSender;
//
//	@Autowired
//	PlatformTransactionManager transactionManager;
//
//	@Autowired
//	DataSource dataSource;
//
//	@Autowired
//	UserDao userDao;
//
//	@Autowired
//	UserService userService;
//
//	List<UserVO> users;
//
//	UserVO searchVO;
//
//	@Before
//	public void setUp() throws Exception {
//		LOG.debug("***************************************************************");
//		LOG.debug("setUp()");
//
//		users = Arrays.asList(
//				new UserVO("김지우-1", 123456, "1111-1", "1111", "abcd1@abcd.com", "0", 1, 1),
//				new UserVO("김지우-2", 123456, "1111-2", "1111", "abcd2@abcd.com", "0", 1, 1),
//				new UserVO("김지우-3", 123456, "1111-3", "1111", "abcd3@abcd.com", "0", 1, 1),
//				new UserVO("김지우-4", 123456, "1111-4", "1111", "abcd4@abcd.com", "0", 1, 1),
//				new UserVO("김지우-5", 123456, "1111-5", "1111", "abcd5@abcd.com", "0", 1, 1));
//
//		searchVO = new UserVO();
//		searchVO.setId("김지우-");
//	}
//
//	@Ignore
//	@Test
//	public void upgradeAllOrNothing() throws Exception {
//		/*
//		 * 5명 중 2명 등업 대상 4번째 사용자에서 강제 예외 발생 2번째 사용자 등급이 rollback 되면 성공
//		 */
//		TestUserService testUserService = new TestUserService(users.get(3).getUserId());
//		// 수동으로 userDao DI
//		testUserService.setMailSender(mailSender);
////		testUserService.setUserDao(userDao);
////		testUserService.setDataSource(dataSource);
////		testUserService.setTransactionManager(transactionManager);
//		LOG.debug("========================================");
//		LOG.debug("upgradeAllOrNothing()");
//
//		try {
//			for (UserVO vo : users) {
//				userDao.doDelete(vo);
//			}
//			assertEquals(0, userDao.getCount(searchVO));
//
//			for (UserVO vo : users) {
//				userDao.doSave(vo);
//			}
//			assertEquals(5, userDao.getCount(searchVO));
//
//			// 등업(트랜잭션 x)
//			testUserService.upgradeLevels(searchVO);
//
//		} catch (Exception e) {
//			LOG.debug("Exception: " + e.getMessage());
//		}
//
//		checkLevel(users.get(1), false);
//
//	}
//
//	@Ignore
//	@Test
//	public void add() throws SQLException {
//		for (UserVO vo : users) {
//			userDao.doDelete(vo);
//		}
//		assertEquals(0, userDao.getCount(searchVO));
//
//		for (UserVO vo : users) {
//			if (vo.getUserId().equals("p100_1")) {
//				vo.setLevel(null);
//			}
//			userService.add(vo);
//		}
//		checkLevel(users.get(0), false);
//	}
//
//	private void checkLevel(UserVO userVO, boolean upgraded) throws EmptyResultDataAccessException, SQLException {
//		UserVO userUpdate = userDao.doSelectOne(userVO);
//
//		LOG.debug("********************checkLevel*********************");
//
//		if (upgraded == true) {
//			assertEquals(userUpdate.getLevel(), userVO.getLevel().nextLevel());
//		} else {
//			assertEquals(userUpdate.getLevel(), userVO.getLevel());
//		}
//	}
//
////	@Ignore
//	@Test
//	public void upgradeLevels() throws Exception {
//		LOG.debug("++++++++++++++++++++++++++++++++++++++");
//		LOG.debug("upgradeLevels()");
//
//		// 1. users 데이터 삭제
//		// 2. users 데이터 입력
//		// 3. 등업
//		// 4. 등업 데이터 비교 p100_2, p100_4
//
//		// 1
//		for (UserVO vo : users) {
//			userDao.doDelete(vo);
//		}
//		assertEquals(0, userDao.getCount(searchVO));
//
//		// 2
//		for (UserVO vo : users) {
//			userDao.doSave(vo);
//		}
//		assertEquals(5, userDao.getCount(searchVO));
//
//		// 3
//		userService.upgradeLevels(searchVO);
//
//		// 4
//		checkLevel(users.get(0), false);
//		checkLevel(users.get(1), false);		//등업 :basic->silver
//		checkLevel(users.get(2), false);
//		checkLevel(users.get(3), false);		//등업 :silver->gold
//		checkLevel(users.get(4), false);
//	}
//
//	@Test
//	public void beans() {
//		LOG.debug("=====================================");
//		LOG.debug("beans()");
//		LOG.debug("mailSender: " + mailSender);
//		LOG.debug("transactionManager: " + transactionManager);
//		LOG.debug("dataSource: " + dataSource);
//		LOG.debug("userDao: " + userDao);
//		LOG.debug("userService: " + userService);
//		LOG.debug("=====================================");
//		assertNotNull(mailSender);
//		assertNotNull(transactionManager);
//		assertNotNull(dataSource);
//		assertNotNull(userDao);
//		assertNotNull(userService);
//	}
//
//}
