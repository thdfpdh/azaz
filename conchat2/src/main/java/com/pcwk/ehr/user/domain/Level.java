package com.pcwk.ehr.user.domain;

public enum Level {
	
//	BASIC(1), SILVER(2), GOLD(3);
	GOLD(3, null), SILVER(2, GOLD), BASIC(1, SILVER);
	
	private final int value;
	private final Level next;	// 다음 레벨 정보
	
	Level(int value, Level next){
		this.value = value;
		this.next = next;
	}
	
	public int intValue() {
		return value;
	}
	
	public Level nextLevel() {
		return this.next;
	}
	
	// 값으로부터 Level object return
	public static Level valueOf(int value) {
		switch(value) {
		case 1: return BASIC;
		case 2: return SILVER;
		case 3: return GOLD;
		default: throw new AssertionError("Unknown value: " + value);
		}
	}
}
