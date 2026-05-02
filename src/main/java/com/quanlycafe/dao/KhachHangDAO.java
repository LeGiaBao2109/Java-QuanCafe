package com.quanlycafe.dao;

import com.quanlycafe.entity.KhachHang;
import com.quanlycafe.util.DBConnect;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class KhachHangDAO {
    public boolean themKhachHang(KhachHang kh) {
        String sql = "INSERT INTO KHACHHANG (maKH, tenKH, sdt, ngayTao, diemTL) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kh.getMaKH());
            ps.setString(2, kh.getTenKH());
            ps.setString(3, kh.getSdt());
            ps.setTimestamp(4, Timestamp.valueOf(kh.getNgayTao()));
            ps.setInt(5, kh.getDiemTL());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public KhachHang timTheoSDT(String sdt) {
        String sql = "SELECT * FROM KHACHHANG WHERE sdt = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sdt);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    KhachHang kh = new KhachHang();
                    kh.setMaKH(rs.getString("maKH"));
                    kh.setTenKH(rs.getString("tenKH"));
                    kh.setSdt(rs.getString("sdt"));
                    kh.setNgayTao(rs.getTimestamp("ngayTao").toLocalDateTime());
                    kh.setDiemTL(rs.getInt("diemTL"));
                    return kh;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean congDiemTichLuy(String maKH, int diemCong) {
        String sql = "UPDATE KHACHHANG SET diemTL = diemTL + ? WHERE maKH = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, diemCong);
            ps.setString(2, maKH);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean truDiemTichLuy(String maKH, int diemTru) {
        String sql = "UPDATE KHACHHANG SET diemTL = diemTL - ? WHERE maKH = ? AND diemTL >= ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, diemTru);
            ps.setString(2, maKH);
            ps.setInt(3, diemTru);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<KhachHang> layTatCaKhachHang() {
        List<KhachHang> ds = new ArrayList<>();
        String sql = "SELECT * FROM KHACHHANG";
        try (Connection conn = DBConnect.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                KhachHang kh = new KhachHang();
                kh.setMaKH(rs.getString("maKH"));
                kh.setTenKH(rs.getString("tenKH"));
                kh.setSdt(rs.getString("sdt"));
                kh.setNgayTao(rs.getTimestamp("ngayTao").toLocalDateTime());
                kh.setDiemTL(rs.getInt("diemTL"));
                ds.add(kh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    public static void main(String[] args) {
        KhachHangDAO dao = new KhachHangDAO();
        System.out.println("--- KIỂM TRA HỆ THỐNG KHÁCH HÀNG (POS) ---");

        String sdtNhap = "0901234567";
        KhachHang kh = dao.timTheoSDT(sdtNhap);

        if (kh != null) {
            System.out.println("Khách hàng: " + kh.getTenKH());
            System.out.println("Điểm hiện tại: " + kh.getDiemTL());
            System.out.println("=> Nhân viên có thể tư vấn đổi Voucher dựa trên điểm này.");
        } else {
            System.out.println("Khách hàng mới. Vui lòng thêm thông tin để tích điểm!");
        }
    }
}