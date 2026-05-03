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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsCTHD;
    }

    public boolean capNhatSoLuong(String maCTHD, int soLuongMoi, double giaMoi) {
        double thanhTienMoi = soLuongMoi * giaMoi;

        String sql = "UPDATE CHITIETHOADON SET soLuong = ?, thanhTien = ? WHERE maCTHD = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, soLuongMoi);
            ps.setDouble(2, thanhTienMoi);
            ps.setString(3, maCTHD);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public double tinhTongTienDonHang(String maDH) {
        String sql = "SELECT SUM(thanhTien) FROM CHITIETHOADON WHERE maDH = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maDH);
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
}