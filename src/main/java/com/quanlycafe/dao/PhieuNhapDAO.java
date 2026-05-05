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

    public String taoMaPhieuMoi() {
        String sql = "SELECT MAX(CAST(SUBSTRING(maPhieu, 3, LEN(maPhieu)) AS INT)) FROM PHIEUNHAP";
        try (Connection conn = DBConnect.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                int maxId = rs.getInt(1);
                return "PN" + String.format("%03d", maxId + 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "PN001";
    }

    public List<String> layDanhSachNhaCungCap() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT DISTINCT tenNCC FROM PHIEUNHAP WHERE tenNCC IS NOT NULL AND tenNCC <> ''";
        try (Connection conn = DBConnect.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean thucHienNhapKho(PhieuNhap pn, List<Object[]> danhSachChiTiet) {
        Connection conn = null;
        try {
            conn = DBConnect.getConnection();
            conn.setAutoCommit(false);

            String sqlPhieu = "INSERT INTO PHIEUNHAP (maPhieu, tenNCC, ngayNhap, tongTienNhap) VALUES (?, ?, ?, ?)";
            try (PreparedStatement psPhieu = conn.prepareStatement(sqlPhieu)) {
                psPhieu.setString(1, pn.getMaPhieu());
                psPhieu.setString(2, pn.getTenNCC());
                psPhieu.setTimestamp(3, Timestamp.valueOf(pn.getNgayNhap()));
                psPhieu.setDouble(4, pn.getTongTienNhap());
                psPhieu.executeUpdate();
            }

            String sqlChiTiet = "INSERT INTO CHITIETPHIEUNHAP (maPhieu, maSP, soLuong, donGiaNhap) VALUES (?, ?, ?, ?)";
            String sqlCapNhatKho = "UPDATE SANPHAM SET tonKho = tonKho + ? WHERE maSP = ?";
            
            try (PreparedStatement psChiTiet = conn.prepareStatement(sqlChiTiet);
                 PreparedStatement psKho = conn.prepareStatement(sqlCapNhatKho)) {
                
                for (Object[] row : danhSachChiTiet) {
                    String maSP = row[0].toString();
                    int soLuong = Integer.parseInt(row[2].toString());
                    double donGia = Double.parseDouble(row[3].toString());

                    psChiTiet.setString(1, pn.getMaPhieu());
                    psChiTiet.setString(2, maSP);
                    psChiTiet.setInt(3, soLuong);
                    psChiTiet.setDouble(4, donGia);
                    psChiTiet.addBatch();

                    psKho.setInt(1, soLuong);
                    psKho.setString(2, maSP);
                    psKho.addBatch();
                }
                psChiTiet.executeBatch();
                psKho.executeBatch();
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }
}