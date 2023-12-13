package com.pcwk.ehr.user.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.pcwk.ehr.user.dao.UserDao;
import com.pcwk.ehr.user.domain.Level;
import com.pcwk.ehr.user.domain.UserVO;

@Service("userServiceImpl")
public class UserServiceImpl implements UserService {

	final Logger LOG = LogManager.getLogger(UserServiceImpl.class);

//	@Resource(name = "dummyMailSender")
//	private MailSender mailSender;

//	public static final int MIN_LOGIN_COUNT_FOR_SILVER = 50;
//	public static final int MIN_RECOMMEND_COUNT_FOR_GOLD = 30;
	
	@Autowired
	private UserDao userDao;

	public UserServiceImpl() {
	}

//	public void setMailSender(MailSender mailSender) {
//		this.mailSender = mailSender;
//	}

	// 등업된 사용자에게 email 전송
//	private void sendUpgradeEmail(UserVO inVO) {
//		LOG.debug("┌───────────────────┐");
//		LOG.debug("┃  sendUpgradeEmail()                       │");
//		LOG.debug("└───────────────────┘");
//		
//		SimpleMailMessage message = new SimpleMailMessage();
//		message.setFrom("conchat");
//		message.setTo(inVO.getEmail());
//		message.setSubject("등업 안내");
//		message.setText("사용자 등급이 \" + inVO.getLevel().name() + \"로 상승했습니다.");
//		mailSender.send(message);
//
////		MimeMessage mail = mailSender.createMimeMessage();
////		MimeMessageHelper mailHelper = new MimeMessageHelper(mail, "UTF-8");
////
////		try {
////			// 보내는 사람
////			mailHelper.setFrom("kjiwoo999@naver.com");
////			// 받는 사람
////			mailHelper.setTo(inVO.getEmail());
////			// 제목
////			mailHelper.setSubject("등업 안내 메일");
////			// 내용
////			mailHelper.setText("사용자 등급이 " + inVO.getLevel().name() + "로 상승했습니다.");
////			// 메일 전송
////			mailSender.send(mail);
////
////		} catch (MessagingException e) {
////			LOG.debug("┌───────────────────┐");
////			LOG.debug("┃  sendUpgradeEmail()                       │" + e.getMessage());
////			LOG.debug("└───────────────────┘");
////			e.printStackTrace();
////		}
//		LOG.debug("┌───────────────────┐");
//		LOG.debug("┃  메일 전송 완료                               │" + inVO.getEmail());
//		LOG.debug("└───────────────────┘");
//
//	}

	// 업그레이드 가능 여부 확인
//	private boolean canUpgradeLevel(UserVO inVO) {
//		Level currentLevel = inVO.getLevel();
//
//		switch (currentLevel) {
//		case BASIC:
//			return inVO.getLogin() >= MIN_LOGIN_COUNT_FOR_SILVER;
//		case SILVER:
//			return inVO.getRecommend() >= MIN_RECOMMEND_COUNT_FOR_GOLD;
//		case GOLD:
//			return false;
//		default:
//			throw new IllegalArgumentException("unknown level: " + currentLevel);
//		}
//	}

//	@Override
//	public void upgradeLevels(UserVO inVO) throws Exception {
//
//		try {
//
//			List<UserVO> allUser = userDao.getAll(inVO);
//
//			// 2.
//			for (UserVO vo : allUser) {
//				boolean changeLevel = false;// 등업 대상 선정
//
//				// 등업 대상 선정
//				if (canUpgradeLevel(vo) == true) {
//					// 등업
//					upgradeLevel(vo);
//				}
//			}
//
//		} catch (Exception e) {
//			LOG.debug("┌─────────────────────────────────────────────────────────┐");
//			LOG.debug("│ upgradeAllOrNothing()                                   │"+e.getMessage());
//			LOG.debug("└─────────────────────────────────────────────────────────┘");
//
//			throw e;
//		}
//
//	}

	protected void upgradeLevel(UserVO inVO) throws SQLException { // protected: 상속관계 오버라이딩 가능
		userDao.doUpdate(inVO);

//		sendUpgradeEmail(inVO);
	}

	@Override
	public void add(UserVO inVO) throws SQLException {
		userDao.doSave(inVO);
	}

	@Override
	public List<UserVO> getAll(UserVO inVO) throws SQLException {
		return userDao.getAll(inVO);
	}

	@Override
	public int doUpdate(UserVO inVO) throws SQLException {
		return userDao.doUpdate(inVO);
	}

	@Override
	public int getCount(UserVO inVO) throws SQLException {
		return userDao.getCount(inVO);
	}

	@Override
	public int doDelete(UserVO inVO) throws SQLException {
		return userDao.doDelete(inVO);
	}

	@Override
	public UserVO doSelectOne(UserVO inVO) throws SQLException, EmptyResultDataAccessException {
		return userDao.doSelectOne(inVO);
	}

	@Override
	public List<UserVO> doRetrieve(UserVO inVO) throws SQLException {
		return userDao.doRetrieve(inVO);
	}

	@Override
	public int doSave(UserVO inVO) throws SQLException {
		return userDao.doSave(inVO);
	}

}
