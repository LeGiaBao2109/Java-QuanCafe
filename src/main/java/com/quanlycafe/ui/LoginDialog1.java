package com.quanlycafe.ui;

import com.quanlycafe.dao.TaiKhoanDAO;
import com.quanlycafe.entity.NhanVien;
import com.quanlycafe.entity.TaiKhoan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginDialog1 extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;

    public LoginDialog1() {
        setTitle("Đăng nhập - HAHA Coffee");
        setSize(900, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(450, 500));
        leftPanel.setLayout(new BorderLayout());

        ImageIcon imageIcon = new ImageIcon("src/main/resources/images/side_image.jpg");
        Image img = imageIcon.getImage().getScaledInstance(450, 500, Image.SCALE_SMOOTH);
        JLabel lblImage = new JLabel(new ImageIcon(img));
        leftPanel.add(lblImage);

        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setPreferredSize(new Dimension(450, 500));
        rightPanel.setLayout(null);

        JLabel lblTitle = new JLabel("CHÀO MỪNG QUAY TRỞ LẠI");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(93, 46, 32));
        lblTitle.setBounds(80, 50, 300, 40);
        rightPanel.add(lblTitle);

        JLabel lblUser = new JLabel("Tên đăng nhập");
        lblUser.setBounds(50, 140, 150, 25);
        rightPanel.add(lblUser);

        txtUsername = new JTextField();
        txtUsername.setBounds(50, 170, 350, 40);
        rightPanel.add(txtUsername);

        JLabel lblPass = new JLabel("Mật khẩu");
        lblPass.setBounds(50, 230, 150, 25);
        rightPanel.add(lblPass);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(50, 260, 350, 40);
        rightPanel.add(txtPassword);

        JButton btnLogin = new JButton("ĐĂNG NHẬP");
        btnLogin.setBounds(50, 340, 350, 50);
        btnLogin.setBackground(new Color(93, 46, 32));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setBorderPainted(false);
        btnLogin.setOpaque(true);
        rightPanel.add(btnLogin);

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                xuLyDangNhap();
            }
        });

        txtPassword.addActionListener(e -> xuLyDangNhap());

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
    }

    private void xuLyDangNhap() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        TaiKhoanDAO dao = new TaiKhoanDAO();
        TaiKhoan tk = dao.dangNhap(username, password);

        if (tk != null) {
            NhanVien nv = tk.getMaNV();
            String role = nv.getRoleNV().name();
            String tenNV = nv.getTenNV();

            JOptionPane.showMessageDialog(this, "Đăng nhập thành công!\nXin chào " + tenNV);

            if (role.equals("ADMIN")) {
                QuanLyForm qlForm = new QuanLyForm(role, tenNV);
                qlForm.setVisible(true);
            } else if (role.equals("NHAN_VIEN")) {
                BanHangModernForm bhForm = new BanHangModernForm(role, tenNV);
                bhForm.setVisible(true);
            }

            this.dispose(); 
            
        } else {
            JOptionPane.showMessageDialog(this, "Sai tên đăng nhập, mật khẩu hoặc tài khoản đã bị khóa!", "Lỗi Đăng Nhập", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } 
        catch (Exception e) {}
        
        SwingUtilities.invokeLater(() -> new LoginDialog1().setVisible(true));
    }
}