package com.quanlycafe.dao;

import com.quanlycafe.entity.NhanVien;
import com.quanlycafe.entity.RoleNhanVien;
import com.quanlycafe.entity.TaiKhoan;
import com.quanlycafe.util.DBConnect;
import java.sql.*;

public class TaiKhoanDAO {
    public TaiKhoan dangNhap(String tenDN, String matKhau) {
        String sql = "SELECT tk.*, nv.tenNV, nv.sdt, nv.roleNV, nv.trangThai " +
                "FROM TAIKHOAN tk " +
                "JOIN NHANVIEN nv ON tk.maNV = nv.maNV " +
                "WHERE tk.tenDangNhap = ? AND tk.matKhau = ? AND nv.trangThai = 1";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tenDN);
            ps.setString(2, matKhau);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    NhanVien nv = new NhanVien(
                            rs.getString("maNV"),
                            rs.getString("tenNV"),
                            rs.getString("sdt"),
                            RoleNhanVien.valueOf(rs.getString("roleNV")),
                            rs.getBoolean("trangThai")
                    );

                    return new TaiKhoan(
                            rs.getString("maTK"),
                            rs.getString("tenDangNhap"),
                            rs.getString("matKhau"),
                            nv
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean voHieuHoaTaiKhoan(String maTK) {
        String sql = "UPDATE NHANVIEN SET trangThai = 0 WHERE maNV = (SELECT maNV FROM TAIKHOAN WHERE maTK = ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maTK);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean kichHoatTaiKhoan(String maTK) {
        String sql = "UPDATE NHANVIEN SET trangThai = 1 WHERE maNV = (SELECT maNV FROM TAIKHOAN WHERE maTK = ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maTK);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}