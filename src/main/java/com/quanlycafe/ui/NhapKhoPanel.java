package com.quanlycafe.ui;

import com.quanlycafe.dao.PhieuNhapDAO;
import com.quanlycafe.dao.SanPhamDAO;
import com.quanlycafe.entity.PhieuNhap;
import com.quanlycafe.entity.SanPham;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NhapKhoPanel extends JPanel {

    private final Color COLOR_BG = new Color(253, 248, 245);
    private final Color COLOR_SURFACE = Color.WHITE;
    private final Color COLOR_PRIMARY = new Color(92, 64, 51);
    private final Color COLOR_BORDER = new Color(230, 220, 210);

    private JTable tblSanPham, tblChiTietNhap;
    private DefaultTableModel modelSanPham, modelChiTiet;
    private JComboBox<String> cbxNhaCungCap;
    private JTextField txtTimKiem;
    private JLabel lblTongTien;
    private double tongTienNhap = 0;
    private DecimalFormat formatter = new DecimalFormat("#,###");

    public NhapKhoPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(COLOR_BG);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(createLeftPanel());
        splitPane.setRightComponent(createRightPanel());
        splitPane.setResizeWeight(0.45);
        splitPane.setBorder(null);

        add(splitPane, BorderLayout.CENTER);
        loadDataKho();
        loadDanhSachNhaCungCap();
    }

    private JPanel createLeftPanel() {
        JPanel pnl = new JPanel(new BorderLayout(0, 10));
        pnl.setBackground(COLOR_SURFACE);
        pnl.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COLOR_BORDER), "Danh Sách Sản Phẩm",
                TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 14), COLOR_PRIMARY));

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlSearch.setBackground(COLOR_SURFACE);
        txtTimKiem = new JTextField(20);
        txtTimKiem.setPreferredSize(new Dimension(200, 35));
        
        JButton btnTim = new JButton("Tìm");
        btnTim.setBackground(COLOR_PRIMARY); 
        btnTim.setForeground(Color.WHITE);
        btnTim.setOpaque(true);
        btnTim.setContentAreaFilled(true);
        btnTim.setBorderPainted(false);
        btnTim.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnTim.addActionListener(e -> thucHienTimKiem());
        txtTimKiem.addActionListener(e -> thucHienTimKiem());

        pnlSearch.add(new JLabel("Tìm kiếm: "));
        pnlSearch.add(txtTimKiem);
        pnlSearch.add(btnTim);

        String[] cols = {"Mã SP", "Tên Sản Phẩm", "Tồn Kho"};
        modelSanPham = new DefaultTableModel(null, cols) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblSanPham = new JTable(modelSanPham);
        styleTable(tblSanPham);
        
        tblSanPham.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    chonSanPhamNhap();
                }
            }
        });

        pnl.add(pnlSearch, BorderLayout.NORTH);
        pnl.add(new JScrollPane(tblSanPham), BorderLayout.CENTER);
        return pnl;
    }

    private JPanel createRightPanel() {
        JPanel pnl = new JPanel(new BorderLayout(0, 10));
        pnl.setBackground(COLOR_SURFACE);
        pnl.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COLOR_BORDER), "Chi Tiết Phiếu Nhập",
                TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 14), COLOR_PRIMARY));

        String[] cols = {"Mã SP", "Tên SP", "Số Lượng", "Giá Nhập", "Thành Tiền"};
        modelChiTiet = new DefaultTableModel(null, cols) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblChiTietNhap = new JTable(modelChiTiet);
        styleTable(tblChiTietNhap);
        
        JPopupMenu popup = new JPopupMenu();
        JMenuItem mnuXoa = new JMenuItem("Xóa khỏi phiếu nhập");
        mnuXoa.addActionListener(e -> xoaMonKhoiPhieu());
        popup.add(mnuXoa);
        tblChiTietNhap.setComponentPopupMenu(popup);

        JPanel pnlBottom = new JPanel(new GridLayout(3, 1, 5, 5));
        pnlBottom.setBackground(COLOR_SURFACE);
        pnlBottom.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel row1 = new JPanel(new BorderLayout());
        row1.setBackground(COLOR_SURFACE);
        row1.add(new JLabel("Nhà cung cấp: "), BorderLayout.WEST);
        
        cbxNhaCungCap = new JComboBox<>();
        cbxNhaCungCap.setEditable(true);
        cbxNhaCungCap.setBackground(Color.WHITE);
        row1.add(cbxNhaCungCap, BorderLayout.CENTER);

        JPanel row2 = new JPanel(new BorderLayout());
        row2.setBackground(COLOR_SURFACE);
        JLabel lblTitleTotal = new JLabel("Tổng tiền nhập: ");
        lblTitleTotal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTongTien = new JLabel("0 đ");
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTongTien.setForeground(new Color(220, 53, 69));
        row2.add(lblTitleTotal, BorderLayout.WEST);
        row2.add(lblTongTien, BorderLayout.EAST);

        JButton btnXacNhan = new JButton("XÁC NHẬN NHẬP KHO");
        btnXacNhan.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnXacNhan.setBackground(new Color(40, 167, 69));
        btnXacNhan.setForeground(Color.WHITE);
        btnXacNhan.setPreferredSize(new Dimension(0, 45));
        btnXacNhan.setOpaque(true);
        btnXacNhan.setContentAreaFilled(true);
        btnXacNhan.setBorderPainted(false);
        btnXacNhan.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnXacNhan.addActionListener(e -> xuLyNhapKho());

        pnlBottom.add(row1);
        pnlBottom.add(row2);
        pnlBottom.add(btnXacNhan);

        pnl.add(new JScrollPane(tblChiTietNhap), BorderLayout.CENTER);
        pnl.add(pnlBottom, BorderLayout.SOUTH);
        return pnl;
    }

    private void loadDataKho() {
        modelSanPham.setRowCount(0);
        SanPhamDAO dao = new SanPhamDAO();
        List<SanPham> ds = dao.layTatCaSanPham();
        for (SanPham sp : ds) {
            if(sp.isTrangThai()) {
                modelSanPham.addRow(new Object[]{sp.getMaSP(), sp.getTenSP(), sp.getTonKho()});
            }
        }
    }

    private void thucHienTimKiem() {
        String kw = txtTimKiem.getText().trim().toLowerCase();
        modelSanPham.setRowCount(0);
        SanPhamDAO dao = new SanPhamDAO();
        List<SanPham> ds = dao.layTatCaSanPham();
        for (SanPham sp : ds) {
            if(sp.isTrangThai()) {
                if (kw.isEmpty() || sp.getMaSP().toLowerCase().contains(kw) || sp.getTenSP().toLowerCase().contains(kw)) {
                    modelSanPham.addRow(new Object[]{sp.getMaSP(), sp.getTenSP(), sp.getTonKho()});
                }
            }
        }
    }

    private void loadDanhSachNhaCungCap() {
        cbxNhaCungCap.removeAllItems();
        PhieuNhapDAO dao = new PhieuNhapDAO();
        List<String> dsNCC = dao.layDanhSachNhaCungCap();
        for (String ncc : dsNCC) {
            cbxNhaCungCap.addItem(ncc);
        }
        cbxNhaCungCap.setSelectedItem("");
    }

    private void chonSanPhamNhap() {
        int row = tblSanPham.getSelectedRow();
        if (row == -1) return;

        String maSP = modelSanPham.getValueAt(row, 0).toString();
        String tenSP = modelSanPham.getValueAt(row, 1).toString();

        JPanel pnlInput = new JPanel(new GridLayout(2, 2, 10, 10));
        JTextField txtSoLuong = new JTextField();
        JTextField txtGiaNhap = new JTextField();
        pnlInput.add(new JLabel("Số lượng nhập:"));
        pnlInput.add(txtSoLuong);
        pnlInput.add(new JLabel("Đơn giá nhập (đ):"));
        pnlInput.add(txtGiaNhap);

        int result = JOptionPane.showConfirmDialog(this, pnlInput, "Nhập kho: " + tenSP, JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int sl = Integer.parseInt(txtSoLuong.getText());
                double gia = Double.parseDouble(txtGiaNhap.getText());
                double thanhTien = sl * gia;

                modelChiTiet.addRow(new Object[]{maSP, tenSP, sl, formatter.format(gia), formatter.format(thanhTien)});
                
                tongTienNhap += thanhTien;
                lblTongTien.setText(formatter.format(tongTienNhap) + " đ");
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập số hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void xoaMonKhoiPhieu() {
        int row = tblChiTietNhap.getSelectedRow();
        if (row != -1) {
            String strThanhTien = modelChiTiet.getValueAt(row, 4).toString().replace(",", "");
            double thanhTien = Double.parseDouble(strThanhTien);
            
            tongTienNhap -= thanhTien;
            lblTongTien.setText(formatter.format(tongTienNhap) + " đ");
            
            modelChiTiet.removeRow(row);
        }
    }

    private void xuLyNhapKho() {
        if (modelChiTiet.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Chưa có sản phẩm nào để nhập!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String ncc = cbxNhaCungCap.getSelectedItem() != null ? cbxNhaCungCap.getSelectedItem().toString().trim() : "";
        if (ncc.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên nhà cung cấp!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Object[]> chiTiet = new ArrayList<>();
        for (int i = 0; i < modelChiTiet.getRowCount(); i++) {
            String ma = modelChiTiet.getValueAt(i, 0).toString();
            String sl = modelChiTiet.getValueAt(i, 2).toString();
            String giaStr = modelChiTiet.getValueAt(i, 3).toString().replace(",", "");
            
            chiTiet.add(new Object[]{ma, modelChiTiet.getValueAt(i, 1).toString(), sl, giaStr});
        }

        PhieuNhapDAO dao = new PhieuNhapDAO();
        PhieuNhap pn = new PhieuNhap();
        pn.setMaPhieu(dao.taoMaPhieuMoi());
        pn.setTenNCC(ncc);
        pn.setNgayNhap(LocalDateTime.now());
        pn.setTongTienNhap(tongTienNhap);

        boolean success = dao.thucHienNhapKho(pn, chiTiet);

        if (success) {
            JOptionPane.showMessageDialog(this, "Nhập kho thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            modelChiTiet.setRowCount(0);
            tongTienNhap = 0;
            lblTongTien.setText("0 đ");
            loadDanhSachNhaCungCap(); 
            loadDataKho(); 
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi khi nhập kho!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void styleTable(JTable tb) {
        tb.setRowHeight(35);
        tb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tb.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tb.getTableHeader().setBackground(new Color(245, 240, 235));
        tb.setSelectionBackground(new Color(235, 225, 215));
        tb.setSelectionForeground(COLOR_PRIMARY);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tb.getColumnCount(); i++) tb.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
    }
}