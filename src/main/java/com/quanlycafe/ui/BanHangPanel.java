package com.quanlycafe.ui;

import com.quanlycafe.dao.*;
import com.quanlycafe.entity.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BanHangPanel extends JPanel {

    private final Color COLOR_BG = new Color(253, 248, 245);
    private final Color COLOR_BORDER = new Color(222, 204, 190);
    private final Color COLOR_PRIMARY_DARK = new Color(92, 64, 51);
    private final Color COLOR_PRIMARY_LIGHT = new Color(188, 143, 111);
    private final Color COLOR_TEXT_MAIN = new Color(60, 42, 33);
    private final Color COLOR_HEADER = new Color(242, 230, 220);

    private JPanel rightCartPanel;
    private JPanel gridPanel;
    private JSplitPane splitPane;

    private JTable table;
    private DefaultTableModel cartTableModel;
    private JLabel lblTamTinh, lblTongTien, lblGiamGia;

    private List<JButton> tabButtons = new ArrayList<>();
    private DecimalFormat formatter = new DecimalFormat("#,###đ");

    private JCheckBox chkTichDiem;
    private JPanel customerPanel;
    private JTextField txtSdtKH, txtTenKH;
    private KhachHang khachHangHienTai = null;
    private KhachHangDAO khDAO = new KhachHangDAO();
    private Voucher voucherApDung = null;
    private JSpinner spnDiemDung;
    private double giamGiaTuDiem = 0;
    private Map<String, ImageIcon> imageCache = new HashMap<>();

    public BanHangPanel() {
        setLayout(new BorderLayout());
        setBackground(COLOR_BG);

        add(createModernHeader(), BorderLayout.NORTH);

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(createLeftMenuPanel());

        rightCartPanel = createRightCartPanel();
        splitPane.setRightComponent(rightCartPanel);

        splitPane.setDividerLocation(800);
        splitPane.setResizeWeight(0.7);
        splitPane.setBorder(null);

        rightCartPanel.setVisible(false);

        add(splitPane, BorderLayout.CENTER);
    }

    private void loadProducts(String maDM) {
        gridPanel.removeAll();
        SanPhamDAO dao = new SanPhamDAO();
        List<Object[]> dsSanPham = dao.laySanPhamBanHangPOS(maDM);

        for (Object[] sp : dsSanPham) {
            String tenSP = sp[0].toString();
            int gia = Integer.parseInt(sp[1].toString());
            String maSizeReal = sp[2].toString();
            String linkAnh = (sp[3] != null) ? sp[3].toString() : "";
            String maSP = sp[4].toString();

            gridPanel.add(createProductCard(tenSP, gia, maDM, maSizeReal, linkAnh, maSP));
        }
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private JPanel createLeftMenuPanel() {
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(COLOR_BG);

        JPanel topArea = new JPanel(new BorderLayout());
        topArea.setBackground(Color.WHITE);
        topArea.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER));

        gridPanel = new JPanel(new GridLayout(0, 4, 15, 15));
        gridPanel.setOpaque(false);
        gridPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel tabMenu = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        tabMenu.setOpaque(false);

        SanPhamDAO dao = new SanPhamDAO();
        List<Object[]> dsDanhMuc = dao.layDanhSachDanhMuc();

        boolean isFirst = true;
        for (Object[] dm : dsDanhMuc) {
            String maDM = dm[0].toString();
            String tenDM = dm[1].toString();

            JButton btnTab = createTabButton(tenDM, maDM, isFirst);
            tabMenu.add(btnTab);

            if (isFirst) {
                loadProducts(maDM);
                isFirst = false;
            }
        }

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        searchPanel.setOpaque(false);

        JTextField txtSearch = new JTextField("Nhập mã/Tên món cần tìm...", 30);
        txtSearch.setPreferredSize(new Dimension(300, 35));

        JButton btnSearch = new JButton("Tìm kiếm");
        btnSearch.setPreferredSize(new Dimension(100, 35));
        btnSearch.setBackground(COLOR_PRIMARY_LIGHT);
        btnSearch.setForeground(COLOR_TEXT_MAIN);
        btnSearch.setFocusPainted(false);

        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String keyword = txtSearch.getText().trim();
                if (!keyword.isEmpty() && !keyword.contains("🔍")) {
                    searchProducts(keyword);
                }
            }
        });

        btnSearch.addActionListener(e -> {
            String keyword = txtSearch.getText().trim();
            searchProducts(keyword);
        });

        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        topArea.add(tabMenu, BorderLayout.NORTH);
        topArea.add(searchPanel, BorderLayout.CENTER);

        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setOpaque(false);
        wrapperPanel.add(gridPanel, BorderLayout.NORTH);

        JScrollPane scrollMenu = new JScrollPane(wrapperPanel);
        scrollMenu.setBorder(null);
        scrollMenu.setOpaque(false);
        scrollMenu.getViewport().setOpaque(false);
        scrollMenu.getVerticalScrollBar().setUnitIncrement(16);

        leftPanel.add(topArea, BorderLayout.NORTH);
        leftPanel.add(scrollMenu, BorderLayout.CENTER);
        return leftPanel;
    }

    private void searchProducts(String keyword) {
        gridPanel.removeAll();
        SanPhamDAO dao = new SanPhamDAO();
        List<Object[]> dsResults = dao.timKiemSanPham(keyword);

        for (Object[] sp : dsResults) {
            String tenSP = sp[0].toString();
            int gia = Integer.parseInt(sp[1].toString());
            String maSize = sp[2].toString();
            String maDM = sp[3].toString();
            String linkAnh = (sp[4] != null) ? sp[4].toString() : "";
            String maSP = sp[5].toString();

            gridPanel.add(createProductCard(tenSP, gia, maDM, maSize, linkAnh, maSP));
        }

        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private void addToCart(String customName, int quantity, int unitPrice, List<String> toppings, String maDM, String maSize, MucDa da, MucDuong duong) {
        if (!rightCartPanel.isVisible()) {
            rightCartPanel.setVisible(true);
            splitPane.setDividerLocation(800);
            splitPane.setDividerSize(1);
            splitPane.revalidate();
            splitPane.repaint();
        }

        cartTableModel.addRow(new Object[]{
                customName,
                quantity,
                formatter.format(quantity * unitPrice),
                unitPrice,
                toppings,
                maDM,
                maSize,
                da,
                duong
        });

        int newRowIdx = cartTableModel.getRowCount() - 1;
        table.setRowSelectionInterval(newRowIdx, newRowIdx);

        JLabel tempLabel = new JLabel(customName);
        tempLabel.setFont(table.getFont());
        tempLabel.setBorder(new EmptyBorder(15, 5, 15, 5));
        int perfectHeight = tempLabel.getPreferredSize().height;
        table.setRowHeight(newRowIdx, Math.max(perfectHeight, 65));

        recalculateTotal();
    }

    private void recalculateTotal() {
        double tamTinh = 0;
        double giaMonCaoNhat = 0;

        for (int i = 0; i < cartTableModel.getRowCount(); i++) {
            double thanhTien = Double.parseDouble(cartTableModel.getValueAt(i, 2).toString().replaceAll("[^\\d]", ""));
            tamTinh += thanhTien;

            int soLuong = (int) cartTableModel.getValueAt(i, 1);
            double donGia = thanhTien / soLuong;
            if (donGia > giaMonCaoNhat) giaMonCaoNhat = donGia;
        }

        double giamGia = 0;
        if (voucherApDung != null) {
            double giaTriVoucher = voucherApDung.getGiaTriGiam();

            if (voucherApDung.getLoaiGiamGia() == LoaiGiamGia.DOI_MON_FREE) {
                giamGia = giaMonCaoNhat;
            } else if (voucherApDung.getLoaiGiamGia().isLaPhanTram()) {
                giamGia = tamTinh * (giaTriVoucher / 100.0);
            } else {
                giamGia = giaTriVoucher;
            }
        }

        if (giamGia > tamTinh) giamGia = tamTinh;

        double tongTien = tamTinh - giamGia;

        lblTamTinh.setText(formatter.format(tamTinh));
        lblGiamGia.setText("-" + formatter.format(giamGia));
        lblTongTien.setText(formatter.format(tongTien));
    }

    private void resetForm() {
        cartTableModel.setRowCount(0);
        voucherApDung = null;
        khachHangHienTai = null;
        chkTichDiem.setSelected(false);
        customerPanel.setVisible(false);
        txtSdtKH.setText("");
        txtTenKH.setText("");
        recalculateTotal();
    }

    private JPanel createRightCartPanel() {
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, COLOR_BORDER));

        JPanel cartHeader = new JPanel(new BorderLayout());
        cartHeader.setBackground(Color.WHITE);
        cartHeader.setBorder(new EmptyBorder(15, 15, 15, 15));
        JLabel lblTable = new JLabel("GIỎ HÀNG");
        lblTable.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTable.setForeground(COLOR_PRIMARY_DARK);
        cartHeader.add(lblTable, BorderLayout.NORTH);

        String[] columnNames = {"Tên món", "SL", "Thành tiền", "UnitPrice", "ToppingList", "MaDM", "MaSize", "MucDa", "MucDuong", "MaSP"};
        cartTableModel = new DefaultTableModel(null, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(cartTableModel);
        table.setRowHeight(40);
        table.setShowGrid(true);
        table.setGridColor(COLOR_BORDER);
        table.setBackground(Color.WHITE);
        table.setSelectionBackground(new Color(245, 240, 235));
        table.setSelectionForeground(Color.BLACK);
        table.getTableHeader().setBackground(COLOR_HEADER);
        table.getTableHeader().setForeground(COLOR_PRIMARY_DARK);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.getColumnModel().getColumn(0).setPreferredWidth(250);
        table.getColumnModel().getColumn(1).setMinWidth(40);
        table.getColumnModel().getColumn(1).setMaxWidth(50);
        table.getColumnModel().getColumn(2).setMinWidth(90);
        table.getColumnModel().getColumn(2).setMaxWidth(110);

        DefaultTableCellRenderer topRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setVerticalAlignment(SwingConstants.CENTER);
                label.setBorder(new EmptyBorder(0, 10, 0, 10));
                return label;
            }
        };
        table.getColumnModel().getColumn(0).setCellRenderer(topRenderer);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        int[] columnsToHide = {9, 8, 7, 6, 5, 4, 3};
        for (int colIndex : columnsToHide) {
            if (table.getColumnModel().getColumnCount() > colIndex) {
                table.getColumnModel().removeColumn(table.getColumnModel().getColumn(colIndex));
            }
        }

        JScrollPane scrollTable = new JScrollPane(table);
        scrollTable.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, COLOR_BORDER));
        scrollTable.getViewport().setBackground(Color.WHITE);
        table.setFillsViewportHeight(true);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        String tenMonHTML = (String) cartTableModel.getValueAt(row, 0);
                        int unitPrice = (int) cartTableModel.getValueAt(row, 3);
                        String maDM = (String) cartTableModel.getValueAt(row, 5);
                        Window owner = SwingUtilities.getWindowAncestor(BanHangPanel.this);
                        ChonMonDialog dialog = new ChonMonDialog(owner, tenMonHTML.replaceAll("<[^>]*>", "").split(" \\(")[0].trim(), unitPrice, maDM);
                        dialog.setVisible(true);

                        if (dialog.isConfirmed()) {
                            cartTableModel.setValueAt(dialog.getFinalTenMonDetail(), row, 0);
                            cartTableModel.setValueAt(dialog.getSoLuong(), row, 1);
                            cartTableModel.setValueAt(formatter.format(dialog.getSoLuong() * dialog.getDonGia()), row, 2);
                            cartTableModel.setValueAt(dialog.getDonGia(), row, 3);
                            cartTableModel.setValueAt(dialog.getSelectedToppingNames(), row, 4);
                            cartTableModel.setValueAt(dialog.getSelectedMaSize(), row, 6);
                            cartTableModel.setValueAt(dialog.getSelectedMucDa(), row, 7);
                            cartTableModel.setValueAt(dialog.getSelectedMucDuong(), row, 8);
                            recalculateTotal();
                        }
                    }
                }
            }
        });

        scrollTable.getViewport().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                table.clearSelection();
            }
        });

        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        controlsPanel.setBackground(Color.WHITE);

        JButton btnGiam = new JButton(" - ");
        JButton btnTang = new JButton(" + ");
        JButton btnXoa = new JButton("Xóa món");

        styleControlButton(btnGiam);
        styleControlButton(btnTang);
        styleControlButton(btnXoa);
        btnXoa.setForeground(new Color(220, 53, 69));

        controlsPanel.add(btnXoa);
        controlsPanel.add(btnGiam);
        controlsPanel.add(btnTang);

        btnXoa.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                cartTableModel.removeRow(row);
                recalculateTotal();
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một món trong bảng để xóa!");
            }
        });

        btnTang.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int qty = Integer.parseInt(cartTableModel.getValueAt(row, 1).toString());
                int price = (int) cartTableModel.getValueAt(row, 3);
                qty++;
                cartTableModel.setValueAt(qty, row, 1);
                cartTableModel.setValueAt(formatter.format(qty * price), row, 2);
                recalculateTotal();
            }
        });

        btnGiam.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int qty = Integer.parseInt(cartTableModel.getValueAt(row, 1).toString());
                int price = (int) cartTableModel.getValueAt(row, 3);
                qty--;
                if (qty <= 0) {
                    cartTableModel.removeRow(row);
                } else {
                    cartTableModel.setValueAt(qty, row, 1);
                    cartTableModel.setValueAt(formatter.format(qty * price), row, 2);
                }
                recalculateTotal();
            }
        });

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(new EmptyBorder(10, 15, 15, 15));

        JPanel calcPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        calcPanel.setOpaque(false);
        lblTamTinh = new JLabel("0đ");
        lblGiamGia = new JLabel("0đ");
        lblTongTien = new JLabel("0đ");
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTongTien.setForeground(COLOR_PRIMARY_DARK);

        calcPanel.add(createRowPanel("Tạm tính:", lblTamTinh, false));
        calcPanel.add(createRowPanel("Giảm giá:", lblGiamGia, false));
        calcPanel.add(createRowPanel("TỔNG THANH TOÁN:", lblTongTien, true));

        JPanel loyaltyPanel = new JPanel(new BorderLayout(5, 5));
        loyaltyPanel.setOpaque(false);
        loyaltyPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        chkTichDiem = new JCheckBox("Khách hàng thân thiết (Tích điểm 10%)");
        chkTichDiem.setFont(new Font("Segoe UI", Font.BOLD, 13));
        chkTichDiem.setForeground(COLOR_PRIMARY_DARK);
        chkTichDiem.setOpaque(false);

        customerPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        customerPanel.setOpaque(false);
        customerPanel.setVisible(false);

        txtSdtKH = new JTextField();
        txtTenKH = new JTextField();
        txtTenKH.setEditable(false);

        customerPanel.add(new JLabel("Số điện thoại:"));
        customerPanel.add(txtSdtKH);
        customerPanel.add(new JLabel("Tên khách hàng:"));
        customerPanel.add(txtTenKH);
        customerPanel.add(new JLabel("Ưu đãi:"));
        JPanel uuDaiBtnGroup = new JPanel(new GridLayout(1, 2, 5, 0));
        uuDaiBtnGroup.setOpaque(false);

        JButton btnDoiDiem = new JButton("Đổi điểm");
        JButton btnVoucher = new JButton("Voucher");
        styleControlButton(btnDoiDiem);
        styleControlButton(btnVoucher);

        uuDaiBtnGroup.add(btnDoiDiem);
        uuDaiBtnGroup.add(btnVoucher);
        customerPanel.add(uuDaiBtnGroup);

        btnDoiDiem.addActionListener(e -> showDiemDialog());

        btnVoucher.addActionListener(e -> {
            int diem = (khachHangHienTai != null) ? khachHangHienTai.getDiemTL() : 0;
            VoucherDAO vDAO = new VoucherDAO();
            List<Voucher> dsVoucher = vDAO.layVoucherKhaDung(diem);

            if (dsVoucher.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không có voucher nào khả dụng!");
                return;
            }

            Voucher selected = (Voucher) JOptionPane.showInputDialog(
                    this, "Chọn Voucher", "Khuyến mãi",
                    JOptionPane.QUESTION_MESSAGE, null, dsVoucher.toArray(), dsVoucher.get(0)
            );

            if (selected != null) {
                this.voucherApDung = selected;
                recalculateTotal();
            }
        });

        chkTichDiem.addActionListener(e -> {
            customerPanel.setVisible(chkTichDiem.isSelected());
            if (!chkTichDiem.isSelected()) {
                khachHangHienTai = null;
                voucherApDung = null;
                txtSdtKH.setText("");
                txtTenKH.setText("");
                recalculateTotal();
            }
            revalidate();
            repaint();
        });

        txtSdtKH.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { checkCustomer(); }
            public void removeUpdate(DocumentEvent e) { checkCustomer(); }
            public void changedUpdate(DocumentEvent e) { checkCustomer(); }

            private void checkCustomer() {
                String sdt = txtSdtKH.getText().trim();
                if (sdt.length() >= 10) {
                    KhachHang kh = khDAO.timTheoSDT(sdt);
                    if (kh != null) {
                        khachHangHienTai = kh;
                        txtTenKH.setText(kh.getTenKH());
                        txtTenKH.setEditable(false);
                    } else {
                        khachHangHienTai = null;
                        txtTenKH.setText("");
                        txtTenKH.setEditable(true);
                    }
                }
            }
        });

        loyaltyPanel.add(chkTichDiem, BorderLayout.NORTH);
        loyaltyPanel.add(customerPanel, BorderLayout.CENTER);

        JPanel infoWrapper = new JPanel(new BorderLayout());
        infoWrapper.setOpaque(false);
        infoWrapper.add(loyaltyPanel, BorderLayout.NORTH);
        infoWrapper.add(calcPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        JButton btnHuy = new JButton("HỦY ĐƠN");
        btnHuy.setPreferredSize(new Dimension(0, 55));
        btnHuy.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnHuy.setBackground(new Color(242, 236, 231));
        btnHuy.setForeground(COLOR_PRIMARY_DARK);
        btnHuy.setFocusPainted(false);
        btnHuy.addActionListener(e -> resetForm());

        JButton btnThanhToan = new JButton("THANH TOÁN");
        btnThanhToan.setBackground(COLOR_PRIMARY_DARK);
        btnThanhToan.setForeground(Color.WHITE);
        btnThanhToan.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnThanhToan.setFocusPainted(false);
        btnThanhToan.addActionListener(e -> {
            if (cartTableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Giỏ hàng trống!");
                return;
            }
            try {
                double tongTienCuoi = Double.parseDouble(lblTongTien.getText().replaceAll("[^\\d]", ""));
                KhachHang khFinal = null;

                if (chkTichDiem.isSelected()) {
                    String sdt = txtSdtKH.getText().trim();
                    if (sdt.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Vui lòng nhập SĐT!");
                        return;
                    }

                    khFinal = khDAO.timTheoSDT(sdt);
                    if (khFinal != null) {
                        if (voucherApDung != null && voucherApDung.getDiemCanDoi() > 0) {
                            khDAO.truDiemTichLuy(khFinal.getMaKH(), voucherApDung.getDiemCanDoi());
                            khFinal.setDiemTL(khFinal.getDiemTL() - voucherApDung.getDiemCanDoi());
                        }
                        int diemCong = (int) (tongTienCuoi * 0.1);
                        khDAO.congDiemTichLuy(khFinal.getMaKH(), diemCong);
                        khFinal.setDiemTL(khFinal.getDiemTL() + diemCong);
                    } else {
                        khFinal = new KhachHang();
                        khFinal.setMaKH("KH" + System.currentTimeMillis());
                        khFinal.setTenKH(txtTenKH.getText().trim());
                        khFinal.setSdt(sdt);
                        khFinal.setDiemTL((int)(tongTienCuoi * 0.1));
                        khFinal.setNgayTao(LocalDateTime.now());
                        khDAO.themKhachHang(khFinal);
                    }
                } else {
                    khFinal = new KhachHang();
                    khFinal.setMaKH("KH00");
                }

                DonHang dh = new DonHang();
                String maDH = "DH" + System.currentTimeMillis();
                dh.setMaDH(maDH);
                dh.setNgayTao(LocalDateTime.now());
                dh.setTrangThai(true);
                dh.setTongTien(tongTienCuoi);
                dh.setStt(1);
                NhanVien nv = new NhanVien();
                nv.setMaNV("NV01");
                dh.setMaNV(nv);
                dh.setMaKH(khFinal);

                DonHangDAO dhDAO = new DonHangDAO();
                ChiTietHoaDonDAO cthdDAO = new ChiTietHoaDonDAO();
                HoaDonDAO hdDAO = new HoaDonDAO();
                ChiTietHoaDonToppingDAO toppingDAO = new ChiTietHoaDonToppingDAO();

                if (dhDAO.themDonHang(dh)) {
                    boolean allDetailsSaved = true;
                    for (int i = 0; i < cartTableModel.getRowCount(); i++) {
                        ChiTietHoaDon ct = new ChiTietHoaDon();
                        String maCTHD = "CT" + java.util.UUID.randomUUID().toString().substring(0, 8);
                        int soLuong = Integer.parseInt(cartTableModel.getValueAt(i, 1).toString());
                        double thanhTien = Double.parseDouble(cartTableModel.getValueAt(i, 2).toString().replaceAll("[^\\d]", ""));
                        String fullDisplayName = cartTableModel.getValueAt(i, 0).toString();
                        String ghiChuThuan = "";
                        if (fullDisplayName.contains("* ")) {
                            ghiChuThuan = fullDisplayName.substring(fullDisplayName.lastIndexOf("* ") + 2).replaceAll("</span></html>", "").trim();
                        }
                        String displaySize = cartTableModel.getValueAt(i, 6).toString();
                        String maSizeReal = displaySize;
                        if (displaySize.equalsIgnoreCase("M")) maSizeReal = "S01";
                        else if (displaySize.equalsIgnoreCase("L")) maSizeReal = "S07";
                        else if (displaySize.equalsIgnoreCase("Tiêu chuẩn")) maSizeReal = "S02";

                        Object daObj = cartTableModel.getValueAt(i, 7);
                        Object duongObj = cartTableModel.getValueAt(i, 8);
                        MucDa daChon = (daObj instanceof MucDa) ? (MucDa) daObj : MucDa.DA_100;
                        MucDuong duongChon = (duongObj instanceof MucDuong) ? (MucDuong) duongObj : MucDuong.DUONG_100;

                        ct.setMaCTHD(maCTHD);
                        ct.setDonHang(dh);
                        ct.setMaSize(maSizeReal);
                        ct.setSoLuong(soLuong);
                        ct.setThanhTien(thanhTien);
                        ct.setLuongDa(daChon);
                        ct.setLuongDuong(duongChon);
                        ct.setGhiChu(ghiChuThuan);

                        if (cthdDAO.themChiTiet(ct)) {
                            Object toppingObj = cartTableModel.getValueAt(i, 4);
                            if (toppingObj instanceof List) {
                                List<String> dsToppingChon = (List<String>) toppingObj;
                                for (String tenTopping : dsToppingChon) {
                                    toppingDAO.themToppingTheoTen(maCTHD, tenTopping);
                                }
                            }
                        } else {
                            allDetailsSaved = false;
                            break;
                        }
                    }

                    if (allDetailsSaved) {
                        HoaDon hd = new HoaDon();
                        hd.setMaHD("HD" + System.currentTimeMillis());
                        hd.setMaDH(dh);
                        hd.setMaVoucher(voucherApDung);
                        hd.setNgayThanhToan(LocalDateTime.now());
                        hd.setPhuongThucTT(PhuongThucTT.TIEN_MAT);
                        hd.setTongTienCuoi(tongTienCuoi);
                        if (hdDAO.taoHoaDon(hd)) {
                            JOptionPane.showMessageDialog(this, "Thanh toán thành công!");
                            resetForm();
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        btnPanel.add(btnHuy);
        btnPanel.add(btnThanhToan);
        bottomPanel.add(infoWrapper, BorderLayout.CENTER);
        bottomPanel.add(btnPanel, BorderLayout.SOUTH);
        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.add(scrollTable, BorderLayout.CENTER);
        centerWrapper.add(controlsPanel, BorderLayout.SOUTH);
        rightPanel.add(cartHeader, BorderLayout.NORTH);
        rightPanel.add(centerWrapper, BorderLayout.CENTER);
        rightPanel.add(bottomPanel, BorderLayout.SOUTH);

        return rightPanel;
    }

    private void styleControlButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBackground(COLOR_BG);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private JPanel createProductCard(String name, int price, String maDM, String maSize, String linkAnh, String maSP) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Label hiển thị ảnh
        JLabel lblImg = new JLabel("Đang tải...", SwingConstants.CENTER);
        lblImg.setPreferredSize(new Dimension(0, 150));
        lblImg.setBackground(new Color(250, 242, 235));
        lblImg.setOpaque(true);

        if (linkAnh != null && !linkAnh.isEmpty()) {
            // Kiểm tra trong bộ nhớ đệm (Cache) trước
            if (imageCache.containsKey(linkAnh)) {
                lblImg.setText("");
                lblImg.setIcon(imageCache.get(linkAnh));
            } else {
                // Tải ảnh trong luồng phụ (Thread) để không treo giao diện
                new Thread(() -> {
                    try {
                        java.net.URL url = new java.net.URL(linkAnh);
                        // ImageIO lúc này đã có TwelveMonkeys hỗ trợ đọc WebP
                        java.awt.Image rawImg = javax.imageio.ImageIO.read(url);

                        if (rawImg != null) {
                            // Căn chỉnh kích thước ảnh phù hợp với khung hình
                            java.awt.Image scaledImg = rawImg.getScaledInstance(180, 150, java.awt.Image.SCALE_SMOOTH);
                            ImageIcon icon = new ImageIcon(scaledImg);

                            // Lưu vào bộ nhớ đệm để dùng lại lần sau
                            imageCache.put(linkAnh, icon);

                            // Cập nhật lại giao diện trên luồng chính của Swing
                            SwingUtilities.invokeLater(() -> {
                                lblImg.setText("");
                                lblImg.setIcon(icon);
                            });
                        }
                    } catch (Exception e) {
                        SwingUtilities.invokeLater(() -> lblImg.setText("Lỗi ảnh"));
                    }
                }).start();
            }
        } else {
            lblImg.setText("Không có ảnh");
        }

        // Phần thông tin tên và giá món
        JPanel info = new JPanel(new BorderLayout());
        info.setBackground(Color.WHITE);
        info.setBorder(new javax.swing.border.EmptyBorder(10, 10, 10, 10));

        JLabel lblName = new JLabel(name);
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblName.setForeground(COLOR_TEXT_MAIN);

        JLabel lblPrice = new JLabel(formatter.format(price));
        lblPrice.setForeground(COLOR_PRIMARY_LIGHT);

        info.add(lblName, BorderLayout.WEST);
        info.add(lblPrice, BorderLayout.EAST);

        card.add(lblImg, BorderLayout.CENTER);
        card.add(info, BorderLayout.SOUTH);

        // Xử lý sự kiện khi click vào món ăn
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Window owner = SwingUtilities.getWindowAncestor(BanHangPanel.this);
                ChonMonDialog dialog = new ChonMonDialog(owner, name, price, maDM);
                dialog.setVisible(true);

                if (dialog.isConfirmed()) {
                    addToCart(
                            dialog.getFinalTenMonDetail(),
                            dialog.getSoLuong(),
                            (int) dialog.getDonGia(),
                            dialog.getSelectedToppingNames(),
                            maDM,
                            dialog.getSelectedMaSize(),
                            dialog.getSelectedMucDa(),
                            dialog.getSelectedMucDuong()
                    );
                }
            }
        });

        return card;
    }

    private JButton createTabButton(String title, String maDM, boolean isActive) {
        JButton btn = new JButton(title);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(120, 45));
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (isActive) {
            styleActiveTab(btn);
        } else {
            styleInactiveTab(btn);
        }

        tabButtons.add(btn);

        btn.addActionListener(e -> {
            for (JButton t : tabButtons) {
                styleInactiveTab(t);
            }
            styleActiveTab(btn);
            loadProducts(maDM);
        });

        return btn;
    }

    private void styleActiveTab(JButton btn) {
        btn.setForeground(COLOR_PRIMARY_DARK);
        btn.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, COLOR_PRIMARY_DARK));
        btn.setBorderPainted(true);
    }

    private void styleInactiveTab(JButton btn) {
        btn.setForeground(new Color(120, 100, 90));
        btn.setBorderPainted(false);
    }

    private JPanel createModernHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 65));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER));
        return header;
    }

    private JPanel createRowPanel(String text, JLabel rightLabel, boolean isBold) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        JLabel lblLeft = new JLabel(text);

        if (isBold) {
            lblLeft.setFont(new Font("Segoe UI", Font.BOLD, 16));
            lblLeft.setForeground(COLOR_TEXT_MAIN);
        } else {
            lblLeft.setForeground(new Color(120, 100, 90));
        }

        panel.add(lblLeft, BorderLayout.WEST);
        panel.add(rightLabel, BorderLayout.EAST);
        return panel;
    }

    private void showDiemDialog() {
        if (khachHangHienTai == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập SĐT khách hàng trước để kiểm tra điểm!");
            return;
        }

        int diemHienCo = khachHangHienTai.getDiemTL();
        String input = JOptionPane.showInputDialog(this,
                "Điểm hiện có: " + diemHienCo + "\n(Quy đổi: 100 điểm = 10.000đ)\nNhập số điểm muốn đổi:",
                "Đổi điểm tích lũy", JOptionPane.QUESTION_MESSAGE);

        if (input != null && !input.isEmpty()) {
            try {
                int diemNhap = Integer.parseInt(input);
                if (diemNhap <= 0) return;
                if (diemNhap > diemHienCo) {
                    JOptionPane.showMessageDialog(this, "Khách hàng không đủ điểm!");
                    return;
                }
                if (diemNhap > 5000) {
                    JOptionPane.showMessageDialog(this, "Chỉ được đổi tối đa 5000 điểm (500.000đ) mỗi lần!");
                    diemNhap = 5000;
                }

                voucherApDung = new Voucher();
                voucherApDung.setTenCT("Đổi " + diemNhap + " điểm");
                voucherApDung.setGiaTriGiam(diemNhap * 100.0);
                voucherApDung.setDiemCanDoi(diemNhap);
                voucherApDung.setLoaiGiamGia(LoaiGiamGia.DIEM_TICH_LUY);

                recalculateTotal();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập số điểm hợp lệ!");
            }
        }
    }
}