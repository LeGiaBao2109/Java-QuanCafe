package com.quanlycafe.dao;

import com.quanlycafe.entity.DonHang;
import com.quanlycafe.entity.KhachHang;
import com.quanlycafe.entity.NhanVien;
import com.quanlycafe.util.DBConnect;
import java.sql.*;
import java.time.LocalDateTime;
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

    public List<DonHang> layTatCaDonHang() {
        List<DonHang> dsTK = new ArrayList<>();
        String sql = "SELECT * FROM DONHANG";
        try (Connection conn = DBConnect.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                NhanVien nv = new NhanVien(); nv.setMaNV(rs.getString("maNV"));
                KhachHang kh = new KhachHang(); kh.setMaKH(rs.getString("maKH"));

                DonHang dh = new DonHang();
                dh.setMaDH(rs.getString("maDH"));
                dh.setStt(rs.getInt("stt"));
                dh.setNgayTao(rs.getTimestamp("ngayTao").toLocalDateTime());
                dh.setMaNV(nv);
                dh.setMaKH(kh);
                dh.setTrangThai(rs.getBoolean("trangThai"));
                dh.setTongTien(rs.getDouble("tongTien"));

                dsTK.add(dh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsTK;
    }

    public boolean xoaDonHang(String maDH) {
        Connection conn = null;
        try {
            conn = DBConnect.getConnection();
            conn.setAutoCommit(false);

            String sqlTopping = "DELETE FROM CT_HD_TOPPING WHERE maCTHD IN " +
                    "(SELECT maCTHD FROM CHITIETHOADON WHERE maDH = ?)";
            PreparedStatement ps1 = conn.prepareStatement(sqlTopping);
            ps1.setString(1, maDH);
            ps1.executeUpdate();

            String sqlChiTiet = "DELETE FROM CHITIETHOADON WHERE maDH = ?";
            PreparedStatement ps2 = conn.prepareStatement(sqlChiTiet);
            ps2.setString(1, maDH);
            ps2.executeUpdate();

            String sqlDonHang = "DELETE FROM DONHANG WHERE maDH = ?";
            PreparedStatement ps3 = conn.prepareStatement(sqlDonHang);
            ps3.setString(1, maDH);
            int rows = ps3.executeUpdate();

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

    public static void main(String[] args) {
        DonHangDAO dao = new DonHangDAO();

        NhanVien nv = new NhanVien(); nv.setMaNV("NV01");
        KhachHang kh = new KhachHang(); kh.setMaKH("KH01");

        System.out.println("--- TEST TẠO ĐƠN HÀNG ---");
        DonHang dh = new DonHang("DH001", 1, LocalDateTime.now(), nv, kh, true);
        if(dao.themDonHang(dh)) {
            System.out.println("✅ Thêm thành công!");
        }

        System.out.println("--- TEST XÓA ĐƠN HÀNG ---");
        if(dao.xoaDonHang("DH001")) {
            System.out.println("✅ Xóa thành công!");
        } else {
            System.out.println("❌ Thất bại!");
        }
    }
}