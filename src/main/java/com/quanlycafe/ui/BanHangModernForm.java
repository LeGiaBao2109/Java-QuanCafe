package com.quanlycafe.ui;

import com.quanlycafe.ui.component.Sidebar;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class BanHangModernForm extends JFrame {
	
    private final Color COLOR_BG = new Color(253, 248, 245);
    
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public BanHangModernForm(String role, String tenNV) {
        setTitle("KAHVI");
        setSize(1366, 768); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_BG);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(COLOR_BG);
        cardPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        BanHangPanel pnlBanHang = new BanHangPanel();
        HoaDonPanel pnlHoaDon = new HoaDonPanel();
        KhachHangPanel pnlKhachHang = new KhachHangPanel();
        NhapKhoPanel pnlNhapKho = new NhapKhoPanel(tenNV);
        ThongKeNhanVienPanel pnlThongKeNV = new ThongKeNhanVienPanel();

        cardPanel.add(pnlBanHang, "BanHang");
        cardPanel.add(pnlHoaDon, "HoaDon");
        cardPanel.add(pnlKhachHang, "KhachHang");
        cardPanel.add(pnlNhapKho, "NhapKho");
        cardPanel.add(pnlThongKeNV, "ThongKeNV");

        Sidebar sidebar = new Sidebar(role, tenNV, menu -> cardLayout.show(cardPanel, menu)); 
        
        add(sidebar, BorderLayout.WEST);
        add(cardPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } 
        catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new BanHangModernForm("NHAN_VIEN", "Test User").setVisible(true));
    }
}