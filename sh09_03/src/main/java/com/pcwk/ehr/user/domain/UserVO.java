package com.pcwk.ehr.user.domain;

import com.pcwk.ehr.cmn.DTO;

public class UserVO extends DTO{
	//ctrl+shift+y : 소문자로
	//ctrl+shift+y : 대문자로
	private String userId    ;//아이디
	private String name       ;//이름
	private String password   ;//비번
	
	//2023.12.01 추가
	private Level level        ;//등급
    private int login          ;//로그인 횟수
    private int recommend      ;//추천
    private String email       ;//email
    private String regDt      ;//등록일
	
     

	public UserVO() {
	}

	

	public UserVO(String userId, String name, String password, Level level, int login, int recommend, String email,
			String reg_dt) {
		super();
		this.userId = userId;
		this.name = name;
		this.password = password;
		this.level = level;
		this.login = login;
		this.recommend = recommend;
		this.email = email;
		this.regDt = reg_dt;
	}

	//UserVO의 업그레이드
	public void upgradeLevel() {
		Level nexLevel = this.level.nextLevel();
		System.out.println(level);
		if(null ==nexLevel) {
			throw new IllegalArgumentException(this.level+"은 없그레이드 불가");
		}else {
			level = nexLevel;
		}
	}
	
	


	public String getUserId() {
		return userId;
	}



	public void setUserId(String userId) {
		this.userId = userId;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public Level getLevel() {
		return level;
	}



	public void setLevel(Level level) {
		this.level = level;
	}



	public int getLogin() {
		return login;
	}



	public void setLogin(int login) {
		this.login = login;
	}



	public int getRecommend() {
		return recommend;
	}



	public void setRecommend(int recommend) {
		this.recommend = recommend;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getRegDt() {
		return regDt;
	}



	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}



	@Override
	public String toString() {
		return "UserVO [userId=" + userId + ", name=" + name + ", password=" + password + ", level=" + level
				+ ", login=" + login + ", recommend=" + recommend + ", email=" + email + ", regDt=" + regDt
				+ ", toString()=" + super.toString() + "]";
	}



	



	

}