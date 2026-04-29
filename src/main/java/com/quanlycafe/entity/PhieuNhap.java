package com.quanlycafe.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class PhieuNhap {
    private String maPhieu;
    private String tenNCC;
    private LocalDateTime ngayNhap;
    private double tongTienNhap;

    public PhieuNhap() {
    }

    public PhieuNhap(LocalDateTime ngayNhap, String maPhieu, String tenNCC, double tongTienNhap) {
        this.ngayNhap = ngayNhap;
        this.maPhieu = maPhieu;
        this.tenNCC = tenNCC;
        this.tongTienNhap = tongTienNhap;
    }

    public String getMaPhieu() {
        return maPhieu;
    }

    public void setMaPhieu(String maPhieu) {
        this.maPhieu = maPhieu;
    }

    public String getTenNCC() {
        return tenNCC;
    }

    public void setTenNCC(String tenNCC) {
        this.tenNCC = tenNCC;
    }

    public LocalDateTime getNgayNhap() {
        return ngayNhap;
    }

    public void setNgayNhap(LocalDateTime ngayNhap) {
        this.ngayNhap = ngayNhap;
    }

    public double getTongTienNhap() {
        return tongTienNhap;
    }

    public void setTongTienNhap(double tongTienNhap) {
        this.tongTienNhap = tongTienNhap;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PhieuNhap phieuNhap = (PhieuNhap) o;
        return Objects.equals(maPhieu, phieuNhap.maPhieu);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(maPhieu);
    }

    @Override
    public String toString() {
        return "PhieuNhap{" +
                "maPhieu='" + maPhieu + '\'' +
                ", tenNCC='" + tenNCC + '\'' +
                ", ngayNhap=" + ngayNhap +
                ", tongTienNhap=" + tongTienNhap +
                '}';
    }
}
