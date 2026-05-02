package com.quanlycafe.dao;

import com.quanlycafe.entity.ChiTietHoaDon;
import com.quanlycafe.entity.DonHang;
import com.quanlycafe.entity.KichCo;
import com.quanlycafe.entity.MucDa;
import com.quanlycafe.entity.MucDuong;
import com.quanlycafe.util.DBConnect;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChiTietHoaDonDAO {
    public boolean themChiTiet(ChiTietHoaDon ct) {
        String sql = "INSERT INTO CHITIETHOADON (maCTHD, maDH, maSize, soLuong, luongDa, luongDuong, ghiChu, thanhTien) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ct.getMaCTHD());
            ps.setString(2, ct.getMaDH().getMaDH());
            ps.setString(3, ct.getMaSize().getMaSize());
            ps.setInt(4, ct.getSoLuong());
            ps.setString(5, ct.getLuongDa().name());
            ps.setString(6, ct.getLuongDuong().name());
            ps.setString(7, ct.getGhiChu());
            ps.setDouble(8, ct.getThanhTien());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ChiTietHoaDon> layDanhSachTheoMaDH(String maDH) {
        List<ChiTietHoaDon> dsCTHD = new ArrayList<>();
        String sql = "SELECT * FROM CHITIETHOADON WHERE maDH = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maDH);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DonHang dh = new DonHang();
                    dh.setMaDH(rs.getString("maDH"));
                    KichCo kc = new KichCo();
                    kc.setMaSize(rs.getString("maSize"));

                    ChiTietHoaDon ct = new ChiTietHoaDon(
                            rs.getString("maCTHD"),
                            dh,
                            kc,
                            rs.getInt("soLuong"),
                            0,
                            rs.getString("ghiChu"),
                            MucDa.valueOf(rs.getString("luongDa")),
                            MucDuong.valueOf(rs.getString("luongDuong"))
                    );
                    dsCTHD.add(ct);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return dsCTHD;
    }

    public static void main(String[] args) {
        ChiTietHoaDonDAO dao = new ChiTietHoaDonDAO();
        System.out.println("--- TEST LƯU CHI TIẾT MÓN ĂN ---");

        DonHang dh = new DonHang(); dh.setMaDH("DH001");
        KichCo kc = new KichCo(); kc.setMaSize("S01");

        ChiTietHoaDon ct = new ChiTietHoaDon(
                "CT001", dh, kc, 2, 35000, "Ít ngọt",
                MucDa.DA_50, MucDuong.DUONG_25
        );

        if(dao.themChiTiet(ct)) {
            System.out.println("✅ Lưu chi tiết thành công! Thành tiền: " + ct.getThanhTien());
        } else {
            System.out.println("❌ Thất bại. Kiểm tra khóa ngoại!");
        }
    }
}