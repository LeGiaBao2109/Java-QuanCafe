package com.quanlycafe.dao;

import com.quanlycafe.entity.Topping;
import com.quanlycafe.util.DBConnect;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ToppingDAO {
    public boolean themTopping(Topping tp) {
        String sql = "INSERT INTO TOPPING (maTopping, tenTopping, gia, trangThai) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tp.getMaTopping());
            ps.setString(2, tp.getTenTopping());
            ps.setDouble(3, tp.getGia());
            ps.setBoolean(4, tp.isTrangThai());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatTopping(Topping tp) {
        String sql = "UPDATE TOPPING SET tenTopping = ?, gia = ?, trangThai = ? WHERE maTopping = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tp.getTenTopping());
            ps.setDouble(2, tp.getGia());
            ps.setBoolean(3, tp.isTrangThai());
            ps.setString(4, tp.getMaTopping());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Topping> layTatCaTopping() {
        List<Topping> ds = new ArrayList<>();
        String sql = "SELECT * FROM TOPPING";
        try (Connection conn = DBConnect.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Topping tp = new Topping();
                tp.setMaTopping(rs.getString("maTopping"));
                tp.setTenTopping(rs.getString("tenTopping"));
                tp.setGia(rs.getDouble("gia"));
                tp.setTrangThai(rs.getBoolean("trangThai"));
                ds.add(tp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    public List<Topping> layToppingActive() {
        List<Topping> ds = new ArrayList<>();
        String sql = "SELECT * FROM TOPPING WHERE trangThai = 1";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Topping tp = new Topping();
                tp.setMaTopping(rs.getString("maTopping"));
                tp.setTenTopping(rs.getString("tenTopping"));
                tp.setGia(rs.getDouble("gia"));
                tp.setTrangThai(rs.getBoolean("trangThai"));
                ds.add(tp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    public Topping timTheoMa(String maTopping) {
        String sql = "SELECT * FROM TOPPING WHERE maTopping = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maTopping);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Topping tp = new Topping();
                    tp.setMaTopping(rs.getString("maTopping"));
                    tp.setTenTopping(rs.getString("tenTopping"));
                    tp.setGia(rs.getDouble("gia"));
                    tp.setTrangThai(rs.getBoolean("trangThai"));
                    return tp;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}