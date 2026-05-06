package com.quanlycafe.dao;

import com.quanlycafe.util.DBConnect;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class ThongKeDAO {
    public double[] getThongKeTongQuan(Date tuNgay, Date denNgay) {
        double[] result = new double[3];
        String sqlBanHang = "SELECT COUNT(maHD) as SoDon, ISNULL(SUM(tongTienCuoi), 0) as DoanhThu FROM HOADON WHERE CAST(ngayThanhToan AS DATE) BETWEEN ? AND ?";
        String sqlNhapKho = "SELECT ISNULL(SUM(tongTienNhap), 0) as ChiPhi FROM PHIEUNHAP WHERE CAST(ngayNhap AS DATE) BETWEEN ? AND ?";

        try (Connection conn = DBConnect.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(sqlBanHang)) {
                ps.setDate(1, new java.sql.Date(tuNgay.getTime()));
                ps.setDate(2, new java.sql.Date(denNgay.getTime()));
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    result[0] = rs.getDouble("SoDon");
                    result[1] = rs.getDouble("DoanhThu");
                }
            }
            try (PreparedStatement ps = conn.prepareStatement(sqlNhapKho)) {
                ps.setDate(1, new java.sql.Date(tuNgay.getTime()));
                ps.setDate(2, new java.sql.Date(denNgay.getTime()));
                ResultSet rs = ps.executeQuery();
                if (rs.next()) result[2] = rs.getDouble("ChiPhi");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public double[] getDoanhThuHomNayVaHomQua() {
        double[] result = new double[2];
        String sqlNay = "SELECT ISNULL(SUM(tongTienCuoi), 0) FROM HOADON WHERE CAST(ngayThanhToan AS DATE) = CAST(GETDATE() AS DATE)";
        String sqlQua = "SELECT ISNULL(SUM(tongTienCuoi), 0) FROM HOADON WHERE CAST(ngayThanhToan AS DATE) = CAST(DATEADD(day, -1, GETDATE()) AS DATE)";
        try (Connection conn = DBConnect.getConnection()) {
            try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sqlNay)) {
                if (rs.next()) result[0] = rs.getDouble(1);
            }
            try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sqlQua)) {
                if (rs.next()) result[1] = rs.getDouble(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public Map<String, Double> getDoanhThuTheoNgay(Date tuNgay, Date denNgay) {
        Map<String, Double> map = new LinkedHashMap<>();
        String sql = "SELECT CAST(ngayThanhToan AS DATE) as Ngay, SUM(tongTienCuoi) as DoanhThu FROM HOADON WHERE CAST(ngayThanhToan AS DATE) BETWEEN ? AND ? GROUP BY CAST(ngayThanhToan AS DATE) ORDER BY Ngay ASC";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(tuNgay.getTime()));
            ps.setDate(2, new java.sql.Date(denNgay.getTime()));
            ResultSet rs = ps.executeQuery();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
            while (rs.next()) map.put(sdf.format(rs.getDate("Ngay")), rs.getDouble("DoanhThu"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public List<Object[]> getTopSanPhamBanChay(Date tuNgay, Date denNgay) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT TOP 5 sp.tenSP, SUM(ct.soLuong) as TongBan FROM CHITIETHOADON ct JOIN KICHCO k ON ct.maSize = k.maSize JOIN SANPHAM sp ON k.maSP = sp.maSP JOIN DONHANG dh ON ct.maDH = dh.maDH JOIN HOADON hd ON dh.maDH = hd.maDH WHERE dh.trangThai = 1 AND CAST(hd.ngayThanhToan AS DATE) BETWEEN ? AND ? GROUP BY sp.tenSP ORDER BY TongBan DESC";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(tuNgay.getTime()));
            ps.setDate(2, new java.sql.Date(denNgay.getTime()));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(new Object[]{rs.getString("tenSP"), rs.getInt("TongBan")});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Object[]> getDoanhThuTheoNhom(Date ngay) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT d.tenDM, SUM(ct.soLuong) as SL, ISNULL(SUM(ct.thanhTien), 0) as Tien FROM CHITIETHOADON ct JOIN KICHCO k ON ct.maSize = k.maSize JOIN SANPHAM sp ON k.maSP = sp.maSP JOIN DANHMUC d ON sp.maDM = d.maDM JOIN DONHANG dh ON ct.maDH = dh.maDH JOIN HOADON hd ON dh.maDH = hd.maDH WHERE CAST(hd.ngayThanhToan AS DATE) = CAST(? AS DATE) GROUP BY d.tenDM ORDER BY Tien DESC";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(ngay.getTime()));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(new Object[]{rs.getString("tenDM"), rs.getInt("SL"), rs.getDouble("Tien")});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Object[]> getThongKeKhungGio(Date ngay) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT DATEPART(HOUR, ngayThanhToan) as KhungGio, COUNT(maHD) as SoBill, ISNULL(SUM(tongTienCuoi), 0) as DoanhThu FROM HOADON WHERE CAST(ngayThanhToan AS DATE) = CAST(? AS DATE) GROUP BY DATEPART(HOUR, ngayThanhToan) ORDER BY KhungGio ASC";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(ngay.getTime()));
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                list.add(new Object[]{rs.getInt("KhungGio"), rs.getInt("SoBill"), rs.getDouble("DoanhThu")});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public double[] getChiSoChotCa(Date ngay) {
        double[] result = new double[3];
        String sqlBill = "SELECT ISNULL(SUM(tongTienCuoi), 0), COUNT(maHD) FROM HOADON WHERE CAST(ngayThanhToan AS DATE) = CAST(? AS DATE)";
        String sqlChuaTT = "SELECT COUNT(maDH) FROM DONHANG WHERE trangThai = 0 AND CAST(ngayTao AS DATE) = CAST(? AS DATE)";
        try (Connection conn = DBConnect.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(sqlBill)) {
                ps.setDate(1, new java.sql.Date(ngay.getTime()));
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    result[0] = rs.getDouble(1);
                    result[1] = rs.getDouble(2);
                }
            }
            try (PreparedStatement ps = conn.prepareStatement(sqlChuaTT)) {
                ps.setDate(1, new java.sql.Date(ngay.getTime()));
                ResultSet rs = ps.executeQuery();
                if (rs.next()) result[2] = rs.getDouble(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}