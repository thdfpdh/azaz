package com.pcwk.ehr;

import static com.pcwk.ehr.user.service.UserServiceImpl.MIN_LOGIN_COUNT_FOR_SILVER;
import static com.pcwk.ehr.user.service.UserServiceImpl.MIN_RECOMEND_COUNT_FOR_GOLD;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import com.pcwk.ehr.user.dao.UserDao;
import com.pcwk.ehr.user.domain.Level;
import com.pcwk.ehr.user.domain.UserVO;
import com.pcwk.ehr.user.service.TestUserService;
import com.pcwk.ehr.user.service.UserService;;

@RunWith(SpringJUnit4ClassRunner.class) // 스프링 테스트 컨텍스트 프레임웍의 JUnit의 확장 기능 지정
@ContextConfiguration(locations = "/applicationContext.xml")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserServiceTest {
	final Logger LOG = LogManager.getLogger(UserDaoJUnitTest.class);
	
	@Resource(name = "dummyMailSender")
	MailSender mailSender;
	@Autowired
	PlatformTransactionManager transactionManager;
	@Autowired
	DataSource dataSource;
	@Autowired
	UserDao userDao;
	@Autowired
	UserService userService;
	List<UserVO> users;
	UserVO searchVO;

	@Before
	public void setUp() throws Exception {
		LOG.debug("**********************************************************");
		LOG.debug("=setUp=");
		// BASIC
		// BASIC -> SILVER(O)
		// SILVER
		// SILVER -> GOLD(O)
		// GOLD
		users = Arrays.asList(
				new UserVO("p103-01", "고송민-01", "1234_01", Level.BASIC, MIN_LOGIN_COUNT_FOR_SILVER - 1, 0,
						"thdals5123@naver.com", "사용하지 않음"),
				new UserVO("p103-02", "고송민-02", "1234_02", Level.BASIC, MIN_LOGIN_COUNT_FOR_SILVER, 2,
						"thdals5123@naver.com", "사용하지 않음"), // (O)
				new UserVO("p103-03", "고송민-03", "1234_03", Level.SILVER, MIN_LOGIN_COUNT_FOR_SILVER + 1,
						MIN_RECOMEND_COUNT_FOR_GOLD - 1, "thdals5123@naver.com", "사용하지 않음"),
				new UserVO("p103-04", "고송민-04", "1234_04", Level.SILVER, MIN_LOGIN_COUNT_FOR_SILVER + 2,
						MIN_RECOMEND_COUNT_FOR_GOLD, "thdals5123@naver.com", "사용하지 않음"), // (O)
				new UserVO("p103-05", "고송민-05", "1234_05", Level.GOLD, MIN_LOGIN_COUNT_FOR_SILVER + 5,
						MIN_RECOMEND_COUNT_FOR_GOLD + 5, "thdals5123@naver.com", "사용하지 않음"));
		searchVO = new UserVO();
		searchVO.setUserId("p103");
	}

	//@Ignore
	@Test
	public void upgradeAllOrNothing() throws SQLException {
		/**
		 * 5명 중 2명 등업 대상 4번째 사용자에서 강제 예외 발생 2번째(p4-02) 사용자 등급이 rollback 되면 성공
		 */
		TestUserService testUserService = new TestUserService(users.get(3).getUserId());
		// 수동으로 userDao DI
		testUserService.setUserDao(userDao);
		testUserService.setDataSource(dataSource);
		testUserService.setTransactionManager(transactionManager);
		LOG.debug("┌─────────────────────────┐");
		LOG.debug("│  upgradeAllOrNothing()  │");
		LOG.debug("└─────────────────────────┘");
		try {
			// 1. 전체 데이터 삭제
			for (UserVO vo : users) {
				userDao.del(vo);
			}
			assertEquals(0, userDao.getCount(searchVO));
			// 2. 추가
			for (UserVO vo : users) {
				userDao.add(vo);
			}
			assertEquals(5, userDao.getCount(searchVO));
			// 3. 등업(트랜잭션 : X)
			testUserService.upgradeLevels(searchVO);
		} catch (Exception e) {
			LOG.debug("┌─────────────┐");
			LOG.debug("│  Exception  │" + e.getMessage());
			LOG.debug("└─────────────┘");
		}
		// rollback 되지 않음 : BASIC 으로 돌아와야 하는데, SILVER임
		checkLevel(users.get(1), false);
	}

	@Ignore
	@Test
	public void add() throws SQLException {
		// 1. users 데이터 모두 삭제
		// 2. users 데이터 입력 : Level 없는 데이터 생성
		// 3. 추가
		// 4. 데이터 비교
		LOG.debug("┌───────────────────────────────────────────┐");
		LOG.debug("│                  add()                    │");
		LOG.debug("└───────────────────────────────────────────┘");
		// 1.
		for (UserVO vo : users) {
			userDao.del(vo);
		}
		assertEquals(0, userDao.getCount(searchVO));
		// 2.
		for (UserVO vo : users) {
			if (vo.getUserId().equals("p103-01")) {
				vo.setLevel(null);
			}
			userService.add(vo);
		}
		checkLevel(users.get(0), false);
	}

	//@Ignore
	@Test
	public void upgradeLevels() throws Exception {
		LOG.debug("┌────────────────────────────────────────────────────────┐");
		LOG.debug("│                     upgradeLevels()                    │");
		LOG.debug("└────────────────────────────────────────────────────────┘");
		// 1. users 데이터 모두 삭제
		// 2. users 데이터 입력
		// 3. 등업
		// 4. 등업 데이터 비교 p103-02(SILVER), p103-04(GOLD)
		// 1.
		for (UserVO vo : users) {
			userDao.del(vo);
		}
		assertEquals(0, userDao.getCount(searchVO));
		// 2.
		for (UserVO vo : users) {
			userDao.add(vo);
		}
		assertEquals(5, userDao.getCount(searchVO));
		// 3.
		userService.upgradeLevels(searchVO);
		checkLevel(users.get(0), false);
		checkLevel(users.get(1), true); // 등업 BASIC -> SILVER
		checkLevel(users.get(2), false);
		checkLevel(users.get(3), true); // 등업 SILVER -> GOLD
		checkLevel(users.get(4), false);
	}

	/**
	 * 
	 * @param userVO
	 * @param upgraded : true(등업 대상)/false
	 * @throws EmptyResultDataAccessException
	 * @throws SQLException
	 */
	private void checkLevel(UserVO userVO, boolean upgraded) throws EmptyResultDataAccessException, SQLException {
		UserVO userUpdate = userDao.get(userVO);
		LOG.debug("┌──────────────┐");
		LOG.debug("│  checkLevel  │");
		LOG.debug("└──────────────┘");
		if (upgraded == true) {
			LOG.debug(userUpdate.getLevel() + "==" + userVO.getLevel().nextLevel());
			assertEquals(userUpdate.getLevel(), userVO.getLevel().nextLevel());
		} else {
			assertEquals(userUpdate.getLevel(), userVO.getLevel());
		}
	}

	@Test
	public void beans() {
		LOG.debug("===============");
		LOG.debug("=beans()=");
		LOG.debug("=mailSender=" + mailSender);
		LOG.debug("=transactionManager=" + transactionManager);
		LOG.debug("=dataSource=" + dataSource);
		LOG.debug("=userService=" + userService);
		LOG.debug("===============");
		assertNotNull(mailSender);
		assertNotNull(transactionManager);
		assertNotNull(userDao);
		assertNotNull(userService);
		assertNotNull(dataSource);
	}
}