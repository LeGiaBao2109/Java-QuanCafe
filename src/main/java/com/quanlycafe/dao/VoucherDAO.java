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

    public List<Voucher> layVoucherKhaDung(int diemHienTai) {
        List<Voucher> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM VOUCHER WHERE trangThai = 1 AND diemCanDoi <= ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, diemHienTai);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Voucher v = new Voucher();
                    v.setMaCT(rs.getString("maCT"));
                    v.setTenCT(rs.getString("tenCT"));
                    v.setDiemCanDoi(rs.getInt("diemCanDoi"));
                    try {
                        v.setLoaiGiamGia(LoaiGiamGia.valueOf(rs.getString("loaiGiamGia")));
                    } catch (Exception e) {
                        v.setLoaiGiamGia(LoaiGiamGia.KHONG_GIAM);
                    }
                    v.setTrangThai(rs.getBoolean("trangThai"));
                    v.setGiaTriGiam(rs.getDouble("giaGiam"));
                    danhSach.add(v);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }

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
                try {
                    v.setLoaiGiamGia(LoaiGiamGia.valueOf(rs.getString("loaiGiamGia")));
                } catch (Exception e) {
                    v.setLoaiGiamGia(LoaiGiamGia.KHONG_GIAM);
                }
                v.setTrangThai(rs.getBoolean("trangThai"));
                v.setGiaTriGiam(rs.getDouble("giaGiam"));
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
                    try {
                        v.setLoaiGiamGia(LoaiGiamGia.valueOf(rs.getString("loaiGiamGia")));
                    } catch (Exception e) {
                        v.setLoaiGiamGia(LoaiGiamGia.KHONG_GIAM);
                    }
                    v.setTrangThai(rs.getBoolean("trangThai"));
                    v.setGiaTriGiam(rs.getDouble("giaGiam"));
                    return v;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean themVoucher(Voucher v) {
        String sql = "INSERT INTO VOUCHER (maCT, tenCT, diemCanDoi, loaiGiamGia, trangThai, giaGiam) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, v.getMaCT());
            ps.setString(2, v.getTenCT());
            ps.setInt(3, v.getDiemCanDoi());
            ps.setString(4, v.getLoaiGiamGia().name());
            ps.setBoolean(5, v.isTrangThai());
            ps.setDouble(6, v.getGiaTriGiam());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatVoucher(Voucher v) {
        String sql = "UPDATE VOUCHER SET tenCT = ?, diemCanDoi = ?, loaiGiamGia = ?, trangThai = ?, giaGiam = ? WHERE maCT = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, v.getTenCT());
            ps.setInt(2, v.getDiemCanDoi());
            ps.setString(3, v.getLoaiGiamGia().name());
            ps.setBoolean(4, v.isTrangThai());
            ps.setDouble(5, v.getGiaTriGiam());
            ps.setString(6, v.getMaCT());
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

    public boolean xoaVoucherVinhVien(String maCT) {
        String sql = "DELETE FROM VOUCHER WHERE maCT = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maCT);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Voucher> timKiemVoucher(String keyword) {
        List<Voucher> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM VOUCHER WHERE maCT LIKE ? OR tenCT LIKE ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Voucher v = new Voucher();
                    v.setMaCT(rs.getString("maCT"));
                    v.setTenCT(rs.getString("tenCT"));
                    v.setDiemCanDoi(rs.getInt("diemCanDoi"));
                    try {
                        v.setLoaiGiamGia(LoaiGiamGia.valueOf(rs.getString("loaiGiamGia")));
                    } catch (Exception e) {
                        v.setLoaiGiamGia(LoaiGiamGia.KHONG_GIAM);
                    }
                    v.setTrangThai(rs.getBoolean("trangThai"));
                    v.setGiaTriGiam(rs.getDouble("giaGiam"));
                    danhSach.add(v);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }
}