package com.pcwk.ehr;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class NConnectionMaker implements ConnectionMaker {

	final String DB_DRIVER = "oracle.jdbc.driver.OracleDriver";
	final String DB_URL = "jdbc:oracle:thin:@192.168.0.123:1521:xe";
	final String DB_ID = "scott";
	final String DB_PASSWORD = "pcwk";

	@Override
	public Connection makeConnection() throws ClassNotFoundException, SQLException {

		Connection conn = null;

		Class.forName(DB_DRIVER);
		conn = DriverManager.getConnection(DB_URL, DB_ID, DB_PASSWORD);
		System.out.println("======================================");
		System.out.println("=Connection=" + conn);
		System.out.println("======================================");

		return conn;
	}
}
