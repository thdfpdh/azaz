package com.pcwk.ehr;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionMaker {
	Connection makeConnection()throws ClassNotFoundException, SQLException;
}
