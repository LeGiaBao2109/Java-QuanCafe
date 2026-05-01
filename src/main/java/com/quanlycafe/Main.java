package com.quanlycafe;

import com.quanlycafe.ui.LoginForm;
import com.quanlycafe.util.DBConnect;
import com.quanlycafe.ui.LoginForm; // Giả sử đây là Frame chính chứa DesktopPane
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        System.out.println("🚀 Đang thử kết nối tới Database...");

        try (Connection conn = DBConnect.getConnection()) {
            if (conn != null) {
                System.out.println("✅ KẾT NỐI THÀNH CÔNG!");
                System.out.println("Thông tin DB: " + conn.getMetaData().getDatabaseProductName());

                // Sau khi kết nối OK, thiết lập giao diện và chạy App
                setupAndRunApp();
            }
        } catch (Exception e) {
            System.out.println("❌ KẾT NỐI THẤT BẠI!");
            System.out.println("Lý do: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void setupAndRunApp() {
        try {
            // Dùng FlatLaf để giao diện hiện đại như trong pom.xml đã khai báo
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Không thể khởi tạo Look and Feel");
        }

        SwingUtilities.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    }
}