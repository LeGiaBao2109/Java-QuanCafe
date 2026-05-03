package com.quanlycafe.dao;

import com.quanlycafe.entity.KichCo;
import com.quanlycafe.entity.SanPham;
import com.quanlycafe.util.DBConnect;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KichCoDAO {
    public List<KichCo> layKichCoTheoSP(String maSP) {
        List<KichCo> dsKC = new ArrayList<>();
        String sql = "SELECT * FROM KICHCO WHERE maSP = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maSP);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    KichCo kc = new KichCo();
                    kc.setMaSize(rs.getString("maSize"));

                    SanPham sp = new SanPham();
                    sp.setMaSP(rs.getString("maSP"));
                    kc.setMaSP(sp);

                    kc.setTenSize(rs.getString("tenSize"));
                    kc.setGia(rs.getDouble("gia"));

                    dsKC.add(kc);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsKC;
    }

    public KichCo timTheoMa(String maSize) {
        String sql = "SELECT * FROM KICHCO WHERE maSize = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maSize);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    KichCo kc = new KichCo();
                    kc.setMaSize(rs.getString("maSize"));

                    SanPham sp = new SanPham();
                    sp.setMaSP(rs.getString("maSP"));
                    kc.setMaSP(sp);

                    kc.setTenSize(rs.getString("tenSize"));
                    kc.setGia(rs.getDouble("gia"));
                    return kc;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean themKichCo(KichCo kc) {
        String sql = "INSERT INTO KICHCO (maSize, maSP, tenSize, gia) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, kc.getMaSize());
            ps.setString(2, kc.getMaSP().getMaSP());
            ps.setString(3, kc.getTenSize());
            ps.setDouble(4, kc.getGia());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatKichCo(KichCo kc) {
        String sql = "UPDATE KICHCO SET tenSize = ?, gia = ? WHERE maSize = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, kc.getTenSize());
            ps.setDouble(2, kc.getGia());
            ps.setString(3, kc.getMaSize());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaKichCo(String maSize) {
        String sql = "DELETE FROM KICHCO WHERE maSize = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maSize);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}