package com.pcwk.ehr.user.service;

import java.sql.SQLException;
import java.util.List;

import com.pcwk.ehr.user.dao.UserDao;
import com.pcwk.ehr.user.domain.Level;
import com.pcwk.ehr.user.domain.UserVO;

public class UserServiceImpl implements UserService {
	
	public static final int MIN_LOGIN_COUNT_FOR_SILVER = 50;
	public static final int MIN_RECOMEND_COUNT_FOR_GOLD = 30;
	
	private UserDao  userDao;
	
	public UserServiceImpl() {}
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	/**
	 * 업그레이드 가능 여부 확인
	 * @param inVO
	 * @return true(등업 대상)/false
	 */
	private boolean canUpgradeLevel(UserVO inVO) {
		Level currentLevel = inVO.getLevel();
		
		switch(currentLevel) {
			case BASIC: return (inVO.getLogin() >= MIN_LOGIN_COUNT_FOR_SILVER);
			case SILVER: return (inVO.getRecommend() >=MIN_RECOMEND_COUNT_FOR_GOLD);
			case GOLD: return false;
			default: throw new IllegalArgumentException("Unknown Level:"+currentLevel);
		}
	}
	
	
	@Override
	public void upgradeLevels(UserVO inVO) throws SQLException {
		//등업 조건
		//1. 전체 사용자 대상
		//2. 등업 대상
		//2.1. 사용자가 처음 가입 하면 BASIC
		//2.2. 가입후 50회 이상 로그인 하면 SILVER
		//2.3. 레벨이 SILVER이고 30번 이상 추천 받으면 GOLD
		//(사용자 레벨을 한달에 한번 일괄처리:트랜잭션)
		List<UserVO> allUser = userDao.getAll(inVO);
		
		//2.
		for(UserVO vo  :allUser) {
			boolean  changeLevel = false;//등업 대상 선정
			
			//등업 대상 선정
			if(canUpgradeLevel(vo)==true) {
				//등업
				upgradeLevel(vo);  
			}
		}
	}

	//레벨 업그레이드 
	protected void upgradeLevel(UserVO inVO) throws SQLException {
		inVO.upgradeLevel();
		userDao.update(inVO);
	}
	
	
	
	@Override
	public void add(UserVO inVO) throws SQLException {
		if(null==inVO.getLevel()) {
			inVO.setLevel(Level.BASIC);
		}
		
		userDao.add(inVO);
		
	}

}









