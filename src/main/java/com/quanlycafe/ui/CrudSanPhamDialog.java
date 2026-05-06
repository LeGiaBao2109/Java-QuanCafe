package com.quanlycafe.ui;

import com.quanlycafe.dao.SanPhamDAO;
import com.quanlycafe.entity.DanhMuc;
import com.quanlycafe.entity.SanPham;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.time.LocalDateTime;

public class CrudSanPhamDialog extends JDialog {

    private final Color COLOR_BG = new Color(253, 248, 245);
    private final Color COLOR_SURFACE = Color.WHITE;
    private final Color COLOR_PRIMARY = new Color(92, 64, 51);
    private final Color COLOR_BORDER = new Color(230, 220, 210);

    private JComboBox<String> cbSize, cbNhomThucDon, cbDonViTinh;
    private JTextField txtMaMon, txtTenMon, txtGia, txtTonKho, txtAnhSP;
    private JButton btnAction, btnCancel;

    private String mode;
    private boolean isSuccess = false;
    private String oldSizeDB = ""; 

    public CrudSanPhamDialog(Window owner, String mode, Object[] rowData) {
        super(owner, "", Dialog.ModalityType.APPLICATION_MODAL);
        this.mode = mode;

        setupUI();
        setupDynamicComboBoxes();
        fillDataAndSetMode(rowData);
        setupActions();

        setSize(500, 650);
        setLocationRelativeTo(owner);
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_BG);

        JPanel pnlHeader = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlHeader.setBackground(COLOR_BG);
        pnlHeader.setBorder(new EmptyBorder(25, 0, 15, 0));

        String title = mode.equals("ADD") ? "THÊM SẢN PHẨM MỚI" : (mode.equals("EDIT") ? "CẬP NHẬT SẢN PHẨM" : "XÓA SẢN PHẨM");
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(COLOR_PRIMARY);
        pnlHeader.add(lblTitle);

        JPanel pnlForm = new JPanel(new GridLayout(8, 2, 15, 15));
        pnlForm.setBackground(COLOR_SURFACE);
        pnlForm.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDER, 1),
                new EmptyBorder(20, 35, 20, 35)
        ));

        txtMaMon = createTextField();
        txtTenMon = createTextField();
        cbNhomThucDon = createComboBox(new String[]{"Cà Phê", "Trà Sữa", "Trà Trái Cây", "Bánh", "Khác"});
        cbSize = createComboBox(new String[]{});
        cbDonViTinh = createComboBox(new String[]{});
        txtGia = createTextField();
        
        txtTonKho = createTextField();
        txtTonKho.setText("100"); 
        
        txtAnhSP = createTextField();
        txtAnhSP.setText("default.png"); 

        pnlForm.add(createLabel("Mã món:")); pnlForm.add(txtMaMon);
        pnlForm.add(createLabel("Tên món:")); pnlForm.add(txtTenMon);
        pnlForm.add(createLabel("Nhóm thực đơn:")); pnlForm.add(cbNhomThucDon);
        pnlForm.add(createLabel("Size:")); pnlForm.add(cbSize);
        pnlForm.add(createLabel("Đơn vị tính:")); pnlForm.add(cbDonViTinh);
        pnlForm.add(createLabel("Giá bán (VND):")); pnlForm.add(txtGia);
        pnlForm.add(createLabel("Tồn kho ban đầu:")); pnlForm.add(txtTonKho);
        pnlForm.add(createLabel("Tên file ảnh:")); pnlForm.add(txtAnhSP);

        JPanel pnlFormWrapper = new JPanel(new BorderLayout());
        pnlFormWrapper.setOpaque(false);
        pnlFormWrapper.setBorder(new EmptyBorder(0, 25, 0, 25));
        pnlFormWrapper.add(pnlForm, BorderLayout.CENTER);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 20));
        pnlButtons.setBackground(COLOR_BG);
        pnlButtons.setBorder(new EmptyBorder(0, 0, 10, 10));

        btnCancel = new JButton("Hủy bỏ");
        styleButton(btnCancel, false);

        btnAction = new JButton();
        styleButton(btnAction, true);

        if(mode.equals("DELETE")) {
            btnAction.setBackground(new Color(220, 53, 69));
        }

        pnlButtons.add(btnCancel);
        pnlButtons.add(btnAction);

        add(pnlHeader, BorderLayout.NORTH);
        add(pnlFormWrapper, BorderLayout.CENTER);
        add(pnlButtons, BorderLayout.SOUTH);
    }

    private void setupDynamicComboBoxes() {
        cbNhomThucDon.addActionListener(e -> {
            String selectedNhom = (String) cbNhomThucDon.getSelectedItem();
            if (selectedNhom == null) return;

            cbSize.removeAllItems();
            cbDonViTinh.removeAllItems();

            switch (selectedNhom) {
                case "Cà Phê":
                case "Trà Sữa":
                case "Trà Trái Cây":
                    cbSize.addItem("M");
                    cbSize.addItem("L");
                    cbSize.addItem("S");
                    cbDonViTinh.addItem("Ly");
                    break;
                case "Bánh":
                    cbSize.addItem("Tiêu chuẩn");
                    cbSize.addItem("Không có");
                    cbDonViTinh.addItem("Phần");
                    cbDonViTinh.addItem("Cái");
                    break;
                case "Khác":
                    cbSize.addItem("Chai 500ml");
                    cbSize.addItem("Tiêu chuẩn");
                    cbSize.addItem("Không có");
                    cbDonViTinh.addItem("Chai");
                    cbDonViTinh.addItem("Khác");
                    break;
            }
        });
        
        cbNhomThucDon.setSelectedIndex(0);
    }

    private void fillDataAndSetMode(Object[] rowData) {
        SanPhamDAO dao = new SanPhamDAO(); // Khởi tạo DAO

        if (mode.equals("ADD")) {
            btnAction.setText("Thêm Mới");
            // Tự sinh mã và khóa TextBox lại
            txtMaMon.setText(dao.taoMaSPTuSinh());
            txtMaMon.setEditable(false);
            txtMaMon.setBackground(new Color(240, 240, 240)); // Đổi màu nền cho biết là field khóa
        } else {
            if (rowData != null) {
                String maSP = rowData[0].toString();
                txtMaMon.setText(maSP);
                txtTenMon.setText(rowData[1].toString());

                cbNhomThucDon.setSelectedItem(rowData[3].toString());

                String oldSizeUI = rowData[2].toString();
                oldSizeDB = oldSizeUI;
                // Xử lý chuẩn hóa tên Size khi hiển thị
                if (oldSizeUI != null && (oldSizeUI.equals("M") || oldSizeUI.equals("L") || oldSizeUI.equals("S"))) {
                    oldSizeDB = "Size " + oldSizeUI;
                }

                cbSize.setSelectedItem(oldSizeUI.replace("Size ", ""));
                cbDonViTinh.setSelectedItem(rowData[4].toString());
                txtGia.setText(rowData[5].toString().replace(".000 VND", "000").replace(" VND", "").replace(",", ""));

                SanPham sp = dao.timTheoMa(maSP);
                if (sp != null) {
                    txtTonKho.setText(String.valueOf(sp.getTonKho()));
                    txtAnhSP.setText(sp.getAnhSP() == null ? "default.png" : sp.getAnhSP());
                }
            }

            if (mode.equals("EDIT")) {
                btnAction.setText("Cập Nhật");
                txtMaMon.setEditable(false);
                txtMaMon.setBackground(new Color(240, 240, 240));
            } else if (mode.equals("DELETE")) {
                btnAction.setText("Xác Nhận Xóa");
                setFieldsEditable(false);
            }
        }
    }

    private void setupActions() {
        btnCancel.addActionListener(e -> dispose());

        btnAction.addActionListener(e -> {
            
            if (mode.equals("DELETE")) {
                int confirm = JOptionPane.showConfirmDialog(this, 
                    "Bạn có chắc chắn muốn xóa sản phẩm này khỏi hệ thống không?\n(Lưu ý: Thao tác này sẽ ẩn toàn bộ các kích cỡ của sản phẩm)", 
                    "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    SanPhamDAO dao = new SanPhamDAO();
                    if (dao.xoaSanPham(txtMaMon.getText().trim())) {
                        JOptionPane.showMessageDialog(this, "Xóa sản phẩm thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                        isSuccess = true;
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Xóa sản phẩm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
                return;
            }

            String maMon = txtMaMon.getText().trim();
            String tenMon = txtTenMon.getText().trim();
            String gia = txtGia.getText().trim();
            String tonKhoStr = txtTonKho.getText().trim();
            String anhSP = txtAnhSP.getText().trim();

            if (maMon.isEmpty() || tenMon.isEmpty() || gia.isEmpty() || tonKhoStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!maMon.matches("^SP\\d+$")) {
                JOptionPane.showMessageDialog(this, "Mã món phải bắt đầu bằng 'SP' theo sau là các chữ số (VD: SP01)!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double giaBan = 0;
            int tonKho = 0;
            try {
                giaBan = Double.parseDouble(gia);
                tonKho = Integer.parseInt(tonKhoStr);
                
                if (giaBan <= 0) {
                    JOptionPane.showMessageDialog(this, "Giá bán phải lớn hơn 0!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (tonKho < 0) {
                    JOptionPane.showMessageDialog(this, "Tồn kho không được nhỏ hơn 0!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Giá bán và tồn kho phải là số hợp lệ!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }

            SanPhamDAO dao = new SanPhamDAO();

            String tenNhom = (String) cbNhomThucDon.getSelectedItem();
            String maDM = "DM05"; 
            if (tenNhom.equals("Cà Phê")) maDM = "DM01";
            else if (tenNhom.equals("Trà Sữa")) maDM = "DM02";
            else if (tenNhom.equals("Trà Trái Cây")) maDM = "DM03";
            else if (tenNhom.equals("Bánh")) maDM = "DM04";

            DanhMuc dm = new DanhMuc();
            dm.setMaDM(maDM);

            SanPham sp = new SanPham();
            sp.setMaSP(maMon);
            sp.setTenSP(tenMon);
            sp.setMaDM(dm);
            sp.setDonViTinh((String) cbDonViTinh.getSelectedItem());
            sp.setTonKho(tonKho); 
            sp.setTrangThai(true);
            sp.setAnhSP(anhSP);

            if (mode.equals("ADD")) {
                if (dao.timTheoMa(maMon) != null) {
                    JOptionPane.showMessageDialog(this, "Mã món đã tồn tại trong hệ thống!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                sp.setNgayTao(LocalDateTime.now());
                sp.setNgayCapNhat(LocalDateTime.now());

                if (dao.themSanPham(sp)) {
                    String sizeUI = (String) cbSize.getSelectedItem();
                    String newSizeDB = sizeUI;
                    if (sizeUI != null && (sizeUI.equals("M") || sizeUI.equals("L") || sizeUI.equals("S"))) {
                        newSizeDB = "Size " + sizeUI;
                    }
                    
                    dao.themKichCo(maMon, newSizeDB, giaBan);

                    JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    isSuccess = true;
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm sản phẩm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
                
            } else if (mode.equals("EDIT")) {
                sp.setNgayCapNhat(LocalDateTime.now());
                
                if (dao.capNhatSanPham(sp)) {
                    String sizeUI = (String) cbSize.getSelectedItem();
                    String newSizeDB = sizeUI;
                    if (sizeUI != null && (sizeUI.equals("M") || sizeUI.equals("L") || sizeUI.equals("S"))) {
                        newSizeDB = "Size " + sizeUI;
                    }
                    
                    dao.capNhatKichCo(maMon, oldSizeDB, newSizeDB, giaBan);
                    
                    JOptionPane.showMessageDialog(this, "Cập nhật sản phẩm thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    isSuccess = true;
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(new Color(100, 80, 70));
        return lbl;
    }

    private JTextField createTextField() {
        JTextField txt = new JTextField();
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txt.setBorder(new CompoundBorder(
                new LineBorder(COLOR_BORDER, 1),
                new EmptyBorder(5, 10, 5, 10)
        ));
        return txt;
    }

    private JComboBox<String> createComboBox(String[] items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        cb.setBackground(Color.WHITE);
        return cb;
    }

    private void styleButton(JButton btn, boolean isPrimary) {
        btn.setPreferredSize(new Dimension(130, 42));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (isPrimary) {
            btn.setBackground(COLOR_PRIMARY);
            btn.setForeground(Color.WHITE);
            btn.setBorderPainted(false);
            btn.setOpaque(true);
        } else {
            btn.setBackground(COLOR_BG);
            btn.setForeground(COLOR_PRIMARY);
            btn.setBorder(new LineBorder(COLOR_BORDER, 1));
            btn.setContentAreaFilled(false);
            btn.setOpaque(true);
        }
    }

    private void setFieldsEditable(boolean editable) {
        txtMaMon.setEditable(editable);
        txtTenMon.setEditable(editable);
        cbSize.setEnabled(editable);
        cbNhomThucDon.setEnabled(editable);
        cbDonViTinh.setEnabled(editable);
        txtGia.setEditable(editable);
        txtTonKho.setEditable(editable);
        txtAnhSP.setEditable(editable);
    }
}