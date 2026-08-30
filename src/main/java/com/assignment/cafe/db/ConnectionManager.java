package com.assignment.cafe.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * JDBC 연결 객체를 만드는 클래스입니다.
 *
 * 오늘 구현 범위에서는 별도 Connection Pool까지 붙이지 않고,
 * try-with-resources로 Connection, PreparedStatement, ResultSet을 닫는 구조를 먼저 잡습니다.
 */
public class ConnectionManager {
    private static final String DEFAULT_URL =
            "jdbc:mysql://localhost:3306/cafe_order_db?serverTimezone=Asia/Seoul&characterEncoding=UTF-8";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASSWORD = "";

    private ConnectionManager() {
    }

    public static Connection getConnection() {
        String url = readConfig("CAFE_DB_URL", "cafe.db.url", DEFAULT_URL);
        String user = readConfig("CAFE_DB_USER", "cafe.db.user", DEFAULT_USER);
        String password = readConfig("CAFE_DB_PASSWORD", "cafe.db.password", DEFAULT_PASSWORD);

        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new IllegalStateException("데이터베이스 연결에 실패했습니다. MySQL 실행과 DB 설정을 확인해주세요.", e);
        }
    }

    private static String readConfig(String envName, String propertyName, String defaultValue) {
        String propertyValue = System.getProperty(propertyName);

        if (propertyValue != null && !propertyValue.isBlank()) {
            return propertyValue;
        }

        String envValue = System.getenv(envName);

        if (envValue != null && !envValue.isBlank()) {
            return envValue;
        }

        return defaultValue;
    }
}
