package com.quanlycafe.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {
    // 1. Chỉ sửa các thông số này cho khớp với Azure của bạn
    private static final String HOST = "127.0.0.1"; // Nếu là Azure: "your-server.database.windows.net"
    private static final String PORT = "1433";
    private static final String DB_NAME = "quanlycafe";
    private static final String USER = "sa";
    private static final String PASS = "01635202769Hieu@";

    // 2. CHUỖI URL PHẢI ĐÚNG CÚ PHÁP NÀY:
    private static final String URL = "jdbc:sqlserver://" + HOST + ":" + PORT + ";databaseName=" + DB_NAME + ";encrypt=true;trustServerCertificate=true;";

    public static Connection getConnection() throws SQLException {
        try {
            // Ép nạp driver thủ công để đảm bảo Java nhận diện được mssql-jdbc
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Lỗi: Không tìm thấy Driver SQL Server trong Classpath!");
        }
    }
}