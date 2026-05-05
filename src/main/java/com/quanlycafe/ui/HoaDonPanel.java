package com.quanlycafe.ui;

import com.quanlycafe.dao.ChiTietHoaDonDAO;
import com.quanlycafe.dao.HoaDonDAO;
import com.quanlycafe.entity.ChiTietHoaDon;
import com.quanlycafe.entity.HoaDon;
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
    private DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("dd/MM/2026 HH:mm");

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

        // Load dữ liệu lần đầu khi mở panel
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

        // Sự kiện click chọn dòng trên bảng Hóa Đơn
        tblHoaDon.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tblHoaDon.getSelectedRow();
                if (row != -1) {
                    // Lấy MaDH (cột 1) để truy vấn chi tiết món
                    String maDH = modelHD.getValueAt(row, 1).toString();
                    loadDataToTableCT(maDH);
                }
            }
        });

        pnl.add(new JScrollPane(tblHoaDon), BorderLayout.CENTER);
        return pnl;
    }

    private JPanel createTableChiTiet() {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setOpaque(false);
        pnl.setBorder(new EmptyBorder(10, 0, 0, 0));

        JLabel lbl = new JLabel("CHI TIẾT SẢN PHẨM TRONG ĐƠN");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pnl.add(lbl, BorderLayout.NORTH);

        String[] cols = {"Sản phẩm (Size)", "Số lượng", "Đá", "Đường", "Ghi chú", "Thành tiền"};
        modelCT = new DefaultTableModel(cols, 0);
        tblChiTiet = new JTable(modelCT);
        tblChiTiet.setRowHeight(30);

        pnl.add(new JScrollPane(tblChiTiet), BorderLayout.CENTER);
        return pnl;
    }

    // Hàm lấy danh sách hóa đơn từ Database
    public void loadDataToTableHD() {
        modelHD.setRowCount(0);
        List<HoaDon> ds = hdDAO.layTatCaHoaDon();
        for (HoaDon hd : ds) {
            modelHD.addRow(new Object[]{
                    hd.getMaHD(),
                    hd.getMaDH().getMaDH(),
                    hd.getNgayThanhToan().format(dtFormatter),
                    hd.getPhuongThucTT(),
                    formatter.format(hd.getTongTienCuoi())
            });
        }
    }

    // Hàm lấy chi tiết món của Đơn hàng tương ứng
    private void loadDataToTableCT(String maDH) {
        modelCT.setRowCount(0);
        List<ChiTietHoaDon> ds = ctDAO.layDanhSachTheoMaDH(maDH);
        for (ChiTietHoaDon ct : ds) {
            modelCT.addRow(new Object[]{
                    ct.getMaSize().getMaSize(), // Sau này Bảo join bảng SanPham để lấy tên SP nhé
                    ct.getSoLuong(),
                    ct.getLuongDa(),
                    ct.getLuongDuong(),
                    ct.getGhiChu(),
                    formatter.format(ct.getThanhTien())
            });
        }
    }
}