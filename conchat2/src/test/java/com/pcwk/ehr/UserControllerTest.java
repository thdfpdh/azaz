package com.pcwk.ehr;

//import static com.pcwk.ehr.user.service.UserServiceImpl.MIN_LOGIN_COUNT_FOR_SILVER;
//import static com.pcwk.ehr.user.service.UserServiceImpl.MIN_RECOMMEND_COUNT_FOR_GOLD;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.pcwk.ehr.cmn.MessageVO;
import com.pcwk.ehr.user.domain.Level;
import com.pcwk.ehr.user.domain.UserVO;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class) // 스프링 테스트 컨텍스트 프레임워크의 JUnit 확장기능 지정
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserControllerTest {

	final Logger LOG = LogManager.getLogger(getClass());

	@Autowired
	WebApplicationContext webApplicationContext;

	// 브라우저 대역
	MockMvc mockMvc;

	List<UserVO> users;

	UserVO searchVO;

	@Before
	public void setUp() throws Exception {
		LOG.debug("======================================");
		LOG.debug("setUp()");

		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

		users = Arrays.asList(
				new UserVO("김지우-1", 123456, "1111-1", "1111", "abcd1@abcd.com", "0", 1, 1),
				new UserVO("김지우-2", 123456, "1111-2", "1111", "abcd2@abcd.com", "0", 1, 1),
				new UserVO("김지우-3", 123456, "1111-3", "1111", "abcd3@abcd.com", "0", 1, 1),
				new UserVO("김지우-4", 123456, "1111-4", "1111", "abcd4@abcd.com", "0", 1, 1),
				new UserVO("김지우-5", 123456, "1111-5", "1111", "abcd5@abcd.com", "0", 1, 1));

		searchVO = new UserVO();
		searchVO.setId("김지우-");

	}
	
	private void isSameUser(UserVO userVO, UserVO outVO) {
		assertEquals(userVO.getNm(), outVO.getNm());
		assertEquals(userVO.getBirth(), outVO.getBirth());
		assertEquals(userVO.getId(), outVO.getId());
		assertEquals(userVO.getPw(), outVO.getPw());
		assertEquals(userVO.getEmail(), outVO.getEmail());
//		assertEquals(userVO.getReg_ymd(), outVO.getReg_ymd());
		assertEquals(userVO.getChat(), outVO.getChat());
		assertEquals(userVO.getAct(), outVO.getAct());
	}
	
//	@Ignore
	@Test
	public void addAndGet() throws Exception {
		
		doDelete(users.get(0));
		doDelete(users.get(1));
		doDelete(users.get(2));
		doDelete(users.get(3));
		doDelete(users.get(4));
		
		doSave(users.get(0));
		doSave(users.get(1));
		doSave(users.get(2));
		doSave(users.get(3));
		doSave(users.get(4));
		
		isSameUser(users.get(0), doSelectOne(users.get(0)));
		isSameUser(users.get(1), doSelectOne(users.get(1)));
		isSameUser(users.get(2), doSelectOne(users.get(2)));
		isSameUser(users.get(3), doSelectOne(users.get(3)));
		isSameUser(users.get(4), doSelectOne(users.get(4)));
		
	}
	
	@Ignore
	@Test
	public void doUpdate() throws Exception {
		LOG.debug("======================================");
		LOG.debug("doUpdate()");
		
		UserVO inVO = users.get(0);
		String upStr = "_U";
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/user/doUpdate.do")
				.param("chat", inVO.getChat() + "").param("act", inVO.getAct() + "").param("tier", inVO.getTier() + "")
				.param("click", inVO.getClick() + "").param("port", inVO.getPort() + "")	.param("pop_scr", inVO.getPop_scr() + "");
		
		ResultActions resultActions = this.mockMvc.perform(requestBuilder).andExpect(status().isOk());

		String result = resultActions.andDo(print()).andReturn().getResponse().getContentAsString();

		LOG.debug("##########################");
		LOG.debug(result);
		
		MessageVO messageVO = new Gson().fromJson(result, MessageVO.class);
		assertEquals(String.valueOf(1), messageVO.getMsgId());
		LOG.debug("messageVO: " + messageVO);
	}
	
	public UserVO doSelectOne(UserVO inVO) throws Exception {
		LOG.debug("======================================");
		LOG.debug("doSelectOne()");
		
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/user/doSelectOne.do")
				.param("id", inVO.getId());
		
		ResultActions resultActions = this.mockMvc.perform(requestBuilder).andExpect(status().isOk());
		
		String result = resultActions.andDo(print()).andReturn().getResponse().getContentAsString();
		LOG.debug("##########################");
		LOG.debug(result);
		
		UserVO outVO = new Gson().fromJson(result, UserVO.class);
		LOG.debug("##########################");
		LOG.debug("outVO: " + outVO);
		assertNotNull(outVO);
		
		return outVO;
	}
	
	public void doDelete(UserVO inVO) throws Exception {
		LOG.debug("======================================");
		LOG.debug("doDelete()");
		
		// URL + 호출방식(get) + param(userId)
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/user/doDelete.do")
				.param("id", inVO.getId());
		
		ResultActions resultActions = this.mockMvc.perform(requestBuilder).andExpect(status().isOk());
		
		String result = resultActions.andDo(print()).andReturn().getResponse().getContentAsString();
		LOG.debug("##########################");
		LOG.debug(result);
		
		MessageVO messageVO = new Gson().fromJson(result, MessageVO.class);
//		assertEquals(String.valueOf(1), messageVO.getMsgId());
		LOG.debug("messageVO: " + messageVO);
	}

	public void doSave(UserVO inVO) throws Exception {
		LOG.debug("======================================");
		LOG.debug("doSave()");

		// MockHttpServletRequestBuilder: param 데이터 저장
		// MockMvc: 호출
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/user/doSave.do")
				.param("nm", inVO.getNm()).param("birth", inVO.getBirth() + "").param("id", inVO.getId())
				.param("pw", inVO.getPw() + "").param("email", inVO.getEmail() + "")
				.param("reg_ymd", inVO.getReg_ymd() + "").param("chat", inVO.getChat() + "").param("act", inVO.getAct() + "");

		ResultActions resultActions = this.mockMvc.perform(requestBuilder).andExpect(status().isOk());

		String result = resultActions.andDo(print()).andReturn().getResponse().getContentAsString();

		LOG.debug("##########################");
		LOG.debug(result);
		
		MessageVO messageVO = new Gson().fromJson(result, MessageVO.class);
		assertEquals(String.valueOf(1), messageVO.getMsgId());
		LOG.debug("messageVO: " + messageVO);
	}

	@Test
	public void beans() {
		LOG.debug("======================================");
		LOG.debug("beans()");
		LOG.debug("webApplicationContext: " + webApplicationContext);
		LOG.debug("mockMvc: " + mockMvc);

		assertNotNull(webApplicationContext);
		assertNotNull(mockMvc);
	}

}
