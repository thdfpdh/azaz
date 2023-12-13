package com.pcwk.ehr.cmn;

import java.sql.SQLException;
import java.util.List;

public interface WorkDiv<T> {
	
	int doUpdate(T inVO) throws SQLException;
	int doDelete(T inVO) throws SQLException;
	T doSelectOne(T inVO) throws SQLException;
	int doSave(T inVO) throws SQLException;
	List<T> doRetrieve(T inVO) throws SQLException;
}
