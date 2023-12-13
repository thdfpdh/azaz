package com.pcwk.ehr;

public class UserVO {

	private String userId;
	private String name;
	private String password;

	public UserVO() {

	}

	public UserVO(String userId, String name, String password) {
		super();
		this.userId = userId;
		this.name = name;
		this.password = password;

	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "UserVO [userId=" + userId + ", name=" + name + ", password=" + password + "]";
	}

}
