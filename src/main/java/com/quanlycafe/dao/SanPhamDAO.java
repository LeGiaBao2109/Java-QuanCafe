package com.quanlycafe.dao;

import com.quanlycafe.entity.DanhMuc;
import com.quanlycafe.entity.SanPham;
import com.quanlycafe.util.DBConnect;

import java.sql.*;
import java.text.DecimalFormat;
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

    public List<Object[]> layDanhSachSanPhamQuanLy() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT dm.tenDM, sp.maSP, sp.tenSP, k.tenSize, sp.donViTinh, k.gia " +
                     "FROM SANPHAM sp " +
                     "JOIN DANHMUC dm ON sp.maDM = dm.maDM " +
                     "JOIN KICHCO k ON sp.maSP = k.maSP " +
                     "WHERE sp.trangThai = 1";
                     
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
             
            DecimalFormat formatter = new DecimalFormat("#,### VND");
            while (rs.next()) {
                String loaiMon = "Đồ uống";
                String maSP = rs.getString("maSP");
                String tenSP = rs.getString("tenSP") + " (" + rs.getString("tenSize") + ")"; 
                String nhom = rs.getString("tenDM");
                String dvt = rs.getString("donViTinh");
                double gia = rs.getDouble("gia");
                
                list.add(new Object[]{loaiMon, maSP, tenSP, nhom, dvt, formatter.format(gia)});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Object[]> layDanhSachDanhMuc() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT maDM, tenDM FROM DANHMUC";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Object[]{rs.getString("maDM"), rs.getString("tenDM")});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Object[]> laySanPhamBanHangPOS(String maDM) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT sp.tenSP, MIN(k.gia) as gia " +
                     "FROM SANPHAM sp " +
                     "JOIN KICHCO k ON sp.maSP = k.maSP " +
                     "WHERE sp.maDM = ? AND sp.trangThai = 1 " +
                     "GROUP BY sp.maSP, sp.tenSP";
                     
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setString(1, maDM);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Object[]{rs.getString("tenSP"), rs.getInt("gia")});
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}