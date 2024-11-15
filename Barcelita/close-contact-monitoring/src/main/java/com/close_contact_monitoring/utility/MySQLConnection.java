package com.close_contact_monitoring.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 	A utility class for managing the connection of the database application with the MySQL database.
 * 	Provides methods to start and close connections to specified MySQL database.
 */
public class MySQLConnection {
	private static final String DB_URL = "";
	private static final String DB_USERNAME = "";
	private static final String DB_PW = "";
	
	/**
     * 	Establishes a connection to the MySQL database using the provided URL, username, and password.
     *
     * 	@return A Connection object representing the database connection.
     * 	@throws SQLException If a database access error occurs or the URL is incorrect.
     */
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PW);
	}
	
	/**
     * 	Closes the provided database connection.
     * 
     * 	@param connection The connection to be closed.
     * 	@throws SQLException If an SQL error occurs during closing the connection.
     */
	public static void closeConnection (Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e){
				e.printStackTrace();
			}
		}
	}
}
