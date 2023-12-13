package com.pcwk.ehr;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.pcwk.ehr.user.dao.UserDao;
import com.pcwk.ehr.user.dao.UserDaoImpl;
import com.pcwk.ehr.user.domain.Level;
import com.pcwk.ehr.user.domain.UserVO;

@RunWith(SpringJUnit4ClassRunner.class) //스프링 테스트 컨텍스트 프레임웤그의 JUnit의 확장기능 지정
@ContextConfiguration(locations = "/applicationContext.xml")
public class UserDaoJUnitTest {

	final Logger LOG = LogManager.getLogger(UserDaoJUnitTest.class);

	UserDao dao;
	// 등록
	UserVO userVO01;
	UserVO userVO02;
	UserVO userVO03;

	// getCount에 사용
	UserVO searchVO;
	
	@Autowired  //테스트 오브젝트가 만들어지고 나면 스프링 테스트 컨텍스트에 자동으로 객체값으로 주입
	ApplicationContext context;

	@Before
	public void setUp() throws Exception {
		/// sh03_01/src/main/java/applicationContext.xml
		//context = new GenericXmlApplicationContext("applicationContext.xml");
		dao = context.getBean("userDao", UserDaoImpl.class);

		// 등록
		//public UserVO(String userId, String name, String password, Level level, int login, int recommend, String email,
		//String reg_dt) {
		userVO01 = new UserVO("p99-01", "이상무99_01", "4321_1",Level.BASIC,1,0,"jamesol@paran.com","사용하지 않음");
		userVO02 = new UserVO("p99-02", "이상무99_02", "4321_2",Level.SILVER,50,2,"jamesol@paran.com","사용하지 않음");
		userVO03 = new UserVO("p99-03", "이상무99_03", "4321_3",Level.GOLD,100,31,"jamesol@paran.com","사용하지 않음");
 
		// getCount에 사용
		searchVO = new UserVO();
		searchVO.setUserId("p99");
		LOG.debug("====================");
		LOG.debug("=context=" + context);
		LOG.debug("=dao=" + dao);
		LOG.debug("====================");
	}

	@After
	public void tearDown() throws Exception {
		LOG.debug("=tearDown==========================");
	}

	@Test
	public void getAll()throws SQLException{
		//1.데이터 삭제
		//2.데이터 입력
		//3.건수확인
		//4.getAll()
		//5.3건 
		//6.데이터 비교
		LOG.debug("====================");
		LOG.debug("=getAll()=");
		LOG.debug("====================");		
		
		//1.
		dao.del(userVO01);
		dao.del(userVO02);
		dao.del(userVO03);
		
		assertEquals(0,dao.getCount(searchVO));
	
		//2.
		int flag = dao.add(userVO01);
		//3
		assertEquals(1, flag);
		assertEquals(1,dao.getCount(searchVO));
		
		flag = dao.add(userVO02);
		assertEquals(1, flag);
		assertEquals(2,dao.getCount(searchVO));
		
		flag = dao.add(userVO03);
		assertEquals(1, flag);
		assertEquals(3,dao.getCount(searchVO));		
		
		
		//4.
		List<UserVO> list = dao.getAll(searchVO);
		
		//5
		assertEquals(3, list.size());
		
		for(UserVO vo   :list) {
			LOG.debug(vo);
		}
		
		isSameUser(userVO01, list.get(0));
		isSameUser(userVO02, list.get(1));
		isSameUser(userVO03, list.get(2));
	}
	
	
	@Ignore
	@Test
	public void update() throws SQLException {
		//1.데이터 삭제
		//2.데이터 입력
		//3.등록데이터 조회
		//4.3번 조회된 데이터를 수정
		//5.update
		//6.수정데이터 조회
		//7.비교
		
		LOG.debug("====================");
		LOG.debug("=update()=");
		LOG.debug("====================");		
		
		//1.
		dao.del(userVO01);
		dao.del(userVO02);
		dao.del(userVO03);
		
		assertEquals(0, dao.getCount(searchVO));
		
		//2.
		dao.add(userVO01);
		assertEquals(1, dao.getCount(searchVO));
		dao.add(userVO02);
		dao.add(userVO03);
		
		//3.
		UserVO getVO = dao.get(userVO01);
		isSameUser(getVO, userVO01);
		
		//4
		String upStr = "_U";
		int    upInt = 10;
		
		getVO.setName(getVO.getName()+upStr);//이상무99_01+"_U"
		getVO.setPassword(getVO.getPassword()+upStr);
		
		getVO.setLevel(Level.SILVER);
		
		getVO.setLogin(getVO.getLogin()+upInt);
		getVO.setRecommend(getVO.getRecommend()+upInt);
		getVO.setEmail(getVO.getEmail()+upStr);
		
		//5.update
		int flag = dao.update(getVO);
		assertEquals(1, flag);
		
		//6.
		UserVO vsVO = dao.get(getVO);
		
		//7.
		isSameUser(vsVO, getVO);
		
		
	}
	
	//setUp() 
	//getFailure()
	//tearDown()
	//expected=예외, 예외가 발생하면 성공
	@Ignore
	@Test(expected = EmptyResultDataAccessException.class)
	public void getFailure() throws SQLException {
		LOG.debug("====================");
		LOG.debug("=getFailure=");
		LOG.debug("====================");
		// 1. 데이터 삭제
		// 2. 한건조회

		// 1.
		dao.del(userVO01);
		dao.del(userVO02);
		dao.del(userVO03);

		userVO01.setUserId("unknown id");
		
		//2.
		dao.get(userVO01);
		
	}

	//@Ignore  //테스트 disable
	@Test(timeout = 3000) // long 1/1000초
	public void addAndGet() throws SQLException {
		// 1. 데이터 삭제
		// 2. 등록
		// 3. 한건조회

		// 1.
		dao.del(userVO01);
		dao.del(userVO02);
		dao.del(userVO03);

		// 2.
		int flag = dao.add(userVO01);
		int count = dao.getCount(searchVO);
		assertThat(flag, is(1));
		assertThat(count, is(1));

		// UserVO02등록
		flag = dao.add(userVO02);
		count = dao.getCount(searchVO);
		assertThat(flag, is(1));
		assertThat(count, is(2));

		// UserVO03등록
		flag = dao.add(userVO03);
		count = dao.getCount(searchVO);
		assertThat(flag, is(1));
		assertThat(count, is(3));

		UserVO outVO01 = dao.get(userVO01);
		UserVO outVO02 = dao.get(userVO02);
		UserVO outVO03 = dao.get(userVO03);
		assertNotNull(outVO01);// Not Null이면 true
		assertNotNull(outVO02);// Not Null이면 true
		assertNotNull(outVO03);// Not Null이면 true

		// 데이터 동일 테스트
		isSameUser(userVO01, outVO01);
		isSameUser(userVO02, outVO02);
		isSameUser(userVO03, outVO03);
	}

	private void isSameUser(UserVO userVO, UserVO outVO) {
		assertEquals(userVO.getUserId(), outVO.getUserId());
		assertEquals(userVO.getName(), outVO.getName());
		assertEquals(userVO.getPassword(), outVO.getPassword());
		
		assertEquals(userVO.getLevel(), outVO.getLevel());//등급
		assertEquals(userVO.getLogin(), outVO.getLogin());//로그인 횟수
		assertEquals(userVO.getRecommend(), outVO.getRecommend());//추천수
		assertEquals(userVO.getEmail(), outVO.getEmail());//email

	}

}
