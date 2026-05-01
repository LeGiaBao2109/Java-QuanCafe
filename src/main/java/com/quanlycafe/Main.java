package com.quanlycafe;

import com.quanlycafe.util.DBConnect;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        // 1. Kiểm tra kết nối Database
        try (Connection conn = DBConnect.getConnection()) {
            if (conn != null) {
                System.out.println("Kết nối Database thành công!");
            }
        } catch (Exception e) {
            System.err.println("Lỗi kết nối: " + e.getMessage());
        }
    }
}