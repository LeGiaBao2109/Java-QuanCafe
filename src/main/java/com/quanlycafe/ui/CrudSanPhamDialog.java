package com.quanlycafe.ui;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class CrudSanPhamDialog extends JDialog {

    private final Color COLOR_BG = new Color(253, 248, 245);
    private final Color COLOR_SURFACE = Color.WHITE;
    private final Color COLOR_PRIMARY = new Color(92, 64, 51);
    private final Color COLOR_BORDER = new Color(230, 220, 210);

    private JComboBox<String> cbSize, cbNhomThucDon, cbDonViTinh;
    private JTextField txtMaMon, txtTenMon, txtGia;
    private JButton btnAction, btnCancel;

    private String mode;
    private boolean isSuccess = false;

    public CrudSanPhamDialog(Window owner, String mode, Object[] rowData) {
        super(owner, "", Dialog.ModalityType.APPLICATION_MODAL);
        this.mode = mode;
        
        setupUI();
        fillDataAndSetMode(rowData);
        setupActions();

        pack();
        setLocationRelativeTo(owner);
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_BG);

        JPanel pnlHeader = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlHeader.setBackground(COLOR_BG);
        pnlHeader.setBorder(new EmptyBorder(15, 0, 10, 0));
        
        String title = mode.equals("ADD") ? "THÊM SẢN PHẨM MỚI" : (mode.equals("EDIT") ? "CẬP NHẬT SẢN PHẨM" : "XÓA SẢN PHẨM");
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(COLOR_PRIMARY);
        pnlHeader.add(lblTitle);

        JPanel pnlForm = new JPanel(new GridLayout(6, 2, 10, 15));
        pnlForm.setBackground(COLOR_SURFACE);
        pnlForm.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDER, 1),
                new EmptyBorder(20, 30, 20, 30)
        ));

        txtMaMon = createTextField();
        txtTenMon = createTextField();
        cbSize = createComboBox(new String[]{"M", "L", "S", "Khác", "Không có"});
        cbNhomThucDon = createComboBox(new String[]{"Cà phê", "Trà sữa", "Trà", "Bánh", "Khác"});
        cbDonViTinh = createComboBox(new String[]{"Ly", "Phần", "Cái"});
        txtGia = createTextField();

        pnlForm.add(createLabel("Mã món:")); pnlForm.add(txtMaMon);
        pnlForm.add(createLabel("Tên món:")); pnlForm.add(txtTenMon);
        pnlForm.add(createLabel("Size:")); pnlForm.add(cbSize);
        pnlForm.add(createLabel("Nhóm thực đơn:")); pnlForm.add(cbNhomThucDon);
        pnlForm.add(createLabel("Đơn vị tính:")); pnlForm.add(cbDonViTinh);
        pnlForm.add(createLabel("Giá bán (VND):")); pnlForm.add(txtGia);

        JPanel pnlFormWrapper = new JPanel(new BorderLayout());
        pnlFormWrapper.setOpaque(false);
        pnlFormWrapper.setBorder(new EmptyBorder(0, 20, 0, 20));
        pnlFormWrapper.add(pnlForm, BorderLayout.CENTER);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        pnlButtons.setBackground(COLOR_BG);

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

    private void fillDataAndSetMode(Object[] rowData) {
        if (mode.equals("ADD")) {
            btnAction.setText("Thêm Mới");
        } else {
            if (rowData != null) {
                txtMaMon.setText(rowData[0].toString());
                txtTenMon.setText(rowData[1].toString());
                cbSize.setSelectedItem(rowData[2].toString());
                cbNhomThucDon.setSelectedItem(rowData[3].toString());
                cbDonViTinh.setSelectedItem(rowData[4].toString());
                txtGia.setText(rowData[5].toString().replace(".000 VND", "000").replace(" VND", ""));
            }
            
            if (mode.equals("EDIT")) {
                btnAction.setText("Cập Nhật");
                txtMaMon.setEditable(false); 
            } else if (mode.equals("DELETE")) {
                btnAction.setText("Xác Nhận Xóa");
                txtMaMon.setEditable(false);
                txtTenMon.setEditable(false);
                cbSize.setEnabled(false);
                cbNhomThucDon.setEnabled(false);
                cbDonViTinh.setEnabled(false);
                txtGia.setEditable(false);
            }
        }
    }

    private void setupActions() {
        btnCancel.addActionListener(e -> dispose());

        btnAction.addActionListener(e -> {
            if (mode.equals("DELETE")) {
                isSuccess = true;
                dispose();
            } else {
                if (txtMaMon.getText().trim().isEmpty() || txtTenMon.getText().trim().isEmpty() || txtGia.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                isSuccess = true;
                dispose();
            }
        });
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(new Color(100, 80, 70));
        return lbl;
    }

    private JTextField createTextField() {
        JTextField txt = new JTextField();
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBorder(new CompoundBorder(
                new LineBorder(COLOR_BORDER, 1),
                new EmptyBorder(5, 10, 5, 10)
        ));
        return txt;
    }

    private JComboBox<String> createComboBox(String[] items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cb.setBackground(Color.WHITE);
        return cb;
    }

    private void styleButton(JButton btn, boolean isPrimary) {
        btn.setPreferredSize(new Dimension(120, 38));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
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
}