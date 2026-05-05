package com.quanlycafe.ui;

import com.quanlycafe.dao.ChiTietHoaDonDAO;
import com.quanlycafe.dao.HoaDonDAO;
import com.quanlycafe.entity.ChiTietHoaDon;
import com.quanlycafe.entity.HoaDon;
import com.quanlycafe.util.BillPrinter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HoaDonPanel extends JPanel {
    private JTable tblHoaDon, tblChiTiet;
    private DefaultTableModel modelHD, modelCT;
    private DecimalFormat formatter = new DecimalFormat("#,###đ");
    private DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private JButton btnInHoaDon;

    private HoaDonDAO hdDAO = new HoaDonDAO();
    private ChiTietHoaDonDAO ctDAO = new ChiTietHoaDonDAO();

    public HoaDonPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(253, 248, 245));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(createTableHoaDon());
        splitPane.setBottomComponent(createTableChiTiet());
        splitPane.setDividerLocation(350);
        splitPane.setResizeWeight(0.5);

        add(splitPane, BorderLayout.CENTER);

        loadDataToTableHD();
    }

    private JPanel createTableHoaDon() {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setOpaque(false);

        JLabel lbl = new JLabel("LỊCH SỬ GIAO DỊCH");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lbl.setBorder(new EmptyBorder(0, 0, 10, 0));
        pnl.add(lbl, BorderLayout.NORTH);

        String[] cols = {"Mã HD", "Mã Đơn", "Ngày thanh toán", "Phương thức", "Tổng tiền cuối"};
        modelHD = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tblHoaDon = new JTable(modelHD);
        tblHoaDon.setRowHeight(35);

        pnl.add(new JScrollPane(tblHoaDon), BorderLayout.CENTER);

        tblHoaDon.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tblHoaDon.getSelectedRow();
                if (row != -1) {
                    String maDH = modelHD.getValueAt(row, 1).toString();
                    loadDataToTableCT(maDH);
                }
            }
        });

        JPanel pnlControl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlControl.setOpaque(false);

        btnInHoaDon = new JButton("In Hóa Đơn");
        btnInHoaDon.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnInHoaDon.setBackground(new Color(0, 153, 76));
        btnInHoaDon.setForeground(Color.WHITE);

        btnInHoaDon.addActionListener(e -> {
            int row = tblHoaDon.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một hóa đơn để in!");
                return;
            }

            List<HoaDon> dsHD = hdDAO.layTatCaHoaDon();
            HoaDon selectedHD = dsHD.get(row);

            String maDH = selectedHD.getMaDH().getMaDH();
            List<ChiTietHoaDon> dsCT = ctDAO.layDanhSachTheoMaDH(maDH);

            String htmlContent = BillPrinter.generateBillHTML(selectedHD, dsCT);
            showPrintPreview(htmlContent);
        });

        pnlControl.add(btnInHoaDon);
        pnl.add(pnlControl, BorderLayout.SOUTH);

        return pnl;
    }

    private JPanel createTableChiTiet() {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setOpaque(false);
        pnl.setBorder(new EmptyBorder(10, 0, 0, 0));

        JLabel lbl = new JLabel("CHI TIẾT SẢN PHẨM TRONG ĐƠN");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pnl.add(lbl, BorderLayout.NORTH);

        String[] cols = {"Sản phẩm (Size)", "Topping", "Số lượng", "Đá", "Đường", "Ghi chú", "Thành tiền"};
        modelCT = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tblChiTiet = new JTable(modelCT);
        tblChiTiet.setRowHeight(35);
        tblChiTiet.getColumnModel().getColumn(1).setPreferredWidth(150);

        pnl.add(new JScrollPane(tblChiTiet), BorderLayout.CENTER);
        return pnl;
    }

    public void loadDataToTableHD() {
        modelHD.setRowCount(0);
        List<HoaDon> ds = hdDAO.layTatCaHoaDon();

        System.out.println("DEBUG: Số lượng hóa đơn lấy được = " + ds.size());

        for (HoaDon hd : ds) {
            modelHD.addRow(new Object[]{
                    hd.getMaHD(),
                    hd.getMaDH() != null ? hd.getMaDH().getMaDH() : "N/A",
                    hd.getNgayThanhToan() != null ? hd.getNgayThanhToan().format(dtFormatter) : "N/A",
                    hd.getPhuongThucTT(),
                    formatter.format(hd.getTongTienCuoi())
            });
        }
    }

    private void loadDataToTableCT(String maDH) {
        modelCT.setRowCount(0);
        List<ChiTietHoaDon> ds = ctDAO.layDanhSachTheoMaDH(maDH);

        for (ChiTietHoaDon ct : ds) {
            String toppingStr = (ct.getDsTopping() != null && !ct.getDsTopping().isEmpty())
                    ? String.join(", ", ct.getDsTopping())
                    : "-";

            modelCT.addRow(new Object[]{
                    ct.getMaSize() != null ? ct.getMaSize().getMaSize() : "N/A",
                    toppingStr,
                    ct.getSoLuong(),
                    ct.getLuongDa(),
                    ct.getLuongDuong(),
                    ct.getGhiChu(),
                    formatter.format(ct.getThanhTien())
            });
        }
    }

    private void showPrintPreview(String html) {
        JEditorPane editorPane = new JEditorPane("text/html", html);
        editorPane.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(editorPane);
        scrollPane.setPreferredSize(new Dimension(350, 500));

        int result = JOptionPane.showConfirmDialog(this, scrollPane, "Xác nhận in hóa đơn",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                editorPane.print();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi in ấn: " + ex.getMessage());
            }
        }
    }
}