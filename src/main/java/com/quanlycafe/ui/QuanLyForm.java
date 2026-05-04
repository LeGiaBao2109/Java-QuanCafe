package com.quanlycafe.ui;

import com.quanlycafe.ui.component.Sidebar;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class QuanLyForm extends JFrame {

    private final Color COLOR_BG = new Color(253, 248, 245);
    private final Color COLOR_SURFACE = Color.WHITE;
    private final Color COLOR_BORDER = new Color(230, 220, 210);

    private CardLayout cardLayout;
    private JPanel cardPanel;

    public QuanLyForm(String role, String tenNV) { 
        setTitle("BaristaPro - Quản trị Hệ thống");
        setSize(1366, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_BG);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(COLOR_BG);
        cardPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        SanPhamPanel pnlSanPham = new SanPhamPanel(role, tenNV);
        JPanel pnlNhanVien = createPlaceholderPanel("Quản lý Nhân sự (Đang phát triển)");
        ThongKePanel pnlBaoCao = new ThongKePanel(); 

        cardPanel.add(pnlSanPham, "SanPham");
        cardPanel.add(pnlNhanVien, "NhanVien");
        cardPanel.add(pnlBaoCao, "BaoCao");

        Sidebar sidebarAdmin = new Sidebar(role, tenNV, menu -> cardLayout.show(cardPanel, menu));
        add(sidebarAdmin, BorderLayout.WEST);
        add(cardPanel, BorderLayout.CENTER);
    }

    private JPanel createPlaceholderPanel(String text) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_SURFACE);
        panel.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));
        
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 22));
        label.setForeground(new Color(150, 140, 130));
        panel.add(label);
        return panel;
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } 
        catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new QuanLyForm("ADMIN", "Usertest").setVisible(true)); 
    }
}