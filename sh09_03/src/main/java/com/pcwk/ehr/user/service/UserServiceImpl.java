package com.pcwk.ehr.user.service;

import java.sql.SQLException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.pcwk.ehr.user.dao.UserDao;
import com.pcwk.ehr.user.domain.Level;
import com.pcwk.ehr.user.domain.UserVO;

public class UserServiceImpl implements UserService {

	final Logger LOG = LogManager.getLogger(UserServiceImpl.class);
	public static final int MIN_LOGIN_COUNT_FOR_SILVER = 50;
	public static final int MIN_RECOMEND_COUNT_FOR_GOLD = 30;

	private UserDao userDao;
	private DataSource dataSource;
	private PlatformTransactionManager transactionManager; // 트랜잭션 추상화 인터페이스
	private MailSender mailSender;

	public UserServiceImpl() {
	}

	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * 업그레이드 가능 여부 확인
	 * 
	 * @param inVO
	 * @return
	 */
	private Boolean canUpgradeLevel(UserVO inVO) {
		Level currentLevel = inVO.getLevel();

		switch (currentLevel) {
		case BASIC:
			return (inVO.getLogin() >= MIN_LOGIN_COUNT_FOR_SILVER);
		case SILVER:
			return (inVO.getRecommend() >= MIN_RECOMEND_COUNT_FOR_GOLD);
		case GOLD:
			return false;
		default:
			throw new IllegalArgumentException("Unkown Level:" + currentLevel);
		}
	}

	@Override
	public void upgradeLevels(UserVO inVO) throws Exception {

		TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

		try {

			// 1.
			List<UserVO> allUser = userDao.getAll(inVO);

			// 2.
			for (UserVO vo : allUser) {

				boolean changeLevel = false; // 등업 대상 선정

				// 등업 대상 선정
				if (canUpgradeLevel(vo) == true) {
					// 등업
					upgradeLevel(vo);
				}
			}
			transactionManager.commit(status); // 정상완료시 커밋수행

		} catch (Exception e) {
			LOG.debug("==========================================");
			LOG.debug("upgradeAllOrNothing()" + e.getMessage());
			LOG.debug("==========================================");
			transactionManager.rollback(status);// 예외발생시 롤백
			throw e;
		}
	}

	// 레벨 업그레이드
	protected void upgradeLevel(UserVO inVO) throws SQLException {
		inVO.upgradeLevel();
		userDao.update(inVO);

		sendUpgradeEmail(inVO);
	}

	// 등업된 사용자에게 email전송
	private void sendUpgradeEmail(UserVO inVO) {
		LOG.debug("==========================================");
		LOG.debug("sendUpgradEmail()");
		LOG.debug("==========================================");

		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("thdals5123@naver.com");
		message.setFrom(inVO.getEmail());
		message.setSubject("등업안내!");
		message.setText("사용자등급이"+inVO.getLevel().name()+"로 등업 되었습니다.");
		
		mailSender.send(message);
		

//		MimeMessage mail = mailSender.createMimeMessage();
//		MimeMessageHelper mailHelper = new MimeMessageHelper(mail, "UTF-8");
//
//		try {
//			// 보내는사람
//			mailHelper.setFrom("thdals5123@naver.com");
//
//			// 받는사람
//			mailHelper.setTo(inVO.getEmail());
//
//			// 제목
//			mailHelper.setSubject("등업 안내");
//
//			// 내용
//			mailHelper.setText("사용자 등급이" + inVO.getLevel().name() + "로 등업 되었습니다.");
//
//			// mail전송
//			mailSender.send(mail);
//
//		} catch (MessagingException e) {
//		LOG.debug("==========================================");
//		LOG.debug("MessagingException" + e.getMessage());
//		LOG.debug("==========================================");
//		e.printStackTrace();
//	}LOG.debug("==========================================");LOG.debug("mail전송 성공"+inVO.getEmail());LOG.debug("==========================================");
//
	}

	@Override
	public void add(UserVO inVO) throws SQLException {
		if (null == inVO.getLevel()) {
			inVO.setLevel(Level.BASIC);
		}

		userDao.add(inVO);

	}

}