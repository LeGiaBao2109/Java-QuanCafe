package com.quanlycafe.ui;

import com.quanlycafe.dao.ChiTietHoaDonDAO;
import com.quanlycafe.dao.HoaDonDAO;
import com.quanlycafe.entity.ChiTietHoaDon;
import com.quanlycafe.entity.HoaDon;
import com.quanlycafe.util.BillPrinter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HoaDonPanel extends JPanel {
    private JTable tblHoaDon, tblChiTiet;
    private DefaultTableModel modelHD, modelCT;
    private DecimalFormat formatter = new DecimalFormat("#,###đ");
    private DateTimeFormatter dFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private DateTimeFormatter tFormatter = DateTimeFormatter.ofPattern("HH:mm");
    private JButton btnInHoaDon;
    private JButton btnReload;

    private Color primaryColor = new Color(91, 67, 56);
    private Color backgroundColor = new Color(253, 248, 245);
    private Color headerBgColor = new Color(242, 236, 231);
    private Color borderColor = new Color(211, 201, 194);

    private HoaDonDAO hdDAO = new HoaDonDAO();
    private ChiTietHoaDonDAO ctDAO = new ChiTietHoaDonDAO();

    public HoaDonPanel() {
        setLayout(new BorderLayout());
        setBackground(backgroundColor);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(createTableHoaDon());
        splitPane.setBottomComponent(createTableChiTiet());
        splitPane.setDividerLocation(350);
        splitPane.setResizeWeight(0.5);
        splitPane.setDividerSize(5);
        splitPane.setBorder(null);
        splitPane.setBackground(backgroundColor);

        add(splitPane, BorderLayout.CENTER);

        loadDataToTableHD();
    }

    private JPanel createTableHoaDon() {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setOpaque(false);

        JLabel lbl = new JLabel("LỊCH SỬ GIAO DỊCH");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lbl.setForeground(primaryColor);
        lbl.setBorder(new EmptyBorder(0, 0, 15, 0));
        pnl.add(lbl, BorderLayout.NORTH);

        String[] cols = {"Mã HD", "Ngày", "Giờ", "Giá ban đầu", "Giảm giá", "Tổng", "PTTT"};
        modelHD = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tblHoaDon = new JTable(modelHD);
        styleTable(tblHoaDon);

        JScrollPane scroll = new JScrollPane(tblHoaDon);
        scroll.setBorder(new LineBorder(borderColor));
        scroll.getViewport().setBackground(Color.WHITE);
        pnl.add(scroll, BorderLayout.CENTER);

        tblHoaDon.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tblHoaDon.getSelectedRow();
                if (row != -1) {
                    List<HoaDon> ds = hdDAO.layTatCaHoaDon();
                    String maDH = ds.get(row).getMaDH().getMaDH();
                    loadDataToTableCT(maDH);
                }
            }
        });

        JPanel pnlControl = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlControl.setOpaque(false);
        pnlControl.setBorder(new EmptyBorder(10, 0, 0, 0));

        btnReload = new JButton("Làm mới");
        btnReload.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnReload.setBackground(new Color(240, 240, 240));
        btnReload.setForeground(primaryColor);
        btnReload.setFocusPainted(false);
        btnReload.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnReload.addActionListener(e -> {
            loadDataToTableHD();
            modelCT.setRowCount(0);
            JOptionPane.showMessageDialog(this, "Đã cập nhật danh sách hóa đơn!");
        });

        btnInHoaDon = new JButton("In Hóa Đơn");
        btnInHoaDon.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnInHoaDon.setBackground(new Color(91, 67, 56));
        btnInHoaDon.setForeground(primaryColor);
        btnInHoaDon.setFocusPainted(false);
        btnInHoaDon.setCursor(new Cursor(Cursor.HAND_CURSOR));

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

        pnlControl.add(btnReload);
        pnlControl.add(btnInHoaDon);
        pnl.add(pnlControl, BorderLayout.SOUTH);

        return pnl;
    }

    private JPanel createTableChiTiet() {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setOpaque(false);
        pnl.setBorder(new EmptyBorder(20, 0, 0, 0));

        JLabel lbl = new JLabel("CHI TIẾT SẢN PHẨM TRONG ĐƠN");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(primaryColor);
        lbl.setBorder(new EmptyBorder(0, 0, 10, 0));
        pnl.add(lbl, BorderLayout.NORTH);

        String[] cols = {"Sản phẩm (Size)", "Topping", "Số lượng", "Đá", "Đường", "Ghi chú", "Giá Gốc"};
        modelCT = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tblChiTiet = new JTable(modelCT);
        styleTable(tblChiTiet);
        tblChiTiet.getColumnModel().getColumn(1).setPreferredWidth(250);

        JScrollPane scroll = new JScrollPane(tblChiTiet);
        scroll.setBorder(new LineBorder(borderColor));
        scroll.getViewport().setBackground(Color.WHITE);
        pnl.add(scroll, BorderLayout.CENTER);
        return pnl;
    }

    private void styleTable(JTable table) {
        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionBackground(new Color(245, 240, 235));
        table.setSelectionForeground(Color.BLACK);
        table.setShowGrid(true);
        table.setGridColor(borderColor);
        table.setIntercellSpacing(new Dimension(0, 0));

        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(100, 45));
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(headerBgColor);
        header.setForeground(primaryColor);
        header.setBorder(new LineBorder(borderColor));

        ((DefaultTableCellRenderer)header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            if(i != 0 && i != 1) {
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }
    }

    public void loadDataToTableHD() {
        modelHD.setRowCount(0);
        List<HoaDon> ds = hdDAO.layTatCaHoaDon();

        for (HoaDon hd : ds) {
            String maDH = hd.getMaDH().getMaDH();
            double giaBanDau = ctDAO.tinhTongTienDonHang(maDH);
            double tong = hd.getTongTienCuoi();
            double giaGiam = giaBanDau - tong;

            modelHD.addRow(new Object[]{
                    hd.getMaHD(),
                    hd.getNgayThanhToan() != null ? hd.getNgayThanhToan().format(dFormatter) : "N/A",
                    hd.getNgayThanhToan() != null ? hd.getNgayThanhToan().format(tFormatter) : "N/A",
                    formatter.format(giaBanDau),
                    formatter.format(giaGiam > 0 ? giaGiam : 0),
                    formatter.format(tong),
                    hd.getPhuongThucTT() != null ? hd.getPhuongThucTT().getMoTa() : "N/A"
            });
        }
    }

    private void loadDataToTableCT(String maDH) {
        modelCT.setRowCount(0);
        List<ChiTietHoaDon> ds = ctDAO.layDanhSachTheoMaDH(maDH);

        for (ChiTietHoaDon ct : ds) {
            String maSize = ct.getMaSize();
            String sizeChu = maSize;
            if ("S01".equalsIgnoreCase(maSize)) sizeChu = "M";
            else if ("S07".equalsIgnoreCase(maSize)) sizeChu = "L";
            else if ("S02".equalsIgnoreCase(maSize)) sizeChu = "Tiêu chuẩn";

            String tenMonGoc = "Sản phẩm";
            String ghiChuHienThi = "";

            if (ct.getGhiChu() != null && ct.getGhiChu().contains(" | ")) {
                String[] parts = ct.getGhiChu().split(" \\| ");
                tenMonGoc = parts[0];
                if (tenMonGoc.contains("(Size")) {
                    tenMonGoc = tenMonGoc.substring(0, tenMonGoc.indexOf("(Size")).trim();
                }
                ghiChuHienThi = parts.length > 1 ? parts[1] : "";
            }

            modelCT.addRow(new Object[]{
                    tenMonGoc + " (Size " + sizeChu + ")",
                    String.join(", ", ct.getDsTopping()),
                    ct.getSoLuong(),
                    ct.getLuongDa() != null ? ct.getLuongDa().getLabel() : "100%",
                    ct.getLuongDuong() != null ? ct.getLuongDuong().getLabel() : "100%",
                    ghiChuHienThi,
                    formatter.format(ct.getThanhTien())
            });
        }
    }

    private void showPrintPreview(String html) {
        JEditorPane editorPane = new JEditorPane("text/html", html);
        editorPane.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(editorPane);
        scrollPane.setPreferredSize(new Dimension(380, 550));
        scrollPane.setBorder(new LineBorder(borderColor));

        UIManager.put("OptionPane.background", backgroundColor);
        UIManager.put("Panel.background", backgroundColor);

        int result = JOptionPane.showConfirmDialog(this, scrollPane, "Xem trước Hóa đơn - KAHVI",
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