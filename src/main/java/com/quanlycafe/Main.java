package com.quanlycafe;

import com.quanlycafe.util.DBConnect;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        System.out.println("🚀 Đang thử kết nối tới Database...");
        try (Connection conn = DBConnect.getConnection()) {
            if (conn != null) {
                System.out.println("✅ KẾT NỐI THÀNH CÔNG!");
                System.out.println("Thông tin DB: " + conn.getMetaData().getDatabaseProductName());
            }
        } catch (Exception e) {
            System.out.println("❌ KẾT NỐI THẤT BẠI!");
            System.out.println("Lý do: " + e.getMessage());
            // In chi tiết lỗi để mình bắt bệnh giúp bạn
            e.printStackTrace();
        }
    }
}
