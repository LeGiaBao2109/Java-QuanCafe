package com.quanlycafe.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import java.time.LocalDateTime;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.quanlycafe.dao.NhanVienDAO;
import com.quanlycafe.dao.SanPhamDAO;
import com.quanlycafe.dao.TaiKhoanDAO;
import com.quanlycafe.entity.DanhMuc;
import com.quanlycafe.entity.NhanVien;
import com.quanlycafe.entity.RoleNhanVien;
import com.quanlycafe.entity.SanPham;
import com.quanlycafe.entity.TaiKhoan;

public class CrudNhanVienDialog extends JDialog{
	private final Color COLOR_BG = new Color(253, 248, 245);
    private final Color COLOR_SURFACE = Color.WHITE;
    private final Color COLOR_PRIMARY = new Color(92, 64, 51);
    private final Color COLOR_BORDER = new Color(230, 220, 210);

    private JComboBox<String> cbRole;
    private JTextField txtMaNV, txtTenDangNhap, txtTenNV, txtSDT, txtMatKhau, txtMaTK;
    private JButton btnAction, btnCancel;

    private String mode;
    private boolean isSuccess = false;
    private String oldSizeDB = ""; 

    public CrudNhanVienDialog(Window owner, String mode, Object[] rowData) {
        super(owner, "", Dialog.ModalityType.APPLICATION_MODAL);
        this.mode = mode;

        setupUI();
        
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

        String title = mode.equals("ADD") ? "THÊM NHÂN VIÊN MỚI" : (mode.equals("EDIT") ? "CẬP NHẬT NHÂN VIÊN" : "XÓA NHÂN VIÊN");
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

        txtMaNV = createTextField();
        txtMaTK = createTextField();
        txtTenDangNhap = createTextField();
        txtMatKhau = createTextField();
        txtTenNV = createTextField();
        cbRole = createComboBox(new String[]{"NHAN_VIEN", "ADMIN"});
        txtSDT = createTextField();
        
        cbRole.setEnabled(false);
        
         

        pnlForm.add(createLabel("Mã nhân viên:")); pnlForm.add(txtMaNV);
        pnlForm.add(createLabel("Mã tài khoản:")); pnlForm.add(txtMaTK);
        pnlForm.add(createLabel("Tên đăng nhập:")); pnlForm.add(txtTenDangNhap);
        pnlForm.add(createLabel("Mật khẩu:")); pnlForm.add(txtMatKhau);
        pnlForm.add(createLabel("Tên nhân viên:")); pnlForm.add(txtTenNV);
        pnlForm.add(createLabel("Role:")); pnlForm.add(cbRole);
        pnlForm.add(createLabel("Số điện thoại:")); pnlForm.add(txtSDT);
        
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
    
    private void fillDataAndSetMode(Object[] rowData) {
        if (mode.equals("ADD")) {
            btnAction.setText("Thêm Mới");
        } else {
            if (rowData != null) {
                String maNV = rowData[0].toString();
                txtMaNV.setText(maNV);
                txtMaTK.setText(rowData[1].toString());
                txtTenDangNhap.setText(rowData[2].toString());
                txtMatKhau.setText(rowData[3].toString());
                txtTenNV.setText(rowData[4].toString());
                cbRole.setSelectedItem(rowData[5].toString());
                txtSDT.setText(rowData[6].toString());
                
                
                
            }

            if (mode.equals("EDIT")) {
                btnAction.setText("Cập Nhật");
                txtMaNV.setEditable(false);
                txtMaTK.setEditable(false);
            } else if (mode.equals("DELETE")) {
                btnAction.setText("Xác Nhận Xóa");
                txtMaNV.setEditable(false);
                txtMaTK.setEditable(false);
                txtTenDangNhap.setEditable(false);
                txtMatKhau.setEditable(false);
                txtTenNV.setEditable(false);
                cbRole.setEnabled(false);
                txtSDT.setEditable(false);
            }
        }
    }
    private void setupActions() {
        btnCancel.addActionListener(e -> dispose());

        btnAction.addActionListener(e -> {
            
            if (mode.equals("DELETE")) {
                int confirm = JOptionPane.showConfirmDialog(this, 
                    "Bạn có chắc chắn muốn xóa nhân viên này khỏi hệ thống không?\n", 
                    "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    NhanVienDAO dao = new NhanVienDAO();
                    if (dao.xoaNhanVien(txtMaNV.getText().trim())) {
                        JOptionPane.showMessageDialog(this, "Xóa nhân viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                        isSuccess = true;
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Xóa nhân viên thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
                return;
            }

            String maNV = txtMaNV.getText().trim();
            
            String tenNV = txtTenNV.getText().trim();
            String sdt = txtSDT.getText().trim();
            
            String maTK = txtMaTK.getText().trim();
            String tenDangNhap = txtTenDangNhap.getText().trim();
            String matkhau = txtMatKhau.getText().trim();

            if (maNV.isEmpty() || tenNV.isEmpty() || sdt.isEmpty() || maTK.isEmpty() || tenDangNhap.isEmpty() || matkhau.isEmpty() ) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!maNV.matches("(NV)\\d{2}")) {
                JOptionPane.showMessageDialog(this, "mã nhân viên bắt đầu NV và có 2 chữ số, vd:NV01", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if(!sdt.matches("\\d{6}")) {
            	JOptionPane.showMessageDialog(this, "mã số điện thoại gồm 6 số", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
          //dùng unicode property
        	//p{Lu}:bất kì chữ in hoa nào unicode
        	//p{Ll}: bất kì chữ in thường nào unicode
            if(!tenNV.matches("(\\p{Lu}\\p{Ll}+\\s)+\\p{Lu}\\p{Ll}+")) {
            	JOptionPane.showMessageDialog(this, "tên nhân viên bắt đầu băng chữ hoa và có 2 từ trở lên", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!maTK.matches("(TK)\\d{2}")) {
                JOptionPane.showMessageDialog(this, "mã tài khoản bắt đầu TK và có 2 chữ số, vd:TK01", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            

            

            NhanVienDAO dao = new NhanVienDAO();
            TaiKhoanDAO dao2 = new TaiKhoanDAO();
            
            TaiKhoan tk = new TaiKhoan();
            NhanVien nv = new NhanVien();
            nv.setMaNV(maNV);
            nv.setTenNV(tenNV);
            tk.setMaTK(maTK);
            tk.setTenDangNhap(tenDangNhap);
            tk.setMatKhau(matkhau);
            tk.setMaNV(nv);
            nv.setSdt(sdt);
            nv.setRoleNV(RoleNhanVien.valueOf(cbRole.getSelectedItem().toString()) );
            nv.setTrangThai(true);
            
            

            if (mode.equals("ADD")) {
                if (dao.timTheoMa(maNV) != null ) {
                    JOptionPane.showMessageDialog(this, "Mã nhân viên đã tồn tại trong hệ thống!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (dao2.timTheoMa(maTK) != null) {
                    JOptionPane.showMessageDialog(this, "Mã tài khoản đã tồn tại trong hệ thống!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                
                
                if (dao.themNhanVien(nv)){
                	if(dao2.themTaiKhoan(tk)) {
                	JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    isSuccess = true;
                    dispose();
                	}
                	
                }
                else {
                    JOptionPane.showMessageDialog(this, "Thêm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
              
                
            } else if (mode.equals("EDIT")) {
               
               
                
                if (dao.capNhatNhanVien(nv)) {
                    if(dao2.capNhatTaiKhoan(tk)) {
                    JOptionPane.showMessageDialog(this, "Cập nhật nhân viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    isSuccess = true;
                    dispose();
                    }
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
}
