package com.quanlycafe.dao;

import com.quanlycafe.entity.PhieuNhap;
import com.quanlycafe.util.DBConnect;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PhieuNhapDAO {

    public String layMaNVTheoTen(String tenNV) {
        String sql = "SELECT TOP 1 maNV FROM NHANVIEN WHERE tenNV = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tenNV);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("maNV");
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return "NV01";
    }

    public String taoMaPhieuMoi() {
        String sql = "SELECT MAX(CAST(SUBSTRING(maPhieu, 3, LEN(maPhieu)) AS INT)) " +
                     "FROM PHIEUNHAP WHERE maPhieu NOT LIKE '%MOCK%'";
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
            while (rs.next()) list.add(rs.getString(1));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public List<Object[]> layLichSuNhapKho() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT pn.maPhieu, sp.tenSP, pn.tenNCC, ct.soLuong, ct.donGiaNhap, " +
                     "(ct.soLuong * ct.donGiaNhap) as ThanhTien, pn.ngayNhap, nv.tenNV " +
                     "FROM PHIEUNHAP pn " +
                     "JOIN CHITIETPHIEUNHAP ct ON pn.maPhieu = ct.maPhieu " +
                     "JOIN SANPHAM sp ON ct.maSP = sp.maSP " +
                     "LEFT JOIN NHANVIEN nv ON pn.maNV = nv.maNV " +
                     "ORDER BY pn.ngayNhap DESC";
        try (Connection conn = DBConnect.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString("maPhieu"),
                    rs.getString("tenSP"),
                    rs.getString("tenNCC"), 
                    rs.getInt("soLuong"),
                    rs.getDouble("donGiaNhap"),
                    rs.getDouble("ThanhTien"),
                    rs.getTimestamp("ngayNhap"), 
                    rs.getString("tenNV") != null ? rs.getString("tenNV") : "Không rõ" // 7
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public boolean thucHienNhapKho(PhieuNhap pn, List<Object[]> danhSachChiTiet, String maNV) {
        Connection conn = null;
        try {
            conn = DBConnect.getConnection();
            conn.setAutoCommit(false);

            String sqlPhieu = "INSERT INTO PHIEUNHAP (maPhieu, tenNCC, ngayNhap, tongTienNhap, maNV) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement psPhieu = conn.prepareStatement(sqlPhieu)) {
                psPhieu.setString(1, pn.getMaPhieu());
                psPhieu.setString(2, pn.getTenNCC());
                psPhieu.setTimestamp(3, Timestamp.valueOf(pn.getNgayNhap()));
                psPhieu.setDouble(4, pn.getTongTienNhap());
                psPhieu.setString(5, maNV);
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
                try { conn.setAutoCommit(true); conn.close(); } 
                catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }
}