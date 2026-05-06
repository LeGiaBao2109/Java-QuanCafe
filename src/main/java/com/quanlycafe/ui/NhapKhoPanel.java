package com.quanlycafe.ui;

import com.quanlycafe.dao.PhieuNhapDAO;
import com.quanlycafe.dao.SanPhamDAO;
import com.quanlycafe.entity.PhieuNhap;
import com.quanlycafe.entity.SanPham;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NhapKhoPanel extends JPanel {

    private final Color COLOR_BG = new Color(253, 248, 245);
    private final Color COLOR_SURFACE = Color.WHITE;
    private final Color COLOR_PRIMARY = new Color(92, 64, 51);
    private final Color COLOR_BORDER = new Color(230, 220, 210);

    private JTable tblSanPham, tblLichSuNhap;
    private DefaultTableModel modelSanPham, modelLichSu;
    private JTextField txtTimKiem;
    private DecimalFormat formatter = new DecimalFormat("#,###");
    
    private String tenNhanVien;
    private String maNhanVien;
    private PhieuNhapDAO phieuNhapDAO;

    private JPanel pnlInputDock;
    private JLabel lblDockTitle;
    private JComboBox<String> cbxNCC;
    private JTextField txtSL, txtGia;
    private JLabel lblTongTienDock;
    
    private String currentMaSP = "";
    private String currentTenSP = "";
    
    private List<Object[]> dsLichSuRaw;

    public NhapKhoPanel(String tenNV) {
        this.tenNhanVien = tenNV;
        this.phieuNhapDAO = new PhieuNhapDAO();
        this.maNhanVien = phieuNhapDAO.layMaNVTheoTen(tenNV); 

        setLayout(new BorderLayout(15, 15));
        setBackground(COLOR_BG);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(createLeftPanel());
        splitPane.setRightComponent(createRightPanel());
        splitPane.setResizeWeight(0.40); 
        splitPane.setBorder(null);
        splitPane.setOpaque(false);

        add(splitPane, BorderLayout.CENTER);
        
        initInputDock();
        add(pnlInputDock, BorderLayout.SOUTH);

        loadDataKho();
        loadLichSuNhapKho();
    }

    private JPanel createLeftPanel() {
        JPanel pnl = new JPanel(new BorderLayout(0, 10));
        pnl.setBackground(COLOR_SURFACE);
        pnl.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COLOR_BORDER), "Danh Sách Sản Phẩm (Click đúp để nhập kho)",
                TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 14), COLOR_PRIMARY));

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlSearch.setBackground(COLOR_SURFACE);
        txtTimKiem = new JTextField(20);
        txtTimKiem.setPreferredSize(new Dimension(200, 35));
        
        JButton btnTim = new JButton("Tìm Kiếm");
        btnTim.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnTim.setBackground(COLOR_PRIMARY); 
        btnTim.setForeground(Color.WHITE);
        btnTim.setOpaque(true);
        btnTim.setContentAreaFilled(true);
        btnTim.setBorderPainted(false);
        btnTim.setFocusPainted(false);
        btnTim.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTim.setPreferredSize(new Dimension(150, 35));

        btnTim.addActionListener(e -> thucHienTimKiem());
        txtTimKiem.addActionListener(e -> thucHienTimKiem());

        pnlSearch.add(new JLabel("Tìm kiếm: "));
        pnlSearch.add(txtTimKiem);
        pnlSearch.add(btnTim);

        modelSanPham = new DefaultTableModel(null, new String[]{"Mã SP", "Tên Sản Phẩm", "Tồn Kho"}) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblSanPham = new JTable(modelSanPham);
        styleTable(tblSanPham);
        
        tblSanPham.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) triggerInputDock();
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
                BorderFactory.createLineBorder(COLOR_BORDER), "Lịch Sử Nhập Kho (Click đúp để xem chi tiết)",
                TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 14), COLOR_PRIMARY));

        String[] cols = {"Mã Phiếu", "Sản Phẩm", "Nhà Cung Cấp", "SL", "Thành Tiền", "Nhân Viên"};
        modelLichSu = new DefaultTableModel(null, cols) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblLichSuNhap = new JTable(modelLichSu);
        styleTable(tblLichSuNhap);
        
        tblLichSuNhap.getColumnModel().getColumn(0).setMaxWidth(90);
        tblLichSuNhap.getColumnModel().getColumn(3).setMaxWidth(60);
        
        tblLichSuNhap.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tblLichSuNhap.getSelectedRow();
                    if (row != -1 && dsLichSuRaw != null) {
                        showChiTietLichSuDialog(row);
                    }
                }
            }
        });

        pnl.add(new JScrollPane(tblLichSuNhap), BorderLayout.CENTER);
        return pnl;
    }

    private void initInputDock() {
        pnlInputDock = new JPanel(new BorderLayout(15, 15));
        pnlInputDock.setBackground(COLOR_SURFACE);
        pnlInputDock.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(4, 0, 0, 0, COLOR_PRIMARY),
                new EmptyBorder(10, 20, 15, 20)
        ));

        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setOpaque(false);
        lblDockTitle = new JLabel("Chưa chọn sản phẩm");
        lblDockTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblDockTitle.setForeground(COLOR_PRIMARY);

        JButton btnClose = new JButton("Đóng");
        btnClose.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnClose.setBackground(new Color(220, 53, 69));
        btnClose.setForeground(Color.WHITE);
        btnClose.setOpaque(true);
        btnClose.setContentAreaFilled(true);
        btnClose.setBorderPainted(false);
        btnClose.setFocusPainted(false);
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClose.addActionListener(e -> pnlInputDock.setVisible(false));

        pnlHeader.add(lblDockTitle, BorderLayout.WEST);
        pnlHeader.add(btnClose, BorderLayout.EAST);

        JPanel pnlBody = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        pnlBody.setOpaque(false);

        JPanel pnlNCC = new JPanel(new BorderLayout(5, 5));
        pnlNCC.setOpaque(false);
        JLabel lblNCC = new JLabel("Nhà Cung Cấp");
        lblNCC.setFont(new Font("Segoe UI", Font.BOLD, 13));
        cbxNCC = new JComboBox<>();
        cbxNCC.setEditable(true);
        cbxNCC.setPreferredSize(new Dimension(220, 38));
        pnlNCC.add(lblNCC, BorderLayout.NORTH);
        pnlNCC.add(cbxNCC, BorderLayout.CENTER);

        JPanel pnlSL = new JPanel(new BorderLayout(5, 5));
        pnlSL.setOpaque(false);
        JLabel lblSL = new JLabel("Số Lượng");
        lblSL.setFont(new Font("Segoe UI", Font.BOLD, 13));
        txtSL = new JTextField();
        txtSL.setFont(new Font("Segoe UI", Font.BOLD, 15));
        txtSL.setPreferredSize(new Dimension(80, 38));
        pnlSL.add(lblSL, BorderLayout.NORTH);
        pnlSL.add(txtSL, BorderLayout.CENTER);

        JPanel pnlGia = new JPanel(new BorderLayout(5, 5));
        pnlGia.setOpaque(false);
        JLabel lblGia = new JLabel("Đơn Giá Nhập (đ)");
        lblGia.setFont(new Font("Segoe UI", Font.BOLD, 13));
        txtGia = new JTextField();
        txtGia.setFont(new Font("Segoe UI", Font.BOLD, 15));
        txtGia.setPreferredSize(new Dimension(130, 38));
        pnlGia.add(lblGia, BorderLayout.NORTH);
        pnlGia.add(txtGia, BorderLayout.CENTER);

        JPanel pnlTien = new JPanel(new BorderLayout(5, 5));
        pnlTien.setOpaque(false);
        JLabel lblTien = new JLabel("Tổng Tiền");
        lblTien.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTongTienDock = new JLabel("0 đ");
        lblTongTienDock.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTongTienDock.setForeground(COLOR_PRIMARY);
        lblTongTienDock.setPreferredSize(new Dimension(160, 38));
        pnlTien.add(lblTien, BorderLayout.NORTH);
        pnlTien.add(lblTongTienDock, BorderLayout.CENTER);

        JButton btnXacNhan = new JButton("XÁC NHẬN NHẬP KHO");
        btnXacNhan.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnXacNhan.setBackground(COLOR_PRIMARY);
        btnXacNhan.setForeground(Color.WHITE);
        btnXacNhan.setOpaque(true);
        btnXacNhan.setContentAreaFilled(true);
        btnXacNhan.setBorderPainted(false);
        btnXacNhan.setFocusPainted(false);
        btnXacNhan.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnXacNhan.setPreferredSize(new Dimension(200, 38)); 
        btnXacNhan.addActionListener(e -> xuLyNhapKhoDock());

        JPanel pnlAction = new JPanel(new BorderLayout());
        pnlAction.setOpaque(false);
        pnlAction.add(btnXacNhan, BorderLayout.SOUTH);

        DocumentListener dl = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { update(); }
            public void removeUpdate(DocumentEvent e) { update(); }
            public void changedUpdate(DocumentEvent e) { update(); }
            private void update() {
                try {
                    int sl = Integer.parseInt(txtSL.getText());
                    double gia = Double.parseDouble(txtGia.getText());
                    lblTongTienDock.setText(formatter.format(sl * gia) + " đ");
                } catch (Exception ex) {
                    lblTongTienDock.setText("0 đ");
                }
            }
        };
        txtSL.getDocument().addDocumentListener(dl);
        txtGia.getDocument().addDocumentListener(dl);

        pnlBody.add(pnlNCC);
        pnlBody.add(pnlSL);
        pnlBody.add(pnlGia);
        pnlBody.add(pnlTien);

        pnlInputDock.add(pnlHeader, BorderLayout.NORTH);
        pnlInputDock.add(pnlBody, BorderLayout.CENTER);
        pnlInputDock.add(pnlAction, BorderLayout.EAST); 

        pnlInputDock.setVisible(false);
    }

    private void triggerInputDock() {
        int row = tblSanPham.getSelectedRow();
        if (row == -1) return;

        currentMaSP = modelSanPham.getValueAt(row, 0).toString();
        currentTenSP = modelSanPham.getValueAt(row, 1).toString();
        String tonKho = modelSanPham.getValueAt(row, 2).toString();

        lblDockTitle.setText("Đang nhập: " + currentTenSP + " (" + currentMaSP + ")  |  Tồn kho hiện tại: " + tonKho);
        txtSL.setText("");
        txtGia.setText("");
        lblTongTienDock.setText("0 đ");

        cbxNCC.removeAllItems();
        for (String ncc : phieuNhapDAO.layDanhSachNhaCungCap()) cbxNCC.addItem(ncc);

        pnlInputDock.setVisible(true);
        revalidate();
        repaint();
    }

    private void xuLyNhapKhoDock() {
        try {
            String ncc = cbxNCC.getSelectedItem() != null ? cbxNCC.getSelectedItem().toString().trim() : "";
            if (ncc.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn hoặc nhập Nhà cung cấp!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int sl = Integer.parseInt(txtSL.getText());
            double gia = Double.parseDouble(txtGia.getText());
            if (sl <= 0 || gia <= 0) throw new Exception();
            
            double thanhTien = sl * gia;
            
            PhieuNhap pn = new PhieuNhap();
            pn.setMaPhieu(phieuNhapDAO.taoMaPhieuMoi());
            pn.setTenNCC(ncc);
            pn.setNgayNhap(LocalDateTime.now());
            pn.setTongTienNhap(thanhTien);
            
            List<Object[]> chiTiet = new ArrayList<>();
            chiTiet.add(new Object[]{currentMaSP, currentTenSP, sl, gia});
            
            boolean success = phieuNhapDAO.thucHienNhapKho(pn, chiTiet, maNhanVien);
            if (success) {
                JOptionPane.showMessageDialog(this, "Đã nhập kho thành công sản phẩm: " + currentTenSP, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                pnlInputDock.setVisible(false);
                loadDataKho(); 
                loadLichSuNhapKho(); 
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi lưu dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }

        } catch(Exception ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số lượng và đơn giá hợp lệ!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showChiTietLichSuDialog(int rowIndex) {
        Object[] data = dsLichSuRaw.get(rowIndex);
        String maPhieu = data[0].toString();
        String tenSP = data[1].toString();
        String ncc = data[2].toString();
        String sl = data[3].toString();
        String giaNhap = formatter.format(data[4]) + " đ";
        String thanhTien = formatter.format(data[5]) + " đ";
        
        Timestamp ts = (Timestamp) data[6];
        String ngayNhap = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(ts);
        String tenNV = data[7].toString();

        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Chi Tiết Phiếu Nhập", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(450, 480);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(COLOR_SURFACE);

        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(COLOR_PRIMARY);
        pnlHeader.setBorder(new EmptyBorder(15, 20, 15, 20));
        JLabel lblTitle = new JLabel("THÔNG TIN PHIẾU: " + maPhieu);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);
        pnlHeader.add(lblTitle, BorderLayout.CENTER);

        JPanel pnlBody = new JPanel(new GridLayout(7, 2, 10, 20));
        pnlBody.setBackground(COLOR_SURFACE);
        pnlBody.setBorder(new EmptyBorder(25, 30, 20, 30));

        addDetailRow(pnlBody, "Ngày nhập:", ngayNhap, false);
        addDetailRow(pnlBody, "Nhân viên:", tenNV, false);
        addDetailRow(pnlBody, "Sản phẩm:", tenSP, true);
        addDetailRow(pnlBody, "Nhà cung cấp:", ncc, true);
        addDetailRow(pnlBody, "Số lượng:", sl, true);
        addDetailRow(pnlBody, "Đơn giá:", giaNhap, false);
        addDetailRow(pnlBody, "Thành tiền:", thanhTien, true);

        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlFooter.setBackground(new Color(245, 245, 245));
        pnlFooter.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, COLOR_BORDER));
        
        JButton btnClose = new JButton("Đóng");
        btnClose.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnClose.setBackground(COLOR_PRIMARY);
        btnClose.setForeground(Color.WHITE);
        btnClose.setOpaque(true);
        btnClose.setContentAreaFilled(true);
        btnClose.setBorderPainted(false);
        btnClose.setFocusPainted(false);
        btnClose.setPreferredSize(new Dimension(100, 35));
        btnClose.addActionListener(e -> dialog.dispose());
        
        pnlFooter.add(btnClose);

        dialog.add(pnlHeader, BorderLayout.NORTH);
        dialog.add(pnlBody, BorderLayout.CENTER);
        dialog.add(pnlFooter, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void addDetailRow(JPanel pnl, String label, String value, boolean isBold) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl.setForeground(Color.GRAY);
        
        JLabel val = new JLabel(value);
        val.setFont(new Font("Segoe UI", isBold ? Font.BOLD : Font.PLAIN, 14));
        val.setForeground(isBold ? COLOR_PRIMARY : Color.DARK_GRAY);
        
        pnl.add(lbl);
        pnl.add(val);
    }

    private void loadDataKho() {
        modelSanPham.setRowCount(0);
        SanPhamDAO dao = new SanPhamDAO();
        for (SanPham sp : dao.layTatCaSanPham()) {
            if(sp.isTrangThai()) modelSanPham.addRow(new Object[]{sp.getMaSP(), sp.getTenSP(), sp.getTonKho()});
        }
    }
    
    private void loadLichSuNhapKho() {
        modelLichSu.setRowCount(0);
        dsLichSuRaw = phieuNhapDAO.layLichSuNhapKho();
        for (Object[] row : dsLichSuRaw) {
            String tien = formatter.format(row[5]) + " đ";
            modelLichSu.addRow(new Object[]{row[0], row[1], row[2], row[3], tien, row[7]});
        }
    }

    private void thucHienTimKiem() {
        String kw = txtTimKiem.getText().trim().toLowerCase();
        modelSanPham.setRowCount(0);
        for (SanPham sp : new SanPhamDAO().layTatCaSanPham()) {
            if(sp.isTrangThai() && (kw.isEmpty() || sp.getMaSP().toLowerCase().contains(kw) || sp.getTenSP().toLowerCase().contains(kw))) {
                modelSanPham.addRow(new Object[]{sp.getMaSP(), sp.getTenSP(), sp.getTonKho()});
            }
        }
    }

    private void styleTable(JTable tb) {
        tb.setRowHeight(38);
        tb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tb.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tb.getTableHeader().setBackground(new Color(245, 240, 235));
        tb.getTableHeader().setForeground(COLOR_PRIMARY);
        tb.setSelectionBackground(new Color(235, 225, 215));
        tb.setSelectionForeground(COLOR_PRIMARY);
        tb.setShowVerticalLines(false);
        tb.setGridColor(COLOR_BORDER);
        
        ((DefaultTableCellRenderer)tb.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tb.getColumnCount(); i++) tb.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
    }
}