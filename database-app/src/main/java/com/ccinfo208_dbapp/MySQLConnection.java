package com.ccinfo208_dbapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 	A utility class for managing the connection of the database application with the MySQL database.
 * 	Provides methods to start and close connections to specified MySQL database.
 */
public class MySQLConnection {
	private static final String DB_URL = "jdbc:mysql://153.92.15.3:3306/u400425564_ccinfo208db?autoReconnect=true&useSSL=false&serverTimezone=UTC"; //this uses db on the Hostinger website
	private static final String DB_USERNAME = "u400425564_root";
	private static final String DB_PW = "DLSUm1234!";
	private static DatabaseKeepAlive keepAlive;
	
	/**
     * 	Establishes a connection to the MySQL database using the provided URL, username, and password.
     *
     * 	@return A Connection object representing the database connection.
     * 	@throws SQLException If a database access error occurs or the URL is incorrect.
     */
	public static Connection getConnection() throws SQLException {
		try {
			Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PW);
			keepAlive = new DatabaseKeepAlive(connection, 10000);
			keepAlive.start();
			System.out.println("Connection to CCINFO208DB established successfully.");
			return connection;
		} catch (SQLException e) {
			System.err.println("Failed to establish a connection to CCINFO208DB: " + e.getMessage());
			throw e;
		}
	}
	
	/**
     * 	Closes the provided database connection.
     * 
     * 	@param connection The connection to be closed.
     * 	@throws SQLException If an SQL error occurs during closing the connection.
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                if (keepAlive != null) {
                    keepAlive.stop();
                }
                connection.close();
                System.out.println("Connection to CCINFO208DB closed successfully.\n");
            } catch (SQLException e) {
                System.err.println("Failed to close connection to CCINFO208DB: " + e.getMessage() + "\n");
            }
        }
    }
}
