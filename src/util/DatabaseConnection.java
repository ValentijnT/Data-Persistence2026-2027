package util;

import java.sql.*;

public class DatabaseConnection {
    private static String URL = "jdbc:postgresql://localhost:5432/ovchip";
    private static String USERNAME = "postgres";
    private static String PASSWORD = "geheim";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
