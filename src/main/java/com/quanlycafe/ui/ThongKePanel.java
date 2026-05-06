package com.quanlycafe.ui;

import com.quanlycafe.dao.ThongKeDAO;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ThongKePanel extends JPanel {

    private final Color COLOR_BG = new Color(253, 248, 245);
    private final Color COLOR_SURFACE = Color.WHITE;
    private final Color COLOR_PRIMARY = new Color(92, 64, 51);
    private final Color COLOR_BORDER = new Color(230, 220, 210);

    private DecimalFormat formatter = new DecimalFormat("#,### đ");
    private ThongKeDAO dao;
    
    private JPanel pnlCards;
    private JPanel pnlChartArea;
    private JPanel pnlTopProducts;

    public ThongKePanel() {
        dao = new ThongKeDAO();
        setLayout(new BorderLayout(0, 20));
        setBackground(COLOR_BG);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 12));
        pnlFilter.setBackground(COLOR_SURFACE);
        pnlFilter.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));

        JLabel lblTuNgay = new JLabel("Từ ngày:");
        lblTuNgay.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTuNgay.setForeground(new Color(100, 80, 70));
        JDateChooser dcTuNgay = createDateChooser(true);

        JLabel lblDenNgay = new JLabel("Đến ngày:");
        lblDenNgay.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblDenNgay.setForeground(new Color(100, 80, 70));
        JDateChooser dcDenNgay = createDateChooser(false);

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

            long diffInMillies = Math.abs(denNgay.getTime() - tuNgay.getTime());
            long diffDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            
            if (diffDays > 31) {
                JOptionPane.showMessageDialog(this, "Chỉ được phép lọc dữ liệu tối đa 31 ngày", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            capNhatDuLieuThongKe(tuNgay, denNgay);
        });

        pnlFilter.add(lblTuNgay);
        pnlFilter.add(dcTuNgay);
        pnlFilter.add(lblDenNgay);
        pnlFilter.add(dcDenNgay);
        pnlFilter.add(btnLoc);

        JPanel pnlContent = new JPanel(new BorderLayout(15, 15));
        pnlContent.setOpaque(false);

        pnlCards = new JPanel(new GridLayout(1, 4, 15, 0));
        pnlCards.setOpaque(false);
        
        pnlChartArea = new JPanel(new BorderLayout());
        pnlChartArea.setOpaque(false);
        
        pnlTopProducts = new JPanel(new BorderLayout());
        pnlTopProducts.setOpaque(false);
        pnlTopProducts.setPreferredSize(new Dimension(350, 0));

        pnlContent.add(pnlCards, BorderLayout.NORTH);
        pnlContent.add(pnlChartArea, BorderLayout.CENTER);
        pnlContent.add(pnlTopProducts, BorderLayout.EAST);

        add(pnlFilter, BorderLayout.NORTH);
        add(pnlContent, BorderLayout.CENTER);

        capNhatDuLieuThongKe(dcTuNgay.getDate(), dcDenNgay.getDate());
    }

    private JDateChooser createDateChooser(boolean isTuNgay) {
        JDateChooser dateChooser = new JDateChooser();
        long sevenDays = 7L * 24 * 60 * 60 * 1000;
        Date today = new Date();
        
        dateChooser.setDate(isTuNgay ? new Date(today.getTime() - sevenDays) : today); 
        
        dateChooser.setMaxSelectableDate(today);

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
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitle.setForeground(new Color(120, 110, 100));

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblValue.setForeground(valueColor);

        JLabel lblSub = new JLabel(subTitle);
        lblSub.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblSub.setForeground(Color.GRAY);

        card.add(lblTitle);
        card.add(lblValue);
        card.add(lblSub);

        return card;
    }

    private void capNhatDuLieuThongKe(Date tuNgay, Date denNgay) {
        double[] tongQuan = dao.getThongKeTongQuan(tuNgay, denNgay);
        int soDon = (int) tongQuan[0];
        double tongDoanhThu = tongQuan[1];
        double tongChiPhi = tongQuan[2];

        double[] doanhThuSoSanh = dao.getDoanhThuHomNayVaHomQua();
        double dtHomNay = doanhThuSoSanh[0];
        double dtHomQua = doanhThuSoSanh[1];
        
        String subTitleSoSanh = "";
        Color colorSoSanh = COLOR_PRIMARY;
        
        if (dtHomNay == 0 && dtHomQua == 0) {
            subTitleSoSanh = "Chưa có phát sinh";
            colorSoSanh = Color.GRAY;
        } else {
            double chenhLech = dtHomNay - dtHomQua;
            if (dtHomQua == 0) {
                subTitleSoSanh = "▲ Tăng 100% so với hôm qua";
                colorSoSanh = new Color(40, 167, 69); 
            } else {
                double phanTram = (Math.abs(chenhLech) / dtHomQua) * 100;
                String textPt = String.format("%.1f", phanTram) + "%";
                if (chenhLech >= 0) {
                    subTitleSoSanh = "▲ Tăng " + textPt + " so với hôm qua";
                    colorSoSanh = new Color(40, 167, 69); 
                } else {
                    subTitleSoSanh = "▼ Giảm " + textPt + " so với hôm qua";
                    colorSoSanh = new Color(220, 53, 69); 
                }
            }
        }

        pnlCards.removeAll();
        pnlCards.add(createKPICard("DOANH THU HÔM NAY", formatter.format(dtHomNay), subTitleSoSanh, colorSoSanh));
        pnlCards.add(createKPICard("SỐ ĐƠN (KỲ LỌC)", soDon + "", "Hóa đơn đã thanh toán", COLOR_PRIMARY));
        pnlCards.add(createKPICard("DOANH THU (KỲ LỌC)", formatter.format(tongDoanhThu), "Tiền thu vào", new Color(40, 167, 69)));
        pnlCards.add(createKPICard("CHI PHÍ NHẬP (KỲ LỌC)", formatter.format(tongChiPhi), "Tiền mua nguyên liệu", new Color(220, 53, 69)));
        
        pnlChartArea.removeAll();
        JLabel lblChartTitle = new JLabel("Biểu đồ Doanh thu theo ngày (Kỳ lọc)");
        lblChartTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblChartTitle.setForeground(COLOR_PRIMARY);
        lblChartTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        JPanel chartWrapper = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_SURFACE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.setColor(COLOR_BORDER);
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 10, 10));
            }
        };
        chartWrapper.setOpaque(false);
        chartWrapper.setBorder(new EmptyBorder(20, 20, 20, 20));

        Map<String, Double> mapDoanhThu = dao.getDoanhThuTheoNgay(tuNgay, denNgay);
        if (mapDoanhThu.isEmpty()) {
            chartWrapper.add(new JLabel("Chưa có phát sinh doanh thu trong kỳ này.", SwingConstants.CENTER), BorderLayout.CENTER);
        } else {
            String[] days = mapDoanhThu.keySet().toArray(new String[0]);
            double[] revenues = mapDoanhThu.values().stream().mapToDouble(Double::doubleValue).toArray();
            String todayStr = new SimpleDateFormat("dd/MM").format(new Date());
            chartWrapper.add(new CustomBarChart(days, revenues, todayStr), BorderLayout.CENTER);
        }
        
        pnlChartArea.add(lblChartTitle, BorderLayout.NORTH);
        pnlChartArea.add(chartWrapper, BorderLayout.CENTER);

        pnlTopProducts.removeAll();
        JLabel lblTopTitle = new JLabel("Top Sản phẩm bán chạy (Kỳ lọc)");
        lblTopTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTopTitle.setForeground(COLOR_PRIMARY);
        lblTopTitle.setBorder(new EmptyBorder(0, 0, 10, 0));

        DefaultTableModel model = new DefaultTableModel(null, new String[]{"Tên món", "SL Bán"}) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        
        List<Object[]> topData = dao.getTopSanPhamBanChay(tuNgay, denNgay);
        for (Object[] row : topData) {
            model.addRow(new Object[]{row[0], row[1]});
        }
        if (topData.isEmpty()) model.addRow(new Object[]{"Chưa có dữ liệu", "-"});

        JTable table = new JTable(model);
        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(245, 240, 235));
        table.getColumnModel().getColumn(1).setMaxWidth(80);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(COLOR_SURFACE);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDER));

        pnlTopProducts.add(lblTopTitle, BorderLayout.NORTH);
        pnlTopProducts.add(scrollPane, BorderLayout.CENTER);

        pnlCards.revalidate(); pnlCards.repaint();
        pnlChartArea.revalidate(); pnlChartArea.repaint();
        pnlTopProducts.revalidate(); pnlTopProducts.repaint();
    }

    class CustomBarChart extends JPanel {
        private String[] labels;
        private double[] values;
        private String todayStr; 

        public CustomBarChart(String[] labels, double[] values, String todayStr) {
            this.labels = labels;
            this.values = values;
            this.todayStr = todayStr;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (values == null || values.length == 0) return;

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            int padding = 30;
            int labelPadding = 20;

            double maxVal = 0;
            for (double v : values) if (v > maxVal) maxVal = v;
            if (maxVal == 0) maxVal = 1;

            int chartWidth = width - padding * 2;
            int chartHeight = height - padding - labelPadding;
            int barWidth = Math.min(chartWidth / values.length - 10, 60); 

            g2.setColor(new Color(230, 230, 230));
            for (int i = 0; i <= 5; i++) {
                int y = padding + (chartHeight * i / 5);
                g2.drawLine(padding, y, width - padding, y);
            }

            for (int i = 0; i < values.length; i++) {
                int x = padding + i * (chartWidth / values.length) + (chartWidth / values.length - barWidth) / 2;
                int barHeight = (int) ((values[i] / maxVal) * chartHeight);
                int y = height - labelPadding - barHeight;

                if (labels[i].equals(todayStr)) {
                    g2.setColor(COLOR_PRIMARY); 
                } else {
                    g2.setColor(new Color(188, 143, 111));
                }
                
                g2.fillRoundRect(x, y, barWidth, barHeight, 5, 5);

                g2.setColor(Color.DARK_GRAY);
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                String valStr = (values[i] == 0) ? "0" : formatter.format(values[i]).replace(" đ", "");
                int strWidth = g2.getFontMetrics().stringWidth(valStr);
                g2.drawString(valStr, x + (barWidth - strWidth) / 2, y - 5);

                g2.setColor(Color.BLACK);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                int labelWidth = g2.getFontMetrics().stringWidth(labels[i]);
                g2.drawString(labels[i], x + (barWidth - labelWidth) / 2, height - 5);
            }
        }
    }
}