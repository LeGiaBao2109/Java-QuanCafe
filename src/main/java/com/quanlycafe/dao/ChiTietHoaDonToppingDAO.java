package com.quanlycafe.dao;

import com.quanlycafe.entity.ChiTietHoaDon;
import com.quanlycafe.entity.ChiTietHoaDonTopping;
import com.quanlycafe.entity.Topping;
import com.quanlycafe.util.DBConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChiTietHoaDonToppingDAO {
    public boolean themToppingVaoChiTiet(ChiTietHoaDonTopping ctt) {
        String sql = "INSERT INTO CT_HD_TOPPING (maCTHD, maTopping, soLuong, giaBan) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ctt.getMaCTHD().getMaCTHD());
            ps.setString(2, ctt.getMaTopping().getMaTopping());
            ps.setInt(3, ctt.getSoLuong());
            ps.setDouble(4, ctt.getGiaBan());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ChiTietHoaDonTopping> layToppingTheoMaCTHD(String maCTHD) {
        List<ChiTietHoaDonTopping> dsCTHDTP = new ArrayList<>();
        String sql = "SELECT * FROM CT_HD_TOPPING WHERE maCTHD = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maCTHD);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Topping tp = new Topping();
                    tp.setMaTopping(rs.getString("maTopping"));
                    ChiTietHoaDon ct = new ChiTietHoaDon();
                    ct.setMaCTHD(rs.getString("maCTHD"));

                    ChiTietHoaDonTopping ctt = new ChiTietHoaDonTopping(
                            ct, tp, rs.getInt("soLuong"), rs.getDouble("giaBan")
                    );
                    dsCTHDTP.add(ctt);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsCTHDTP;
    }

    public double tinhTongTienToppingTheoCTHD(String maCTHD) {
        String sql = "SELECT SUM(soLuong * giaBan) AS TongTien FROM CT_HD_TOPPING WHERE maCTHD = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maCTHD);
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