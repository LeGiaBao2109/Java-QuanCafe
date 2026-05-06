package com.quanlycafe.dao;

import com.quanlycafe.entity.NhanVien;
import com.quanlycafe.entity.RoleNhanVien;
import com.quanlycafe.entity.SanPham;
import com.quanlycafe.util.DBConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class NhanVienDAO {
    public List<NhanVien> layTatCaNhanVien() {
        List<NhanVien> dsNV = new ArrayList<>();
        String sql = "SELECT * FROM NHANVIEN";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                NhanVien nv = new NhanVien(
                        rs.getString("maNV"),
                        rs.getString("tenNV"),
                        rs.getString("sdt"),
                        RoleNhanVien.valueOf(rs.getString("roleNV")),
                        rs.getBoolean("trangThai")
                );
                dsNV.add(nv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsNV;
    }

    public NhanVien timTheoMa(String maNV) {
        String sql = "SELECT * FROM NHANVIEN WHERE maNV = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maNV);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new NhanVien(
                            rs.getString("maNV").trim(),
                            rs.getString("tenNV").trim(),
                            rs.getString("sdt").trim(),
                            RoleNhanVien.valueOf(rs.getString("roleNV").trim()),
                            rs.getBoolean("trangThai")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean capNhatNhanVien(NhanVien nv) {
        String sql = "UPDATE NHANVIEN SET tenNV = ?, sdt = ? WHERE maNV = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nv.getTenNV());
            ps.setString(2, nv.getSdt());
            ps.setString(3, nv.getMaNV());
            int rows = ps.executeUpdate();
            System.out.println("DEBUG DAO: Số dòng đã sửa: " + rows);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<Object[]> layDanhSachNhanVienQuanLy() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT nv.maNV, tk.maTK, tk.tenDangNhap, tk.matKhau, nv.tenNV, nv.roleNV, nv.sdt, nv.trangThai " +
                     "FROM NHANVIEN nv " +
                     "JOIN TAIKHOAN tk ON nv.maNV = tk.maNV " +
                     "WHERE nv.trangThai = 1";
                     
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
             
            
            while (rs.next()) {
                
                String maNV = rs.getString("maNV");
                String maTK = rs.getString("maTK");
                String tenDangNhap = rs.getString("tenDangNhap");
                String matKhau = rs.getString("matKhau");
                String tenNV = rs.getString("tenNV"); 
                String roleNV = rs.getString("roleNV");
                
                int sdt = rs.getInt("sdt");
                
                list.add(new Object[]{maNV, maTK, tenDangNhap, matKhau, tenNV, roleNV, sdt});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public boolean themNhanVien(NhanVien nv) {
        String sql = "INSERT INTO NHANVIEN (maNV, tenNV, roleNV, sdt, trangThai) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nv.getMaNV());
            ps.setString(2, nv.getTenNV());
            ps.setString(3, nv.getRoleNV().name());
            ps.setString(4, nv.getSdt());
            
            ps.setBoolean(5, nv.isTrangThai());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean xoaNhanVien(String maNV) {
        String sql = "UPDATE NHANVIEN SET trangThai = 0 WHERE maNV = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNV);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}