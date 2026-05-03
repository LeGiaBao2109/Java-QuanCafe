package com.quanlycafe.dao;

import com.quanlycafe.entity.LoaiGiamGia;
import com.quanlycafe.entity.Voucher;
import com.quanlycafe.util.DBConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class VoucherDAO {

    public List<Voucher> layTatCaVoucher() {
        List<Voucher> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM VOUCHER";
        try (Connection conn = DBConnect.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Voucher v = new Voucher();
                v.setMaCT(rs.getString("maCT"));
                v.setTenCT(rs.getString("tenCT"));
                v.setDiemCanDoi(rs.getInt("diemCanDoi"));
                v.setLoaiGiamGia(LoaiGiamGia.valueOf(rs.getString("loaiGiamGia")));
                v.setTrangThai(rs.getBoolean("trangThai"));
                danhSach.add(v);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    public Voucher timTheoMa(String maCT) {
        String sql = "SELECT * FROM VOUCHER WHERE maCT = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maCT);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Voucher v = new Voucher();
                    v.setMaCT(rs.getString("maCT"));
                    v.setTenCT(rs.getString("tenCT"));
                    v.setDiemCanDoi(rs.getInt("diemCanDoi"));
                    v.setLoaiGiamGia(LoaiGiamGia.valueOf(rs.getString("loaiGiamGia")));
                    v.setTrangThai(rs.getBoolean("trangThai"));
                    return v;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean themVoucher(Voucher v) {
        String sql = "INSERT INTO VOUCHER (maCT, tenCT, diemCanDoi, loaiGiamGia, trangThai) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, v.getMaCT());
            ps.setString(2, v.getTenCT());
            ps.setInt(3, v.getDiemCanDoi());
            ps.setString(4, v.getLoaiGiamGia().name());
            ps.setBoolean(5, v.isTrangThai());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatVoucher(Voucher v) {
        String sql = "UPDATE VOUCHER SET tenCT = ?, diemCanDoi = ?, loaiGiamGia = ?, trangThai = ? WHERE maCT = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, v.getTenCT());
            ps.setInt(2, v.getDiemCanDoi());
            ps.setString(3, v.getLoaiGiamGia().name());
            ps.setBoolean(4, v.isTrangThai());
            ps.setString(5, v.getMaCT());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean huyVoucher(String maCT) {
        String sql = "UPDATE VOUCHER SET trangThai = 0 WHERE maCT = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maCT);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}