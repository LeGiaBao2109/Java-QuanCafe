package com.quanlycafe.dao;

import com.quanlycafe.entity.DanhMuc;
import com.quanlycafe.entity.SanPham;
import com.quanlycafe.util.DBConnect;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SanPhamDAO {

    // Thêm sản phẩm mới (Dành cho quản lý)
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

    // Cập nhật thông tin sản phẩm
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

    // Lấy tất cả sản phẩm
    public List<SanPham> layTatCaSanPham() {
        List<SanPham> ds = new ArrayList<>();
        String sql = "SELECT * FROM SANPHAM";
        try (Connection conn = DBConnect.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                ds.add(mapResultSetToSanPham(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    // Nghiệp vụ POS: Lọc sản phẩm theo danh mục (Ví dụ: Trà sữa, Cà phê)
    public List<SanPham> layTheoDanhMuc(String maDM) {
        List<SanPham> ds = new ArrayList<>();
        String sql = "SELECT * FROM SANPHAM WHERE maDM = ? AND trangThai = 1"; // Chỉ lấy món đang kinh doanh
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maDM);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ds.add(mapResultSetToSanPham(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    // Tìm sản phẩm theo mã
    public SanPham timTheoMa(String maSP) {
        String sql = "SELECT * FROM SANPHAM WHERE maSP = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maSP);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToSanPham(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Cập nhật tồn kho (Dùng khi thanh toán hoặc nhập hàng)
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

    // Helper method để tránh lặp code[cite: 3]
    private SanPham mapResultSetToSanPham(ResultSet rs) throws SQLException {
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

    public static void main(String[] args) {
        SanPhamDAO dao = new SanPhamDAO();
        System.out.println("--- KIỂM TRA DANH SÁCH SẢN PHẨM ---");
        List<SanPham> ds = dao.layTatCaSanPham();
        if (ds.isEmpty()) {
            System.out.println("Không có sản phẩm nào.");
        } else {
            for (SanPham sp : ds) {
                String status = sp.isTrangThai() ? "Đang bán" : "Ngừng bán";
                System.out.println(sp.getMaSP() + " | " + sp.getTenSP() + " | Kho: " + sp.getTonKho() + " | " + status);
            }
        }
    }
}