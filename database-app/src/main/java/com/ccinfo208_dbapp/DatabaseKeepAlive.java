package com.ccinfo208_dbapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseKeepAlive {
    private final Connection connection;
    private final int intervalMillis; // Keep-alive interval in milliseconds
    private boolean running = true;

    public DatabaseKeepAlive(Connection connection, int intervalMillis) {
        this.connection = connection;
        this.intervalMillis = intervalMillis;
        this.running = true;
    }

    public void start() {
        Thread keepAliveThread = new Thread(() -> {
            while (running) {
                try {
                    Thread.sleep(intervalMillis);
                    // Send a lightweight query
                    try (PreparedStatement stmt = connection.prepareStatement("SELECT 1")) {
                        stmt.executeQuery();
                    }
                } catch (SQLException | InterruptedException e) {
                    System.err.println("Keep-alive error: " + e.getMessage());
                    break;
                }
            }
        });

        keepAliveThread.setDaemon(true); // Daemon thread stops when the app exits
        keepAliveThread.start();
    }

    public void stop() {
        running = false;
    }
}

