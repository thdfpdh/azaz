package com.pcwk.ehr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.pcwk.ehr.user.dao.UserDao;
import com.pcwk.ehr.user.domain.Level;
import com.pcwk.ehr.user.domain.UserVO;
import com.pcwk.ehr.user.service.TestUserService;
import com.pcwk.ehr.user.service.UserService;

import static com.pcwk.ehr.user.service.UserServiceImpl.MIN_LOGIN_COUNT_FOR_SILVER;
import static com.pcwk.ehr.user.service.UserServiceImpl.MIN_RECOMEND_COUNT_FOR_GOLD;;

@RunWith(SpringJUnit4ClassRunner.class) // 스프링 테스트 컨텍스트 프레임워크의 JUnit의 확장기능 지정
@ContextConfiguration(locations = "/applicationContext.xml")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class UserServiceTest {
	final Logger LOG = LogManager.getLogger(UserDaoJUnitTest.class);

	@Autowired
	DataSource  dataSource;
	
	@Autowired
	UserDao userDao;

	@Autowired
	UserService userService;

	List<UserVO> users;
	UserVO searchVO;

	@Before
	public void setUp() throws Exception {
		LOG.debug("*************************************************************");
		LOG.debug("=setUp()=");

		// 데이터를 만든다.
		// BASIC
		// BASIC -> SILVER(등업대상)
		// SILVER
		// SILVER -> GOLD(등업대상)
		// GOLD

		users = Arrays.asList(

				new UserVO("P103-01", "고송민_01", "4321_1", Level.BASIC , MIN_LOGIN_COUNT_FOR_SILVER-1, 0                            , "thdals5123@naver.com", "사용하지 않음"),
				new UserVO("P103-02", "고송민_02", "4321_2", Level.BASIC , MIN_LOGIN_COUNT_FOR_SILVER  , 2                            , "thdals5123@naver.com", "사용하지 않음"), // 등업대상
				new UserVO("P103-03", "고송민_03", "4321_3", Level.SILVER, MIN_LOGIN_COUNT_FOR_SILVER+1, MIN_RECOMEND_COUNT_FOR_GOLD-1, "thdals5123@naver.com", "사용하지 않음"),
				new UserVO("P103-04", "고송민_04", "4321_4", Level.SILVER, MIN_LOGIN_COUNT_FOR_SILVER+2, MIN_RECOMEND_COUNT_FOR_GOLD  , "thdals5123@naver.com", "사용하지 않음"), // 등업대상
				new UserVO("P103-05", "고송민_05", "4321_5", Level.GOLD  , MIN_LOGIN_COUNT_FOR_SILVER+5, MIN_RECOMEND_COUNT_FOR_GOLD+5, "thdals5123@naver.com", "사용하지 않음")
				);

		searchVO = new UserVO();
		searchVO.setUserId("P103-");
	}
	
	
	@Test
	public void upgradeAllOrNothing()throws SQLException,EmptyResultDataAccessException{
		/*
		 * 5명중 2명이 등업 대상
		 * 4번제 사용자에서 강제 예외 발생
		 * 2번째(P103-02) 사용자 등급이 rollback되면 성공
		 */
		
		TestUserService testUserService=new TestUserService(users.get(3).getUserId());
		//수동으로 userDao DI
		testUserService.setUserDao(userDao);
		testUserService.setDataSource(dataSource);
		//testUserService.set
		LOG.debug("-------------------------------------------------------------------------------------");
		LOG.debug("=upgradeAllOrNothing()=");
			
		try {//1.전체 데이터 삭제
			for(UserVO vo :users) {
				userDao.del(vo);
			}
			//assertEquals(0, userDao.get(searchVO));
			
			//2.추가
			for(UserVO vo:users) {
				userDao.add(vo);
			}
			//assertEquals(5, userDao.get(searchVO));
			
			//3. 등업(트랜잭션:X)
			testUserService.upgradeLevels(searchVO);
			
			
		}catch(Exception e) {
			LOG.debug("-------------------------------------------------------------------------------------");
			LOG.debug("Exception="+e.getLocalizedMessage());
			
		}
			//rollback안됨 : BASIC으로 돌아와야 하는데 그렇지 않음(SILVER)
			checkLevel(users.get(1), false);
		
	}

	@Ignore
	@Test
	public void add() throws SQLException {
		// 1.users 데이터 모두 삭제
		// 2.users 데이터 입력: Level이 없는 데이터 생성
		// 3.추가
		// 4.데이터 비교
		LOG.debug("-------------------------------------------------------------------------------------");
		LOG.debug("=add()=");

		// 1.
		for (UserVO vo : users) {
			userDao.del(vo);
		}
		assertEquals(0, userDao.getCount(searchVO));

		// 2.users 데이터 입력: Level이 없는 데이터 생성
		for (UserVO vo : users) {

			if (vo.getUserId().equals("P103-01")) {
				vo.setLevel(null);
			}
			userService.add(vo);

		}

		checkLevel(users.get(0), false);

	}

	@Ignore
	@Test
	public void upgradeLevels() throws SQLException {
		LOG.debug("-------------------------------------------------------------------------------------");
		LOG.debug("=upgradeLevels()=");

		// 1.users 데이터 모두삭제
		// 2.users 데이터 입력
		// 3.등업
		// 4.등업 데이터 비교 P103-02(SILVER),P103-04(GOLD)

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
		checkLevel(users.get(1), true);
		checkLevel(users.get(2), false);
		checkLevel(users.get(3), true);
		checkLevel(users.get(4), false);
	}

	/**
	 * @param userVO
	 * @param upgraded :true(등업대상)/false
	 * @throws EmptyResultDataAccessException      
	 * @throws SQLException
	 */
	private void checkLevel(UserVO userVO, boolean upgraded) throws EmptyResultDataAccessException, SQLException {
		UserVO userUpdate = userDao.get(userVO);

		LOG.debug("checkLevel");

		if (upgraded == true) {//등업대상
			LOG.debug(userUpdate.getLevel()+"==", userVO.getLevel().nextLevel());
			assertEquals(userUpdate.getLevel(), userVO.getLevel().nextLevel());
		} else {
			assertEquals(userUpdate.getLevel(), userVO.getLevel());
		}
 
	}
	
	@Ignore
	@Test
	public void test() {
		LOG.debug("=======================");
		LOG.debug("====beans()=");
		LOG.debug("=======================");
		LOG.debug("userService=" + userService);
		LOG.debug("=======================");
		assertNotNull(userDao);
		assertNotNull(userService);
	}

}