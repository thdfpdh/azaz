package com.pcwk.ehr.user.controller;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.pcwk.ehr.cmn.DTO;
import com.pcwk.ehr.cmn.MessageVO;
import com.pcwk.ehr.user.domain.UserVO;
import com.pcwk.ehr.user.service.UserService;

@Controller
@RequestMapping("user")
public class UserController {

	final Logger LOG = LogManager.getLogger(getClass());

	@Autowired
	UserService userService;
	
	// 목록 조회
	// http://localhost:8080/ehr/user/doRetrieve.do?searchDiv=10&searchWord=점심시간
	@RequestMapping(value = "/doRetrieve.do", method = RequestMethod.GET)
	public String doRetrieve(DTO searchVO, HttpServletRequest req, Model model) throws SQLException {
		String view = "user/user_list";
		LOG.debug("┌───────────────────┐");
		LOG.debug("┃  doRetrieve()   │ DTO: " + searchVO);
		LOG.debug("└───────────────────┘");
		
		String searchDiv = req.getParameter("searchDiv");
		String searchWord = req.getParameter("searchWord");
		
		LOG.debug("searchDiv: " + searchDiv);
		LOG.debug("searchWord: " + searchWord);
		
		return view;
	}
	
	// 수정
	@RequestMapping(value = "/doUpdate.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String doUpdate(UserVO inVO) throws SQLException {
		String jsonString = "";
		LOG.debug("┌───────────────────┐");
		LOG.debug("┃  doUpdate()   │ inVO: " + inVO);
		LOG.debug("└───────────────────┘");
		
		int flag = this.userService.doUpdate(inVO);
		
		String message = "";
		if(flag == 1) message = inVO.getId() + "가 수정 되었습니다.";
		else message = inVO.getId() + "수정 실패";
		
		MessageVO messageVO = new MessageVO(flag + "", message);
		jsonString = new Gson().toJson(messageVO);
		LOG.debug("jsonString: " + jsonString);
		
		return jsonString;
	}

	// 조회
	@RequestMapping(value = "/doSelectOne.do", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String doSelectOne(UserVO inVO, HttpServletRequest req) throws SQLException, EmptyResultDataAccessException {
		String jsonString = "";
		LOG.debug("┌───────────────────┐");
		LOG.debug("┃  doSelectOne()   │ inVO: " + inVO);
		LOG.debug("└───────────────────┘");
		
		String userId = req.getParameter("userId");
		LOG.debug("userId: " + userId);
		
		UserVO outVO = this.userService.doSelectOne(inVO);
		LOG.debug("outVO: " + outVO);
		
		String message = "";
		if(outVO != null) {
			message = inVO.getId() + "가 조회 되었습니다.";
			jsonString = new Gson().toJson(outVO);
		}
		else {
			message = inVO.getId() + "조회 실패";
			jsonString = new Gson().toJson(new MessageVO("0", message));
		}
		
		LOG.debug("jsonString: " + jsonString);

		return jsonString;
	}

	// 삭제
	@RequestMapping(value = "/doDelete.do", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String doDelete(UserVO inVO, HttpServletRequest req) throws SQLException {

		String jsonString = "";
		LOG.debug("┌───────────────────┐");
		LOG.debug("┃  doDelete()   │ inVO: " + inVO);
		LOG.debug("└───────────────────┘");

		String userId = req.getParameter("userId");
		LOG.debug("userId: " + userId);

		int flag = userService.doDelete(inVO);
		String message = "";

		if (flag == 1)
			message = inVO.getId() + "가 삭제 되었습니다.";
		else
			message = inVO.getId() + "삭제 실패";

		MessageVO messageVO = new MessageVO(String.valueOf(flag), message);
		jsonString = new Gson().toJson(messageVO);

		LOG.debug("jsonString: " + jsonString);

		return jsonString;
	}

	// 등록
	@RequestMapping(value = "/doSave.do", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody // HTTP 요청 부분의 body 부분이 그대로 브라우저에 전달
	public String doSave(UserVO inVO) throws SQLException {
		String jsonString = "";

		LOG.debug("┌───────────────────┐");
		LOG.debug("┃  doSave()     │ inVO: " + inVO);
		LOG.debug("└───────────────────┘");

		int flag = userService.doSave(inVO);
		String message = "";

		if (flag == 1)
			message = inVO.getId() + "가 등록 되었습니다.";
		else
			message = inVO.getId() + "등록 실패";

		MessageVO messageVO = new MessageVO(flag + "", message);
		jsonString = new Gson().toJson(messageVO);
		LOG.debug("jsonString: " + jsonString);

		return jsonString;
	}
}
