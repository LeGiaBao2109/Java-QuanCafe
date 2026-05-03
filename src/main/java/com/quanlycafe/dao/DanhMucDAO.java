package com.quanlycafe.dao;

import com.quanlycafe.entity.DanhMuc;
import com.quanlycafe.util.DBConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DanhMucDAO {
    public List<DanhMuc> layTatCaDanhMuc() {
        List<DanhMuc> dsDM = new ArrayList<>();
        String sql = "SELECT * FROM DANHMUC";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                DanhMuc dm = new DanhMuc(
                        rs.getString("maDM"),
                        rs.getString("tenDM")
                );
                dsDM.add(dm);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsDM;
    }

    public DanhMuc timTheoMa(String maDM) {
        String sql = "SELECT * FROM DANHMUC WHERE maDM = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maDM);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new DanhMuc(rs.getString("maDM"), rs.getString("tenDM"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<DanhMuc> timTheoTen(String tenDM) {
        List<DanhMuc> dsDM = new ArrayList<>();
        String sql = "SELECT * FROM DANHMUC WHERE tenDM LIKE ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + tenDM + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    dsDM.add(new DanhMuc(rs.getString("maDM"), rs.getString("tenDM")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsDM;
    }

    public boolean themDanhMuc(DanhMuc dm) {
        String sql = "INSERT INTO DANHMUC (maDM, tenDM) VALUES (?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dm.getMaDM());
            ps.setString(2, dm.getTenDM());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatDanhMuc(DanhMuc dm) {
        String sql = "UPDATE DANHMUC SET tenDM = ? WHERE maDM = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dm.getTenDM());
            ps.setString(2, dm.getMaDM());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}