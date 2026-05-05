package com.quanlycafe.dao;

import com.quanlycafe.entity.ChiTietHoaDon;
import com.quanlycafe.entity.DonHang;
import com.quanlycafe.entity.MucDa;
import com.quanlycafe.entity.MucDuong;
import com.quanlycafe.util.DBConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChiTietHoaDonDAO {
    public boolean themChiTiet(ChiTietHoaDon ct) {
        String sql = "INSERT INTO CHITIETHOADON (maCTHD, maDH, maSize, soLuong, luongDa, luongDuong, ghiChu, thanhTien) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ct.getMaCTHD());
            ps.setString(2, ct.getDonHang().getMaDH());
            ps.setString(3, ct.getMaSize());
            ps.setInt(4, ct.getSoLuong());

            // Đảm bảo name() trả về đúng DA_0, DA_50... khớp với CONSTRAINT
            ps.setString(5, ct.getLuongDa().name());
            ps.setString(6, ct.getLuongDuong().name());

            // Cắt bớt ghi chú nếu quá dài để tránh lỗi NVARCHAR(255)
            String note = ct.getGhiChu();
            if (note != null && note.length() > 255) {
                note = note.substring(0, 250) + "...";
            }
            ps.setString(7, note);

            ps.setDouble(8, ct.getThanhTien());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            // Log chi tiết để debug
            System.err.println("Lỗi SQL tại maCTHD: " + ct.getMaCTHD());
            System.err.println("Giá trị đá: " + ct.getLuongDa().name());
            System.err.println("Giá trị đường: " + ct.getLuongDuong().name());
            e.printStackTrace();
            return false;
        }
    }

    public List<ChiTietHoaDon> layDanhSachTheoMaDH(String maDH) {
        List<ChiTietHoaDon> dsCTHD = new ArrayList<>();
        String sql = "SELECT ct.*, sp.TenSP, kc.TenSize " +
                "FROM CHITIETHOADON ct " +
                "JOIN KICHCO kc ON ct.maSize = kc.maSize " +
                "JOIN SANPHAM sp ON kc.maSP = sp.maSP " +
                "WHERE ct.maDH = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maDH);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ChiTietHoaDon ct = new ChiTietHoaDon();
                    ct.setMaCTHD(rs.getString("maCTHD"));

                    DonHang dh = new DonHang();
                    dh.setMaDH(rs.getString("maDH"));
                    ct.setDonHang(dh);

                    ct.setMaSize(rs.getString("maSize"));

                    String tenDayDu = rs.getString("TenSP") + " (" + rs.getString("TenSize") + ")";
                    String ghiChuGoc = rs.getString("ghiChu");
                    ct.setGhiChu(tenDayDu + " | " + (ghiChuGoc != null ? ghiChuGoc : ""));

                    ct.setSoLuong(rs.getInt("soLuong"));
                    ct.setThanhTien(rs.getDouble("thanhTien"));
                    ct.setDonGia(ct.getSoLuong() > 0 ? ct.getThanhTien() / ct.getSoLuong() : 0);
                    ct.setDsTopping(layToppingCuaChiTiet(ct.getMaCTHD()));

                    try {
                        ct.setLuongDa(MucDa.valueOf(rs.getString("luongDa")));
                        ct.setLuongDuong(MucDuong.valueOf(rs.getString("luongDuong")));
                    } catch (Exception e) {
                        ct.setLuongDa(MucDa.DA_100);
                        ct.setLuongDuong(MucDuong.DUONG_100);
                    }

                    dsCTHD.add(ct);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsCTHD;
    }

    public boolean capNhatSoLuong(String maCTHD, int soLuongMoi, double donGiaMoi) {
        String sql = "UPDATE CHITIETHOADON SET soLuong = ?, thanhTien = ? WHERE maCTHD = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, soLuongMoi);
            ps.setDouble(2, soLuongMoi * donGiaMoi);
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
                if (rs.next()) return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public boolean xoaChiTiet(String maCTHD) {
        String sqlXoaTopping = "DELETE FROM CT_HD_TOPPING WHERE maCTHD = ?";
        String sqlXoaCT = "DELETE FROM CHITIETHOADON WHERE maCTHD = ?";
        try (Connection conn = DBConnect.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps1 = conn.prepareStatement(sqlXoaTopping);
                 PreparedStatement ps2 = conn.prepareStatement(sqlXoaCT)) {
                ps1.setString(1, maCTHD);
                ps1.executeUpdate();
                ps2.setString(1, maCTHD);
                int res = ps2.executeUpdate();
                conn.commit();
                return res > 0;
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private List<String> layToppingCuaChiTiet(String maCTHD) {
        List<String> toppings = new ArrayList<>();
        String sql = "SELECT t.tenTopping FROM CT_HD_TOPPING c JOIN TOPPING t ON c.maTopping = t.maTopping WHERE c.maCTHD = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maCTHD);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) toppings.add(rs.getString("tenTopping"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toppings;
    }

    public boolean luuTopping(String maCTHD, List<String> maToppings) {
        String sql = "INSERT INTO CT_HD_TOPPING (maCTHD, maTopping) VALUES (?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (String maTopping : maToppings) {
                ps.setString(1, maCTHD);
                ps.setString(2, maTopping);
                ps.addBatch();
            }
            ps.executeBatch();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}