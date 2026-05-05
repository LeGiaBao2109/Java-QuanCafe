package com.quanlycafe.ui;

import com.quanlycafe.dao.TaiKhoanDAO;
import com.quanlycafe.entity.NhanVien;
import com.quanlycafe.entity.TaiKhoan;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginDialog1 extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;

    private final Color COLOR_PRIMARY = new Color(92, 64, 51);
    private final Color COLOR_BORDER = new Color(222, 204, 190);
    private final Color COLOR_BG_LEFT = new Color(245, 240, 235);
    private final Color COLOR_TEXT = new Color(60, 42, 33);

    public LoginDialog1() {
        setTitle("Đăng nhập - HAHA Coffee");
        setSize(900, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(450, 500));
        leftPanel.setLayout(new BorderLayout());
        leftPanel.setBackground(COLOR_BG_LEFT);

        try {
            ImageIcon imageIcon = new ImageIcon("src/main/resources/images/side_image.jpg");
            Image img = imageIcon.getImage().getScaledInstance(450, 500, Image.SCALE_SMOOTH);
            JLabel lblImage = new JLabel(new ImageIcon(img));
            leftPanel.add(lblImage);
        } catch (Exception e) {
        }

        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setPreferredSize(new Dimension(450, 500));
        rightPanel.setLayout(null);

        JLabel lblTitle = new JLabel("CHÀO MỪNG QUAY TRỞ LẠI", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(COLOR_PRIMARY);
        lblTitle.setBounds(0, 60, 450, 40);
        rightPanel.add(lblTitle);

        JLabel lblUser = new JLabel("Tên đăng nhập");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblUser.setForeground(COLOR_TEXT);
        lblUser.setBounds(50, 140, 150, 25);
        rightPanel.add(lblUser);

        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtUsername.setBounds(50, 170, 350, 45);
        txtUsername.setBorder(new CompoundBorder(
                new LineBorder(COLOR_BORDER, 1),
                new EmptyBorder(0, 15, 0, 15)
        ));
        rightPanel.add(txtUsername);

        JLabel lblPass = new JLabel("Mật khẩu");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPass.setForeground(COLOR_TEXT);
        lblPass.setBounds(50, 240, 150, 25);
        rightPanel.add(lblPass);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtPassword.setBounds(50, 270, 350, 45);
        txtPassword.setBorder(new CompoundBorder(
                new LineBorder(COLOR_BORDER, 1),
                new EmptyBorder(0, 15, 0, 15)
        ));
        rightPanel.add(txtPassword);

        JButton btnLogin = new JButton("ĐĂNG NHẬP");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnLogin.setBounds(50, 360, 350, 50);
        btnLogin.setBackground(COLOR_PRIMARY);
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