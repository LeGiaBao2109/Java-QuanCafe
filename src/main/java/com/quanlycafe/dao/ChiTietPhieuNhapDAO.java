package com.quanlycafe.dao;

import com.quanlycafe.entity.ChiTietPhieuNhap;
import com.quanlycafe.entity.PhieuNhap;
import com.quanlycafe.entity.SanPham;
import com.quanlycafe.util.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChiTietPhieuNhapDAO {
    public boolean themChiTiet(ChiTietPhieuNhap ct) {
        String sql = "INSERT INTO CHITIETPHIEUNHAP (maPhieu, maSP, soLuong, donGiaNhap) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ct.getMaPhieu().getMaPhieu());
            ps.setString(2, ct.getMaSP().getMaSP());
            ps.setInt(3, ct.getSoLuong());
            ps.setDouble(4, ct.getDonGiaNhap());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ChiTietPhieuNhap> layTheoMaPhieu(String maPhieu) {
        List<ChiTietPhieuNhap> dsCTPN = new ArrayList<>();
        String sql = "SELECT * FROM CHITIETPHIEUNHAP WHERE maPhieu = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maPhieu);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PhieuNhap pn = new PhieuNhap();
                    pn.setMaPhieu(rs.getString("maPhieu"));

                    SanPham sp = new SanPham();
                    sp.setMaSP(rs.getString("maSP"));

                    ChiTietPhieuNhap ct = new ChiTietPhieuNhap(
                            pn, sp,
                            rs.getInt("soLuong"),
                            rs.getDouble("donGiaNhap")
                    );
                    dsCTPN.add(ct);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsCTPN;
    }

    public double tinhTongTienTheoPhieu(String maPhieu) {
        String sql = "SELECT SUM(soLuong * donGiaNhap) AS TongTien FROM CHITIETPHIEUNHAP WHERE maPhieu = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maPhieu);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("TongTien");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}