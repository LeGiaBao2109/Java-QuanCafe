package com.quanlycafe.dao;

import com.quanlycafe.entity.*;
import com.quanlycafe.util.DBConnect;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HoaDonDAO {
    public boolean taoHoaDon(HoaDon hd) {
        String sql = "INSERT INTO HOADON (maHD, maDH, maVoucher, ngayThanhToan, phuongThucTT, tongTienCuoi) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hd.getMaHD());
            ps.setString(2, hd.getMaDH().getMaDH());
            if (hd.getMaVoucher() != null) {
                ps.setString(3, hd.getMaVoucher().getMaCT());
            } else {
                ps.setNull(3, Types.VARCHAR);
            }
            ps.setTimestamp(4, Timestamp.valueOf(hd.getNgayThanhToan()));
            ps.setString(5, hd.getPhuongThucTT().name());
            ps.setDouble(6, hd.getTongTienCuoi());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<HoaDon> layTatCaHoaDon() {
        List<HoaDon> dsHD = new ArrayList<>();
        String sql = "SELECT h.*, d.maNV, d.maKH, d.stt, n.tenNV, k.tenKH, v.tenCT, v.giaGiam, v.loaiGiamGia " +
                "FROM HOADON h " +
                "LEFT JOIN DONHANG d ON h.maDH = d.maDH " +
                "LEFT JOIN NHANVIEN n ON d.maNV = n.maNV " +
                "LEFT JOIN KHACHHANG k ON d.maKH = k.maKH " +
                "LEFT JOIN VOUCHER v ON h.maVoucher = v.maCT " +
                "ORDER BY h.ngayThanhToan DESC";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                DonHang dh = new DonHang();
                dh.setMaDH(rs.getString("maDH"));

                KhachHang kh = new KhachHang();
                kh.setTenKH(rs.getString("tenKH"));
                dh.setMaKH(kh);

                Voucher v = null;
                if (rs.getString("maVoucher") != null) {
                    v = new Voucher();
                    v.setMaCT(rs.getString("maVoucher"));
                    v.setTenCT(rs.getString("tenCT"));
                }

                HoaDon hd = new HoaDon();
                hd.setMaHD(rs.getString("maHD"));
                hd.setMaDH(dh);
                hd.setMaVoucher(v);
                hd.setNgayThanhToan(rs.getTimestamp("ngayThanhToan").toLocalDateTime());
                hd.setPhuongThucTT(PhuongThucTT.valueOf(rs.getString("phuongThucTT")));
                hd.setTongTienCuoi(rs.getDouble("tongTienCuoi"));

                dsHD.add(hd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsHD;
    }

    public HoaDon timHoaDonTheoMa(String maHD) {
        String sql = "SELECT h.*, d.maNV, d.maKH, d.stt, n.tenNV, k.tenKH, v.tenCT " +
                "FROM HOADON h " +
                "LEFT JOIN DONHANG d ON h.maDH = d.maDH " +
                "LEFT JOIN NHANVIEN n ON d.maNV = n.maNV " +
                "LEFT JOIN KHACHHANG k ON d.maKH = k.maKH " +
                "LEFT JOIN VOUCHER v ON h.maVoucher = v.maCT " +
                "WHERE h.maHD = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHD);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    DonHang dh = new DonHang();
                    dh.setMaDH(rs.getString("maDH"));

                    KhachHang kh = new KhachHang();
                    kh.setTenKH(rs.getString("tenKH"));
                    dh.setMaKH(kh);

                    Voucher v = null;
                    if (rs.getString("maVoucher") != null) {
                        v = new Voucher();
                        v.setMaCT(rs.getString("maVoucher"));
                        v.setTenCT(rs.getString("tenCT"));
                    }

                    HoaDon hd = new HoaDon();
                    hd.setMaHD(rs.getString("maHD"));
                    hd.setMaDH(dh);
                    hd.setMaVoucher(v);
                    hd.setNgayThanhToan(rs.getTimestamp("ngayThanhToan").toLocalDateTime());
                    hd.setPhuongThucTT(PhuongThucTT.valueOf(rs.getString("phuongThucTT")));
                    hd.setTongTienCuoi(rs.getDouble("tongTienCuoi"));
                    return hd;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}