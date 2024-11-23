package Apps;

import java.sql.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class LinkingDatabase {
    public static Connection getConnection() throws SQLException, IOException {
        Properties config = new Properties();
        config.load(new FileInputStream("resources/config.properties"));

        String url = config.getProperty("db.url");
        String username = config.getProperty("db.username");
        String password = config.getProperty("db.password");

        return DriverManager.getConnection(url, username, password);
    }
}
