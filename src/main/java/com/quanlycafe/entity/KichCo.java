package com.quanlycafe.entity;

import java.util.Objects;

public class KichCo {
    private String maSize;
    private SanPham maSP;
    private String tenSize;
    private double gia;

    public KichCo() {
    }

    public KichCo(String maSize, SanPham maSP, String tenSize, double gia) {
        setMaSize(maSize);
        this.maSP = maSP;
        setTenSize(tenSize);
        setGia(gia);
    }

    public String getMaSize() {
        return maSize;
    }

    public void setMaSize(String maSize) {
        if (maSize == null || !maSize.trim().matches("^S\\d+$")) {
            throw new IllegalArgumentException("Mã kích cỡ không hợp lệ! Phải bắt đầu bằng 'S' theo sau là các chữ số (VD: S01).");
        }
        this.maSize = maSize.trim();
    }

    public SanPham getMaSP() {
        return maSP;
    }

    public void setMaSP(SanPham maSP) {
        this.maSP = maSP;
    }

    public String getTenSize() {
        return tenSize;
    }

    public void setTenSize(String tenSize) {
        if (tenSize == null || tenSize.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên kích cỡ (Size) không được để trống!");
        }
        this.tenSize = tenSize.trim();
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        if (gia <= 0) {
            throw new IllegalArgumentException("Giá bán phải lớn hơn 0!");
        }
        this.gia = gia;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        KichCo kichCo = (KichCo) o;
        return Objects.equals(maSize, kichCo.maSize);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(maSize);
    }

    @Override
    public String toString() {
        return "KichCo{" +
                "maSize='" + maSize + '\'' +
                ", maSP=" + maSP +
                ", tenSize='" + tenSize + '\'' +
                ", gia=" + gia +
                '}';
    }
}