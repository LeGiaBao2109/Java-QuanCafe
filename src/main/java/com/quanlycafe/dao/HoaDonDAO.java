package com.quanlycafe.dao;

import com.quanlycafe.entity.DonHang;
import com.quanlycafe.entity.HoaDon;
import com.quanlycafe.entity.PhuongThucTT;
import com.quanlycafe.entity.Voucher;
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
        String sql = "SELECT * FROM HOADON";
        try (Connection conn = DBConnect.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

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

    public static void main(String[] args) {
        HoaDonDAO dao = new HoaDonDAO();
        System.out.println("--- TEST HOADONDAO ---");
        List<HoaDon> ds = dao.layTatCaHoaDon();
        if (ds.isEmpty()) {
            System.out.println("Danh sách trống!");
        } else {
            for (HoaDon hd : ds) {
                System.out.println("Mã HD: " + hd.getMaHD() + " | Tiền: " + hd.getTongTienCuoi());
            }
        }
        // Test doanh thu hôm nay
        LocalDateTime bd = LocalDateTime.now().withHour(0).withMinute(0);
        LocalDateTime kt = LocalDateTime.now().withHour(23).withMinute(59);
        System.out.println("Doanh thu hôm nay: " + dao.tinhDoanhThu(bd, kt));
    }
}