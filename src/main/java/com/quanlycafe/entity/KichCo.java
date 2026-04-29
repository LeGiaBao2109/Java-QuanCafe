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
        this.maSize = maSize;
        this.maSP = maSP;
        this.tenSize = tenSize;
        this.gia = gia;
    }

    public String getMaSize() {
        return maSize;
    }

    public void setMaSize(String maSize) {
        this.maSize = maSize;
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
        this.tenSize = tenSize;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
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
