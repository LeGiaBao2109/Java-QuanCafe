package com.quanlycafe.ui;

import com.quanlycafe.dao.ThongKeDAO;

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

public class ThongKeNhanVienPanel extends JPanel {

    private final Color COLOR_BG = new Color(253, 248, 245);
    private final Color COLOR_SURFACE = Color.WHITE;
    private final Color COLOR_PRIMARY = new Color(92, 64, 51);
    private final Color COLOR_BORDER = new Color(230, 220, 210);

    private DecimalFormat formatter = new DecimalFormat("#,###");
    private ThongKeDAO dao;
    
    private DefaultTableModel modelNhom, modelKhungGio;
    private JLabel lblTongTien, lblTrungBinhBill, lblChuaThanhToan;

    public ThongKeNhanVienPanel() {
        dao = new ThongKeDAO();
        setLayout(new BorderLayout(0, 20)); 
        setBackground(COLOR_BG);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel pnlTop = new JPanel(new BorderLayout());
        pnlTop.setOpaque(false);
        
        JLabel lblTitle = new JLabel("BÁO CÁO CHỐT CA / NGÀY");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(COLOR_PRIMARY);

        JPanel pnlRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        pnlRight.setOpaque(false);
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        JLabel lblDateTitle = new JLabel("Ngày hiện tại: " + sdf.format(new Date()));
        lblDateTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblDateTitle.setForeground(new Color(100, 80, 70));
        
        JButton btnIn = new JButton("In Báo Cáo");
        btnIn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnIn.setBackground(COLOR_PRIMARY); 
        btnIn.setForeground(Color.WHITE);
        btnIn.setFocusPainted(false);
        btnIn.setBorderPainted(false);
        btnIn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnIn.setPreferredSize(new Dimension(140, 35));
        
        btnIn.addActionListener(e -> inBaoCaoCa());
        
        pnlRight.add(lblDateTitle);
        pnlRight.add(btnIn);

        pnlTop.add(lblTitle, BorderLayout.WEST);
        pnlTop.add(pnlRight, BorderLayout.EAST);

        JPanel pnlContent = new JPanel(new GridLayout(1, 2, 20, 0));
        pnlContent.setOpaque(false);

        pnlContent.add(createPanelDoanhThuNhom());
        pnlContent.add(createPanelKhungGio());

        add(pnlTop, BorderLayout.NORTH);
        add(pnlContent, BorderLayout.CENTER);

        loadDuLieu(new Date());
    }

    private JPanel createPanelDoanhThuNhom() {
        JPanel pnl = new JPanel(new BorderLayout(0, 15));
        pnl.setOpaque(false);

        JLabel lblTitle = new JLabel("Doanh thu theo Nhóm");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(COLOR_PRIMARY);

        modelNhom = new DefaultTableModel(null, new String[]{"Nhóm hàng", "SL Bán", "Thành tiền"}) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable tbl = createStyledTable(modelNhom);
        
        DefaultTableCellRenderer rightRender = new DefaultTableCellRenderer();
        rightRender.setHorizontalAlignment(JLabel.RIGHT);
        tbl.getColumnModel().getColumn(1).setCellRenderer(rightRender);
        tbl.getColumnModel().getColumn(2).setCellRenderer(rightRender);
        tbl.getColumnModel().getColumn(1).setMaxWidth(100);

        JScrollPane scrollPane = new JScrollPane(tbl);
        scrollPane.getViewport().setBackground(COLOR_SURFACE);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDER));

        JPanel pnlTongKet = new JPanel(new GridLayout(3, 1, 0, 10));
        pnlTongKet.setOpaque(false);

        lblTongTien = new JLabel("0 đ");
        lblTrungBinhBill = new JLabel("0 đ");
        lblChuaThanhToan = new JLabel("0");

        pnlTongKet.add(createSummaryRow("Tổng tiền doanh thu:", lblTongTien, new Color(40, 167, 69))); 
        pnlTongKet.add(createSummaryRow("Trung bình / Hóa đơn:", lblTrungBinhBill, COLOR_PRIMARY));     
        pnlTongKet.add(createSummaryRow("Hóa đơn chưa thanh toán:", lblChuaThanhToan, new Color(220, 53, 69))); 

        pnl.add(lblTitle, BorderLayout.NORTH);
        pnl.add(scrollPane, BorderLayout.CENTER);
        pnl.add(pnlTongKet, BorderLayout.SOUTH);
        return pnl;
    }

    private JPanel createPanelKhungGio() {
        JPanel pnl = new JPanel(new BorderLayout(0, 15));
        pnl.setOpaque(false);

        JLabel lblTitle = new JLabel("Lưu lượng theo Khung giờ");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(COLOR_PRIMARY);

        modelKhungGio = new DefaultTableModel(null, new String[]{"Khung giờ", "Số Bill", "Doanh thu", "Tỉ lệ %"}) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable tbl = createStyledTable(modelKhungGio);
        
        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        for(int i=0; i<4; i++) tbl.getColumnModel().getColumn(i).setCellRenderer(centerRender);

        JScrollPane scrollPane = new JScrollPane(tbl);
        scrollPane.getViewport().setBackground(COLOR_SURFACE);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDER));

        pnl.add(lblTitle, BorderLayout.NORTH);
        pnl.add(scrollPane, BorderLayout.CENTER);
        return pnl;
    }

    private JPanel createSummaryRow(String title, JLabel lblValue, Color valueColor) {
        JPanel pnl = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_SURFACE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.setColor(COLOR_BORDER);
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 10, 10));
                g2.dispose();
            }
        };
        pnl.setOpaque(false);
        pnl.setBorder(new EmptyBorder(12, 15, 12, 15));

        JLabel lblT = new JLabel(title);
        lblT.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblT.setForeground(Color.DARK_GRAY);

        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblValue.setForeground(valueColor);

        pnl.add(lblT, BorderLayout.WEST);
        pnl.add(lblValue, BorderLayout.EAST);
        return pnl;
    }

    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setRowHeight(38);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(245, 240, 235));
        table.getTableHeader().setForeground(COLOR_PRIMARY);
        table.setSelectionBackground(new Color(235, 225, 215));
        table.setSelectionForeground(COLOR_PRIMARY);
        table.setShowVerticalLines(false);
        table.setGridColor(COLOR_BORDER);
        
        ((DefaultTableCellRenderer)table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        return table;
    }

    private void loadDuLieu(Date ngay) {
        modelNhom.setRowCount(0);
        List<Object[]> dsNhom = dao.getDoanhThuTheoNhom(ngay);
        for (Object[] row : dsNhom) {
            modelNhom.addRow(new Object[]{ row[0], row[1], formatter.format(row[2]) + " đ" });
        }

        double[] chiSo = dao.getChiSoChotCa(ngay);
        double tongTien = chiSo[0];
        double soBill = chiSo[1];
        int chuaTT = (int) chiSo[2];
        
        lblTongTien.setText(formatter.format(tongTien) + " đ");
        double tbBill = (soBill > 0) ? (tongTien / soBill) : 0;
        lblTrungBinhBill.setText(formatter.format(tbBill) + " đ");
        lblChuaThanhToan.setText(chuaTT + " hóa đơn");

        modelKhungGio.setRowCount(0);
        List<Object[]> dsGio = dao.getThongKeKhungGio(ngay);
        
        for (int i = 0; i <= 23; i++) {
            int slBill = 0;
            double doanhThuGio = 0;
            
            for (Object[] row : dsGio) {
                if ((int) row[0] == i) {
                    slBill = (int) row[1];
                    doanhThuGio = (double) row[2];
                    break;
                }
            }
            
            String tenGio = String.format("%02d:00 - %02d:59", i, i);
            double tiLe = (tongTien > 0) ? (doanhThuGio / tongTien * 100) : 0;
            String strTiLe = String.format("%.1f%%", tiLe);
            
            modelKhungGio.addRow(new Object[]{ tenGio, slBill, formatter.format(doanhThuGio), strTiLe });
        }
    }

    private void inBaoCaoCa() {
        try {
            Date ngayIn = new Date();
            StringBuilder sb = new StringBuilder();
            sb.append("==========================================\n");
            sb.append("             BÁO CÁO CHỐT CA\n");
            sb.append("==========================================\n");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            sb.append("Thời gian in: ").append(sdf.format(ngayIn)).append("\n\n");

            sb.append(String.format("%-20s %5s %13s\n", "Nhóm hàng", "SL", "Thành tiền"));
            sb.append("------------------------------------------\n");
            
            double[] chiSo = dao.getChiSoChotCa(ngayIn);
            double tongTien = chiSo[0];
            
            List<Object[]> dsNhom = dao.getDoanhThuTheoNhom(ngayIn);
            for (Object[] row : dsNhom) {
                String ten = row[0].toString();
                if (ten.length() > 20) ten = ten.substring(0, 20); 
                String sl = row[1].toString();
                String tien = formatter.format(row[2]);
                sb.append(String.format("%-20s %5s %13s\n", ten, sl, tien));
            }
            sb.append("------------------------------------------\n");

            sb.append(String.format("%-25s %14s\n", "Tổng tiền:", formatter.format(chiSo[0]) + " đ"));
            double tbBill = chiSo[1] > 0 ? chiSo[0] / chiSo[1] : 0;
            sb.append(String.format("%-25s %14s\n", "Trung bình bill:", formatter.format(tbBill) + " đ"));
            sb.append(String.format("%-25s %14s\n", "Phiếu chưa thanh toán:", (int)chiSo[2] + ""));
            sb.append("------------------------------------------\n");

            sb.append(String.format("%-15s %7s %11s %5s\n", "Khung giờ", "SL Bill", "Doanh thu", "Tỉ lệ"));
            sb.append("------------------------------------------\n");
            
            List<Object[]> dsGio = dao.getThongKeKhungGio(ngayIn);
            for (int i = 0; i <= 23; i++) {
                int slBill = 0;
                double doanhThuGio = 0;
                for (Object[] row : dsGio) {
                    if ((int) row[0] == i) {
                        slBill = (int) row[1];
                        doanhThuGio = (double) row[2];
                        break;
                    }
                }
                String tenGio = String.format("%02d:00-%02d:59", i, i);
                String tienStr = formatter.format(doanhThuGio);
                
                double tiLe = (tongTien > 0) ? (doanhThuGio / tongTien * 100) : 0;
                String strTiLe = String.format("%.1f%%", tiLe);
                
                sb.append(String.format("%-15s %7d %11s %5s\n", tenGio, slBill, tienStr, strTiLe));
            }
            
            sb.append("==========================================\n");
            sb.append("        Xin cảm ơn và hẹn gặp lại!\n");

            JDialog printDialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Xem trước bản in Z-Report", Dialog.ModalityType.APPLICATION_MODAL);
            printDialog.setSize(440, 750);
            printDialog.setLocationRelativeTo(this);
            printDialog.setLayout(new BorderLayout());

            JTextArea txtPrint = new JTextArea(sb.toString());
            txtPrint.setFont(new Font("Monospaced", Font.PLAIN, 13)); 
            txtPrint.setBackground(new Color(255, 253, 240)); 
            txtPrint.setEditable(false);
            txtPrint.setMargin(new Insets(20, 20, 20, 20));

            JScrollPane scroll = new JScrollPane(txtPrint);
            scroll.setBorder(BorderFactory.createEmptyBorder());
            scroll.getVerticalScrollBar().setUnitIncrement(16);

            JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
            pnlBottom.setBackground(Color.WHITE);
            pnlBottom.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, COLOR_BORDER));
            
            JButton btnXacNhanIn = new JButton("Xác nhận In");
            btnXacNhanIn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnXacNhanIn.setBackground(COLOR_PRIMARY);
            btnXacNhanIn.setForeground(Color.WHITE);
            btnXacNhanIn.setOpaque(true);
            btnXacNhanIn.setContentAreaFilled(true);
            btnXacNhanIn.setFocusPainted(false);
            btnXacNhanIn.setBorderPainted(false);
            btnXacNhanIn.setPreferredSize(new Dimension(140, 35));
            btnXacNhanIn.addActionListener(e2 -> {
                printDialog.dispose();
                JOptionPane.showMessageDialog(this, "Đã in và đóng ca thành công!", "Hoàn tất", JOptionPane.INFORMATION_MESSAGE);
            });
            
            JButton btnHuy = new JButton("Hủy");
            btnHuy.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnHuy.setBackground(new Color(220, 53, 69)); 
            btnHuy.setForeground(Color.WHITE);
            btnHuy.setOpaque(true);
            btnHuy.setContentAreaFilled(true);
            btnHuy.setFocusPainted(false);
            btnHuy.setBorderPainted(false);
            btnHuy.setPreferredSize(new Dimension(100, 35));
            btnHuy.addActionListener(e2 -> printDialog.dispose());

            pnlBottom.add(btnXacNhanIn);
            pnlBottom.add(btnHuy);

            printDialog.add(scroll, BorderLayout.CENTER);
            printDialog.add(pnlBottom, BorderLayout.SOUTH);
            printDialog.setVisible(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tạo bản in: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}