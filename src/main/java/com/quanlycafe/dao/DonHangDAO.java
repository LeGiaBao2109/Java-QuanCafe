package com.quanlycafe.dao;

import com.quanlycafe.entity.DonHang;
import com.quanlycafe.entity.KhachHang;
import com.quanlycafe.entity.NhanVien;
import com.quanlycafe.util.DBConnect;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DonHangDAO {

    public boolean themDonHang(DonHang dh) {
        String sql = "INSERT INTO DONHANG (maDH, stt, ngayTao, maNV, maKH, trangThai, tongTien) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dh.getMaDH());
            ps.setInt(2, dh.getStt());
            ps.setTimestamp(3, Timestamp.valueOf(dh.getNgayTao()));
            ps.setString(4, dh.getMaNV().getMaNV());
            ps.setString(5, dh.getMaKH().getMaKH());
            ps.setBoolean(6, dh.isTrangThai());
            ps.setDouble(7, dh.getTongTien());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<DonHang> layDonHangChuaThanhToan() {
        List<DonHang> dsDH = new ArrayList<>();
        String sql = "SELECT * FROM DONHANG WHERE trangThai = 0 ORDER BY ngayTao ASC";
        try (Connection conn = DBConnect.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                DonHang dh = new DonHang();
                dh.setMaDH(rs.getString("maDH"));
                dh.setStt(rs.getInt("stt"));
                dh.setNgayTao(rs.getTimestamp("ngayTao").toLocalDateTime());

                NhanVien nv = new NhanVien();
                nv.setMaNV(rs.getString("maNV"));
                dh.setMaNV(nv);

                KhachHang kh = new KhachHang();
                kh.setMaKH(rs.getString("maKH"));
                dh.setMaKH(kh);

                dh.setTrangThai(rs.getBoolean("trangThai"));
                dh.setTongTien(rs.getDouble("tongTien"));
                dsDH.add(dh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsDH;
    }

    public DonHang layTheoMa(String maDH) {
        String sql = "SELECT * FROM DONHANG WHERE maDH = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maDH);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    DonHang dh = new DonHang();
                    dh.setMaDH(rs.getString("maDH"));
                    dh.setStt(rs.getInt("stt"));
                    dh.setNgayTao(rs.getTimestamp("ngayTao").toLocalDateTime());

                    NhanVien nv = new NhanVien();
                    nv.setMaNV(rs.getString("maNV"));
                    dh.setMaNV(nv);

                    KhachHang kh = new KhachHang();
                    kh.setMaKH(rs.getString("maKH"));
                    dh.setMaKH(kh);

                    dh.setTrangThai(rs.getBoolean("trangThai"));
                    dh.setTongTien(rs.getDouble("tongTien"));
                    return dh;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateTongTien(String maDH, double tongTien) {
        String sql = "UPDATE DONHANG SET tongTien = ? WHERE maDH = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, tongTien);
            ps.setString(2, maDH);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatTrangThai(String maDH, boolean trangThai) {
        String sql = "UPDATE DONHANG SET trangThai = ? WHERE maDH = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, trangThai);
            ps.setString(2, maDH);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaDonHang(String maDH) {
        Connection conn = null;
        try {
            conn = DBConnect.getConnection();
            conn.setAutoCommit(false);

            String sqlTopping = "DELETE FROM CT_HD_TOPPING WHERE maCTHD IN (SELECT maCTHD FROM CHITIETHOADON WHERE maDH = ?)";
            try (PreparedStatement ps1 = conn.prepareStatement(sqlTopping)) {
                ps1.setString(1, maDH);
                ps1.executeUpdate();
            }

            String sqlChiTiet = "DELETE FROM CHITIETHOADON WHERE maDH = ?";
            try (PreparedStatement ps2 = conn.prepareStatement(sqlChiTiet)) {
                ps2.setString(1, maDH);
                ps2.executeUpdate();
            }

            String sqlDonHang = "DELETE FROM DONHANG WHERE maDH = ?";
            int rows;
            try (PreparedStatement ps3 = conn.prepareStatement(sqlDonHang)) {
                ps3.setString(1, maDH);
                rows = ps3.executeUpdate();
            }

            conn.commit();
            return rows > 0;
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}