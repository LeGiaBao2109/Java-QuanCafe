package com.quanlycafe.ui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class KhachHangPanel extends JPanel {
    public KhachHangPanel() {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        setBorder(new LineBorder(new Color(222, 204, 190), 1));
        
        JLabel lblTitle = new JLabel("Giao diện Quản lý Khách hàng đang phát triển...");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(150, 140, 130));
        
        add(lblTitle);
    }
}