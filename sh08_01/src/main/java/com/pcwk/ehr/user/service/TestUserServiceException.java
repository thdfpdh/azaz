package com.pcwk.ehr.user.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

<<<<<<< HEAD


public class TestUserServiceException extends RuntimeException {

	final Logger LOG = LogManager.getLogger(TestUserServiceException.class);
	public TestUserServiceException() {
		
	}
	
	public TestUserServiceException(String message) {
		super(message);
		LOG.debug("┌─────────────────────────────────────────────────────────┐");
		LOG.debug("│ TestUserServiceException()                              │"+message);
		LOG.debug("└─────────────────────────────────────────────────────────┘");
		
=======
public class TestUserServiceException extends RuntimeException {

	final Logger LOG = LogManager.getLogger(TestUserServiceException.class);
	public TestUserServiceException() {
		
	}
	
	public TestUserServiceException(String message) {
		super(message);
		LOG.debug("TestUserServiceException()"+message);
>>>>>>> branch 'main' of https://github.com/thdfpdh/azaz.git
	}
}
