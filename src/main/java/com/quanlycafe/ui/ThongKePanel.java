package com.quanlycafe.ui;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Date;

public class ThongKePanel extends JPanel {

    private final Color COLOR_BG = new Color(253, 248, 245);
    private final Color COLOR_SURFACE = Color.WHITE;
    private final Color COLOR_PRIMARY = new Color(92, 64, 51);
    private final Color COLOR_BORDER = new Color(230, 220, 210);

    public ThongKePanel() {
        setLayout(new BorderLayout(0, 20));
        setBackground(COLOR_BG);
        setOpaque(false);

        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 12));
        pnlFilter.setBackground(COLOR_SURFACE);
        pnlFilter.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));

        JLabel lblTuNgay = new JLabel("Từ ngày:");
        lblTuNgay.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTuNgay.setForeground(new Color(100, 80, 70));
        
        JDateChooser dcTuNgay = createDateChooser();

        JLabel lblDenNgay = new JLabel("Đến ngày:");
        lblDenNgay.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblDenNgay.setForeground(new Color(100, 80, 70));
        
        JDateChooser dcDenNgay = createDateChooser();

        JButton btnLoc = new JButton("Lọc dữ liệu");
        btnLoc.setPreferredSize(new Dimension(120, 35));
        btnLoc.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLoc.setBackground(COLOR_PRIMARY);
        btnLoc.setForeground(Color.WHITE);
        btnLoc.setFocusPainted(false);
        btnLoc.setBorderPainted(false);
        btnLoc.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnLoc.addActionListener(e -> {
            Date tuNgay = dcTuNgay.getDate();
            Date denNgay = dcDenNgay.getDate();
            
            if (tuNgay == null || denNgay == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn đầy đủ ngày!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (tuNgay.after(denNgay)) {
                JOptionPane.showMessageDialog(this, "\"Từ ngày\" không thể lớn hơn \"Đến ngày\"!", "Lỗi chọn ngày", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            capNhatDuLieuThongKe(tuNgay, denNgay);
        });

        pnlFilter.add(lblTuNgay);
        pnlFilter.add(dcTuNgay);
        pnlFilter.add(lblDenNgay);
        pnlFilter.add(dcDenNgay);
        pnlFilter.add(btnLoc);

        JPanel pnlContent = new JPanel(new BorderLayout(0, 20));
        pnlContent.setOpaque(false);

        JPanel pnlCards = new JPanel(new GridLayout(1, 4, 15, 0));
        pnlCards.setOpaque(false);

        pnlCards.add(createKPICard("SỐ ĐƠN BÁN RA", "0", "Đang chờ lọc...", COLOR_PRIMARY));
        pnlCards.add(createKPICard("TỔNG DOANH THU", "0đ", "Đang chờ lọc...", COLOR_PRIMARY));
        pnlCards.add(createKPICard("TRUNG BÌNH/ĐƠN", "0đ", "Đang chờ lọc...", COLOR_PRIMARY));
        pnlCards.add(createKPICard("TĂNG TRƯỞNG", "-", "So với kỳ trước", new Color(40, 167, 69)));

        JPanel pnlChartArea = new JPanel(new BorderLayout());
        pnlChartArea.setBackground(COLOR_SURFACE);
        pnlChartArea.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));
        
        JLabel lblChartPlaceholder = new JLabel("Khu vực hiển thị Biểu đồ", SwingConstants.CENTER);
        lblChartPlaceholder.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        lblChartPlaceholder.setForeground(new Color(150, 140, 130));
        pnlChartArea.add(lblChartPlaceholder, BorderLayout.CENTER);

        pnlContent.add(pnlCards, BorderLayout.NORTH);
        pnlContent.add(pnlChartArea, BorderLayout.CENTER);

        add(pnlFilter, BorderLayout.NORTH);
        add(pnlContent, BorderLayout.CENTER);
    }

    private JDateChooser createDateChooser() {
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setDate(new Date()); 
        dateChooser.setDateFormatString("dd/MM/yyyy");
        dateChooser.setPreferredSize(new Dimension(150, 35));
        dateChooser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JTextField dateTextField = (JTextField) dateChooser.getDateEditor().getUiComponent();
        dateTextField.setBackground(COLOR_SURFACE);
        dateTextField.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        
        return dateChooser;
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

    private void capNhatDuLieuThongKe(Date tuNgay, Date denNgay) {
        System.out.println("Truy vấn DB từ: " + tuNgay + " đến " + denNgay);
    }
}