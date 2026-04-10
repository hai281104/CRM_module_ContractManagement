package com.crm.utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
	private static final String URL = "jdbc:mysql://localhost:3306/db_crm?useUnicode=true&characterEncoding=UTF-8";
    private static final String USER = "root";
    private static final String PASS = ""; // Mật khẩu mặc định của WAMP

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
