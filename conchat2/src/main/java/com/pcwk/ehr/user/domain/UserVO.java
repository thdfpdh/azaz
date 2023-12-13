package com.pcwk.ehr.user.domain;

import com.pcwk.ehr.cmn.DTO;

public class UserVO  {
	// ctrl + shift + y : 소문자로 바꾸기
	// ctrl + shift + x : 대문자로 바꾸기

	private int no;
	private String nm;
	private int birth;
	private String id;
	private String pw;
	private String email;
	private String reg_ymd;
	private int chat;
	private int act;
	private int tier;
	private int click;
	private int port;
	private double pop_scr;

	public UserVO() {
		super();
	}

	public UserVO(String nm, int birth, String id, String pw, String email, String reg_ymd, int chat, int act) {
		super();
		this.nm = nm;
		this.birth = birth;
		this.id = id;
		this.pw = pw;
		this.email = email;
		this.reg_ymd = reg_ymd;
		this.chat = chat;
		this.act = act;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getNm() {
		return nm;
	}

	public void setNm(String nm) {
		this.nm = nm;
	}

	public int getBirth() {
		return birth;
	}

	public void setBirth(int birth) {
		this.birth = birth;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getReg_ymd() {
		return reg_ymd;
	}

	public void setReg_ymd(String reg_ymd) {
		this.reg_ymd = reg_ymd;
	}

	public int getChat() {
		return chat;
	}

	public void setChat(int chat) {
		this.chat = chat;
	}

	public int getAct() {
		return act;
	}

	public void setAct(int act) {
		this.act = act;
	}

	public int getTier() {
		return tier;
	}

	public void setTier(int tier) {
		this.tier = tier;
	}

	public int getClick() {
		return click;
	}

	public void setClick(int click) {
		this.click = click;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public double getPop_scr() {
		return pop_scr;
	}

	public void setPop_scr(double pop_scr) {
		this.pop_scr = pop_scr;
	}

	@Override
	public String toString() {
		return "UserVO [no=" + no + ", nm=" + nm + ", birth=" + birth + ", id=" + id + ", pw=" + pw + ", email=" + email
				+ ", reg_ymd=" + reg_ymd + ", chat=" + chat + ", act=" + act + ", tier=" + tier + ", click=" + click
				+ ", port=" + port + ", pop_scr=" + pop_scr + "]";
	}

}