package com.quanlycafe.dao;

import com.quanlycafe.entity.NhanVien;
import com.quanlycafe.entity.RoleNhanVien;
import com.quanlycafe.util.DBConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NhanVienDAO {
    public List<NhanVien> layTatCaNhanVien() {
        List<NhanVien> dsNV = new ArrayList<>();
        String sql = "SELECT * FROM NHANVIEN";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                NhanVien nv = new NhanVien(
                        rs.getString("maNV"),
                        rs.getString("tenNV"),
                        rs.getString("sdt"),
                        RoleNhanVien.valueOf(rs.getString("roleNV")),
                        rs.getBoolean("trangThai")
                );
                dsNV.add(nv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsNV;
    }

    public NhanVien timTheoMa(String maNV) {
        String sql = "SELECT * FROM NHANVIEN WHERE maNV = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maNV);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new NhanVien(
                            rs.getString("maNV"),
                            rs.getString("tenNV"),
                            rs.getString("sdt"),
                            RoleNhanVien.valueOf(rs.getString("roleNV")),
                            rs.getBoolean("trangThai")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean capNhatNhanVien(NhanVien nv) {
        String sql = "UPDATE NHANVIEN SET tenNV = ?, sdt = ?, roleNV = ?, trangThai = ? WHERE maNV = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nv.getTenNV());
            ps.setString(2, nv.getSdt());
            ps.setString(3, nv.getRoleNV().name());
            ps.setBoolean(4, nv.isTrangThai());
            ps.setString(5, nv.getMaNV());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        NhanVienDAO dao = new NhanVienDAO();

        System.out.println("--- KIỂM TRA DANH SÁCH NHÂN VIÊN ---");
        List<NhanVien> dsNV = dao.layTatCaNhanVien();
        if (dsNV.isEmpty()) {
            System.out.println("Không có dữ liệu trong bảng NHANVIEN.");
        } else {
            dsNV.forEach(System.out::println);
        }

        System.out.println("\n--- THỬ TÌM NHÂN VIÊN NV01 ---");
        NhanVien nv = dao.timTheoMa("NV01");
        if (nv != null) {
            System.out.println("Tìm thấy: " + nv.getTenNV() + " [" + nv.getRoleNV().getMoTa() + "]");
        } else {
            System.out.println("Không tìm thấy mã NV01.");
        }
    }
}