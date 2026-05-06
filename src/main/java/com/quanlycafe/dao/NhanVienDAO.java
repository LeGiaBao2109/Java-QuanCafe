package com.quanlycafe.dao;

import com.quanlycafe.entity.NhanVien;
import com.quanlycafe.entity.RoleNhanVien;
import com.quanlycafe.util.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                        rs.getString("maNV").trim(),
                        rs.getString("tenNV").trim(),
                        rs.getString("sdt").trim(),
                        RoleNhanVien.valueOf(rs.getString("roleNV").trim()),
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
        String sql = "UPDATE NHANVIEN SET tenNV = ?, sdt = ?, roleNV = ? WHERE maNV = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nv.getTenNV());
            ps.setString(2, nv.getSdt());
            ps.setString(3, nv.getRoleNV().name());
            ps.setString(4, nv.getMaNV());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Object[]> layDanhSachNhanVienQuanLy() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT nv.maNV, tk.maTK, tk.tenDangNhap, tk.matKhau, nv.tenNV, nv.roleNV, nv.sdt " +
                "FROM NHANVIEN nv " +
                "JOIN TAIKHOAN tk ON nv.maNV = tk.maNV";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Object[]{
                        rs.getString("maNV").trim(),
                        rs.getString("maTK").trim(),
                        rs.getString("tenDangNhap").trim(),
                        rs.getString("matKhau").trim(),
                        rs.getString("tenNV").trim(),
                        rs.getString("roleNV").trim(),
                        rs.getString("sdt").trim()
                });
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
        String sqlDeleteTK = "DELETE FROM TAIKHOAN WHERE maNV = ?";
        String sqlDeleteNV = "DELETE FROM NHANVIEN WHERE maNV = ?";

        try (Connection conn = DBConnect.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement psTK = conn.prepareStatement(sqlDeleteTK);
                 PreparedStatement psNV = conn.prepareStatement(sqlDeleteNV)) {

                psTK.setString(1, maNV);
                psTK.executeUpdate();

                psNV.setString(1, maNV);
                int result = psNV.executeUpdate();

                conn.commit();
                return result > 0;
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String taoMaNVTuSinh() {
        String sql = "SELECT MAX(maNV) FROM NHANVIEN";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                String lastMa = rs.getString(1);
                if (lastMa == null) return "NV01";
                int num = Integer.parseInt(lastMa.substring(2).trim()) + 1;
                return String.format("NV%02d", num);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "NV01";
    }
}