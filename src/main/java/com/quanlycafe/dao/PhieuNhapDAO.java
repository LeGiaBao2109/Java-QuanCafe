package com.quanlycafe.dao;

import com.quanlycafe.entity.PhieuNhap;
import com.quanlycafe.util.DBConnect;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PhieuNhapDAO {
    public boolean taoPhieuNhap(PhieuNhap pn) {
        String sql = "INSERT INTO PHIEUNHAP (maPhieu, tenNCC, ngayNhap, tongTienNhap) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pn.getMaPhieu());
            ps.setString(2, pn.getTenNCC());
            ps.setTimestamp(3, Timestamp.valueOf(pn.getNgayNhap()));
            ps.setDouble(4, pn.getTongTienNhap());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<PhieuNhap> layTatCaPhieuNhap() {
        List<PhieuNhap> dsPN = new ArrayList<>();
        String sql = "SELECT * FROM PHIEUNHAP ORDER BY ngayNhap DESC";
        try (Connection conn = DBConnect.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                PhieuNhap pn = new PhieuNhap();
                pn.setMaPhieu(rs.getString("maPhieu"));
                pn.setTenNCC(rs.getString("tenNCC"));
                pn.setNgayNhap(rs.getTimestamp("ngayNhap").toLocalDateTime());
                pn.setTongTienNhap(rs.getDouble("tongTienNhap"));
                dsPN.add(pn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsPN;
    }

    public PhieuNhap timTheoMa(String maPhieu) {
        String sql = "SELECT * FROM PHIEUNHAP WHERE maPhieu = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPhieu);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    PhieuNhap pn = new PhieuNhap();
                    pn.setMaPhieu(rs.getString("maPhieu"));
                    pn.setTenNCC(rs.getString("tenNCC"));
                    pn.setNgayNhap(rs.getTimestamp("ngayNhap").toLocalDateTime());
                    pn.setTongTienNhap(rs.getDouble("tongTienNhap"));
                    return pn;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public double tinhTongTienNhap(LocalDateTime tuNgay, LocalDateTime denNgay) {
        String sql = "SELECT SUM(tongTienNhap) FROM PHIEUNHAP WHERE ngayNhap BETWEEN ? AND ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(tuNgay));
            ps.setTimestamp(2, Timestamp.valueOf(denNgay));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public List<PhieuNhap> locTheoNhaCungCap(String tenNCC) {
        List<PhieuNhap> dsPN = new ArrayList<>();
        String sql = "SELECT * FROM PHIEUNHAP WHERE tenNCC LIKE ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + tenNCC + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PhieuNhap pn = new PhieuNhap();
                    pn.setMaPhieu(rs.getString("maPhieu"));
                    pn.setTenNCC(rs.getString("tenNCC"));
                    pn.setNgayNhap(rs.getTimestamp("ngayNhap").toLocalDateTime());
                    pn.setTongTienNhap(rs.getDouble("tongTienNhap"));
                    dsPN.add(pn);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsPN;
    }
}