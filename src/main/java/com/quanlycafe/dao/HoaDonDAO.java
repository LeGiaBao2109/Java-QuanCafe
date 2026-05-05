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

    public boolean timTheoID(String maHD) {
        String sql = "SELECT COUNT(*) FROM HOADON WHERE maHD = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHD);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<HoaDon> layTatCaHoaDon() {
        List<HoaDon> dsHD = new ArrayList<>();
        String sql = "SELECT h.maHD, h.ngayThanhToan, h.phuongThucTT, h.tongTienCuoi, h.maVoucher, " +
                "d.maDH, d.stt, d.maNV, d.maKH, " +
                "n.tenNV, k.tenKH, " +
                "v.maCT, v.tenCT, v.loaiGiamGia, v.giaGiam " +
                "FROM HOADON h " +
                "INNER JOIN DONHANG d ON h.maDH = d.maDH " +
                "LEFT JOIN NHANVIEN n ON d.maNV = n.maNV " +
                "LEFT JOIN KHACHHANG k ON d.maKH = k.maKH " +
                "LEFT JOIN VOUCHER v ON h.maVoucher = v.maCT " +
                "ORDER BY h.ngayThanhToan DESC";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                NhanVien nv = new NhanVien();
                nv.setMaNV(rs.getString("maNV"));
                nv.setTenNV(rs.getString("tenNV"));

                KhachHang kh = new KhachHang();
                kh.setMaKH(rs.getString("maKH"));
                kh.setTenKH(rs.getString("tenKH"));

                DonHang dh = new DonHang();
                dh.setMaDH(rs.getString("maDH"));
                dh.setStt(rs.getInt("stt"));
                dh.setMaNV(nv);
                dh.setMaKH(kh);

                Voucher v = null;
                String maVoucherDB = rs.getString("maVoucher");
                if (maVoucherDB != null) {
                    v = new Voucher();
                    v.setMaCT(rs.getString("maCT"));
                    v.setTenCT(rs.getString("tenCT"));

                    String loaiGiamStr = rs.getString("loaiGiamGia");
                    if (loaiGiamStr != null) {
                        try {
                            v.setLoaiGiamGia(LoaiGiamGia.valueOf(loaiGiamStr));
                        } catch (IllegalArgumentException e) {
                            v.setLoaiGiamGia(LoaiGiamGia.KHONG_GIAM);
                        }
                    }
                }

                HoaDon hd = new HoaDon();
                hd.setMaHD(rs.getString("maHD"));
                hd.setMaDH(dh);
                hd.setMaVoucher(v);

                Timestamp ts = rs.getTimestamp("ngayThanhToan");
                if (ts != null) {
                    hd.setNgayThanhToan(ts.toLocalDateTime());
                }

                String ptth = rs.getString("phuongThucTT");
                if (ptth != null) {
                    hd.setPhuongThucTT(PhuongThucTT.valueOf(ptth));
                }

                hd.setTongTienCuoi(rs.getDouble("tongTienCuoi"));
                dsHD.add(hd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsHD;
    }

    public List<HoaDon> timTheoNgay(LocalDateTime ngayBatDau, LocalDateTime ngayKetThuc) {
        List<HoaDon> dsHD = new ArrayList<>();
        String sql = "SELECT * FROM HOADON WHERE ngayThanhToan BETWEEN ? AND ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(ngayBatDau));
            ps.setTimestamp(2, Timestamp.valueOf(ngayKetThuc));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DonHang dh = new DonHang();
                    dh.setMaDH(rs.getString("maDH"));

                    Voucher v = null;
                    String maCT = rs.getString("maVoucher");
                    if (maCT != null) {
                        v = new Voucher();
                        v.setMaCT(maCT);
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsHD;
    }

    public double tinhDoanhThu(LocalDateTime ngayBatDau, LocalDateTime ngayKetThuc) {
        String sql = "SELECT SUM(tongTienCuoi) FROM HOADON WHERE ngayThanhToan BETWEEN ? AND ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(ngayBatDau));
            ps.setTimestamp(2, Timestamp.valueOf(ngayKetThuc));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}