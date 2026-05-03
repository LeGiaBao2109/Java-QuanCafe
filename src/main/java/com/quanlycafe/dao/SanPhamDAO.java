package com.quanlycafe.dao;

import com.quanlycafe.entity.DanhMuc;
import com.quanlycafe.entity.SanPham;
import com.quanlycafe.util.DBConnect;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SanPhamDAO {
    public boolean themSanPham(SanPham sp) {
        String sql = "INSERT INTO SANPHAM (maSP, tenSP, anhSP, maDM, donViTinh, tonKho, trangThai, ngayTao, ngayCapNhat) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sp.getMaSP());
            ps.setString(2, sp.getTenSP());
            ps.setString(3, sp.getAnhSP());
            ps.setString(4, sp.getMaDM().getMaDM());
            ps.setString(5, sp.getDonViTinh());
            ps.setInt(6, sp.getTonKho());
            ps.setBoolean(7, sp.isTrangThai());
            ps.setTimestamp(8, Timestamp.valueOf(sp.getNgayTao()));
            ps.setTimestamp(9, Timestamp.valueOf(sp.getNgayCapNhat()));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatSanPham(SanPham sp) {
        String sql = "UPDATE SANPHAM SET tenSP = ?, anhSP = ?, maDM = ?, donViTinh = ?, tonKho = ?, trangThai = ?, ngayCapNhat = ? WHERE maSP = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sp.getTenSP());
            ps.setString(2, sp.getAnhSP());
            ps.setString(3, sp.getMaDM().getMaDM());
            ps.setString(4, sp.getDonViTinh());
            ps.setInt(5, sp.getTonKho());
            ps.setBoolean(6, sp.isTrangThai());
            ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(8, sp.getMaSP());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<SanPham> layTatCaSanPham() {
        List<SanPham> ds = new ArrayList<>();
        String sql = "SELECT * FROM SANPHAM";
        try (Connection conn = DBConnect.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                SanPham sp = new SanPham();
                sp.setMaSP(rs.getString("maSP"));
                sp.setTenSP(rs.getString("tenSP"));
                sp.setAnhSP(rs.getString("anhSP"));
                DanhMuc dm = new DanhMuc();
                dm.setMaDM(rs.getString("maDM"));
                sp.setMaDM(dm);
                sp.setDonViTinh(rs.getString("donViTinh"));
                sp.setTonKho(rs.getInt("tonKho"));
                sp.setTrangThai(rs.getBoolean("trangThai"));
                sp.setNgayTao(rs.getTimestamp("ngayTao").toLocalDateTime());
                sp.setNgayCapNhat(rs.getTimestamp("ngayCapNhat").toLocalDateTime());
                ds.add(sp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    public List<SanPham> layTheoDanhMuc(String maDM) {
        List<SanPham> dsSP = new ArrayList<>();
        String sql = "SELECT * FROM SANPHAM WHERE maDM = ? AND trangThai = 1";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maDM);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SanPham sp = new SanPham();
                    sp.setMaSP(rs.getString("maSP"));
                    sp.setTenSP(rs.getString("tenSP"));
                    sp.setAnhSP(rs.getString("anhSP"));
                    DanhMuc dm = new DanhMuc();
                    dm.setMaDM(rs.getString("maDM"));
                    sp.setMaDM(dm);
                    sp.setDonViTinh(rs.getString("donViTinh"));
                    sp.setTonKho(rs.getInt("tonKho"));
                    sp.setTrangThai(rs.getBoolean("trangThai"));
                    sp.setNgayTao(rs.getTimestamp("ngayTao").toLocalDateTime());
                    sp.setNgayCapNhat(rs.getTimestamp("ngayCapNhat").toLocalDateTime());
                    dsSP.add(sp);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsSP;
    }

    public SanPham timTheoMa(String maSP) {
        String sql = "SELECT * FROM SANPHAM WHERE maSP = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maSP);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    SanPham sp = new SanPham();
                    sp.setMaSP(rs.getString("maSP"));
                    sp.setTenSP(rs.getString("tenSP"));
                    sp.setAnhSP(rs.getString("anhSP"));
                    DanhMuc dm = new DanhMuc();
                    dm.setMaDM(rs.getString("maDM"));
                    sp.setMaDM(dm);
                    sp.setDonViTinh(rs.getString("donViTinh"));
                    sp.setTonKho(rs.getInt("tonKho"));
                    sp.setTrangThai(rs.getBoolean("trangThai"));
                    sp.setNgayTao(rs.getTimestamp("ngayTao").toLocalDateTime());
                    sp.setNgayCapNhat(rs.getTimestamp("ngayCapNhat").toLocalDateTime());
                    return sp;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean capNhatTonKho(String maSP, int soLuong) {
        String sql = "UPDATE SANPHAM SET tonKho = tonKho + ? WHERE maSP = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, soLuong);
            ps.setString(2, maSP);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}