package com.quanlycafe.ui.component;

import com.quanlycafe.ui.LoginDialog1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Sidebar extends JPanel {

    private final Color COLOR_BORDER = new Color(222, 204, 190);
    private final Color COLOR_PRIMARY_DARK = new Color(92, 64, 51);
    private final Color COLOR_TEXT_MAIN = new Color(60, 42, 33);

    private List<JButton> menuButtons = new ArrayList<>();

    public Sidebar(String role, String userName, Consumer<String> onMenuClick) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(220, 0));
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, COLOR_BORDER));

        JPanel topMenu = new JPanel();
        topMenu.setLayout(new BoxLayout(topMenu, BoxLayout.Y_AXIS));
        topMenu.setBackground(Color.WHITE);
        topMenu.setBorder(new EmptyBorder(20, 15, 20, 15));

        JLabel lblName = new JLabel(userName);
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblName.setForeground(COLOR_PRIMARY_DARK);
        lblName.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblRole = new JLabel(role.equals("ADMIN") ? "Quản lý (Admin)" : "Nhân viên (Staff)");
        lblRole.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblRole.setForeground(Color.GRAY);
        lblRole.setAlignmentX(Component.LEFT_ALIGNMENT);

        topMenu.add(lblName);
        topMenu.add(lblRole);
        topMenu.add(Box.createVerticalStrut(30));

        if (role.equals("ADMIN")) {
            JButton btnSP = createNavButton("📦 Sản phẩm/Menu", true);
            JButton btnNV = createNavButton("👥 Nhân viên", false);
            JButton btnBC = createNavButton("📊 Báo cáo", false);

            btnSP.addActionListener(e -> {
                setActive(btnSP);
                if (onMenuClick != null) onMenuClick.accept("SanPham");
            });
            btnNV.addActionListener(e -> {
                setActive(btnNV);
                if (onMenuClick != null) onMenuClick.accept("NhanVien");
            });
            btnBC.addActionListener(e -> {
                setActive(btnBC);
                if (onMenuClick != null) onMenuClick.accept("BaoCao");
            });

            topMenu.add(btnSP);
            topMenu.add(Box.createVerticalStrut(8));
            topMenu.add(btnNV);
            topMenu.add(Box.createVerticalStrut(8));
            topMenu.add(btnBC);
            
        } else if (role.equals("STAFF") || role.equals("NHAN_VIEN")) {
            JButton btnOrder = createNavButton("Order", true);
            JButton btnBill = createNavButton("Bill", false);
            JButton btnKH = createNavButton("Khách hàng", false);

            btnOrder.addActionListener(e -> {
                setActive(btnOrder);
                if (onMenuClick != null) onMenuClick.accept("BanHang");
            });
            btnBill.addActionListener(e -> {
                setActive(btnBill);
                if (onMenuClick != null) onMenuClick.accept("HoaDon");
            });
            btnKH.addActionListener(e -> {
                setActive(btnKH);
                if (onMenuClick != null) onMenuClick.accept("KhachHang");
            });

            topMenu.add(btnOrder);
            topMenu.add(Box.createVerticalStrut(8));
            topMenu.add(btnBill);
            topMenu.add(Box.createVerticalStrut(8));
            topMenu.add(btnKH);
        }

        JPanel bottomMenu = new JPanel(new BorderLayout());
        bottomMenu.setBackground(Color.WHITE);
        bottomMenu.setBorder(new EmptyBorder(15, 15, 20, 15));
        
        JButton btnLogout = createNavButton("🚪Đăng xuất", false);
        btnLogout.setForeground(new Color(220, 53, 69));
        
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                Window currentWindow = SwingUtilities.getWindowAncestor(this);
                if (currentWindow != null) {
                    currentWindow.dispose(); 
                    new LoginDialog1().setVisible(true);
                }
            }
        });
        
        bottomMenu.add(btnLogout, BorderLayout.CENTER);

        add(topMenu, BorderLayout.NORTH);
        add(bottomMenu, BorderLayout.SOUTH);
    }

    private JButton createNavButton(String text, boolean isActive) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(200, 45)); 

        if (isActive) {
            btn.setBackground(COLOR_PRIMARY_DARK); 
            btn.setForeground(Color.WHITE);
        } else {
            btn.setBackground(Color.WHITE);
            btn.setForeground(COLOR_TEXT_MAIN);
        }
        
        menuButtons.add(btn);
        return btn;
    }

    private void setActive(JButton activeBtn) {
        for (JButton btn : menuButtons) {
            if (btn.getText().contains("Đăng xuất")) continue;
            btn.setBackground(Color.WHITE);
            btn.setForeground(COLOR_TEXT_MAIN);
        }
        activeBtn.setBackground(COLOR_PRIMARY_DARK);
        activeBtn.setForeground(Color.WHITE);
    }
}