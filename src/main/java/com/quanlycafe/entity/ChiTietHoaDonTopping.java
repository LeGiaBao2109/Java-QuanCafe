package com.quanlycafe.entity;

import java.util.Objects;

public class ChiTietHoaDonTopping {
    private ChiTietHoaDon maCTHD;
    private Topping maTopping;
    private int soLuong;
    private double giaBan;
    private double thanhTien;

    public ChiTietHoaDonTopping() {
    }

    public ChiTietHoaDonTopping(ChiTietHoaDon maCTHD, Topping maTopping, int soLuong, double giaBan) {
        this.maCTHD = maCTHD;
        this.maTopping = maTopping;
        this.soLuong = soLuong;
        this.giaBan = giaBan;
        tinhThanhTien();
    }

    private void tinhThanhTien() {
        this.thanhTien = this.soLuong * this.giaBan;
    }

    public ChiTietHoaDon getMaCTHD() {
        return maCTHD;
    }

    public void setMaCTHD(ChiTietHoaDon maCTHD) {
        this.maCTHD = maCTHD;
    }

    public Topping getMaTopping() {
        return maTopping;
    }

    public void setMaTopping(Topping maTopping) {
        this.maTopping = maTopping;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
        tinhThanhTien();
    }

    public double getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(double giaBan) {
        this.giaBan = giaBan;
        tinhThanhTien();
    }

    public double getThanhTien() {
        return thanhTien;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChiTietHoaDonTopping that = (ChiTietHoaDonTopping) o;
        return Objects.equals(maCTHD, that.maCTHD) && Objects.equals(maTopping, that.maTopping);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maCTHD, maTopping);
    }

    @Override
    public String toString() {
        return "ChiTietHoaDonTopping{" +
                "maCTHD=" + maCTHD +
                ", maTopping=" + maTopping +
                ", soLuong=" + soLuong +
                ", giaBan=" + giaBan +
                ", thanhTien=" + thanhTien +
                '}';
    }
}