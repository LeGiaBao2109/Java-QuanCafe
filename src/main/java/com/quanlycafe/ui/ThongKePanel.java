package com.quanlycafe.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ThongKePanel extends JPanel {

    private final Color COLOR_BG = new Color(253, 248, 245);
    private final Color COLOR_SURFACE = Color.WHITE;
    private final Color COLOR_PRIMARY = new Color(92, 64, 51);
    private final Color COLOR_BORDER = new Color(230, 220, 210);

    public ThongKePanel() {
        setLayout(new BorderLayout(0, 20));
        setBackground(COLOR_BG);
        setOpaque(false);

        JPanel pnlCards = new JPanel(new GridLayout(1, 4, 15, 0));
        pnlCards.setOpaque(false);

        pnlCards.add(createKPICard("SỐ ĐƠN HÔM NAY", "45", "Tăng 5 đơn so với hôm qua", COLOR_PRIMARY));
        pnlCards.add(createKPICard("DOANH THU HÔM NAY", "2.150.000đ", "Đạt 85% mục tiêu ngày", COLOR_PRIMARY));
        pnlCards.add(createKPICard("DOANH THU THÁNG NÀY", "45.800.000đ", "Từ 01/05 đến hiện tại", COLOR_PRIMARY));
        pnlCards.add(createKPICard("TĂNG TRƯỞNG", "+12.5%", "So với cùng kỳ tháng trước", new Color(40, 167, 69)));

        JPanel pnlChartArea = new JPanel(new BorderLayout());
        pnlChartArea.setBackground(COLOR_SURFACE);
        pnlChartArea.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));
        
        JLabel lblChartPlaceholder = new JLabel("Khu vực hiển thị Biểu đồ (Sẽ tích hợp dữ liệu thực tế sau)", SwingConstants.CENTER);
        lblChartPlaceholder.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        lblChartPlaceholder.setForeground(new Color(150, 140, 130));
        pnlChartArea.add(lblChartPlaceholder, BorderLayout.CENTER);

        add(pnlCards, BorderLayout.NORTH);
        add(pnlChartArea, BorderLayout.CENTER);
    }

    private JPanel createKPICard(String title, String value, String subTitle, Color valueColor) {
        JPanel card = new JPanel(new GridLayout(3, 1, 0, 5));
        card.setBackground(COLOR_SURFACE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1),
                new EmptyBorder(15, 20, 15, 20)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTitle.setForeground(new Color(120, 110, 100));

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblValue.setForeground(valueColor);

        JLabel lblSub = new JLabel(subTitle);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(Color.GRAY);

        card.add(lblTitle);
        card.add(lblValue);
        card.add(lblSub);

        return card;
    }
}