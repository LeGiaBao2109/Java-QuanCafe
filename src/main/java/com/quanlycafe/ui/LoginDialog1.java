package com.quanlycafe.ui;

import javax.swing.*;
import java.awt.*;

public class LoginDialog1 extends JFrame {
    public LoginDialog1() {
        setTitle("Đăng nhập - Nopita Coffee");
        setSize(900, 500); // Tăng chiều ngang để chia đôi
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout()); // Sử dụng BorderLayout cho khung chính

        // --- PHẦN BÊN TRÁI: ẢNH ---
        JPanel leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(450, 500));
        leftPanel.setLayout(new BorderLayout());

        // Load ảnh (Thay bằng đường dẫn ảnh của bạn)
        ImageIcon imageIcon = new ImageIcon("src/main/resources/images/side_image.jpg");
        // Scale ảnh cho vừa khung
        Image img = imageIcon.getImage().getScaledInstance(450, 500, Image.SCALE_SMOOTH);
        JLabel lblImage = new JLabel(new ImageIcon(img));
        leftPanel.add(lblImage);

        // --- PHẦN BÊN PHẢI: LOGIN FORM ---
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setPreferredSize(new Dimension(450, 500));
        rightPanel.setLayout(null); // Dùng null để tự do căn chỉnh theo ý bạn

        JLabel lblTitle = new JLabel("CHÀO MỪNG QUAY TRỞ LẠI");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(93, 46, 32));
        lblTitle.setBounds(80, 50, 300, 40);
        rightPanel.add(lblTitle);

        JLabel lblUser = new JLabel("Tên đăng nhập");
        lblUser.setBounds(50, 140, 150, 25);
        rightPanel.add(lblUser);

        JTextField txtUsername = new JTextField();
        txtUsername.setBounds(50, 170, 350, 40);
        rightPanel.add(txtUsername);

        JLabel lblPass = new JLabel("Mật khẩu");
        lblPass.setBounds(50, 230, 150, 25);
        rightPanel.add(lblPass);

        JPasswordField txtPassword = new JPasswordField();
        txtPassword.setBounds(50, 260, 350, 40);
        rightPanel.add(txtPassword);

        JButton btnLogin = new JButton("ĐĂNG NHẬP");
        btnLogin.setBounds(50, 340, 350, 50);
        btnLogin.setBackground(new Color(93, 46, 32));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        rightPanel.add(btnLogin);

        // --- THÊM VÀO FRAME ---
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginDialog1().setVisible(true));
    }
}