package com.quanlycafe.entity;

import java.util.Objects;

public class ChiTietHoaDonTopping {
    private ChiTietHoaDon chiTietHoaDon;
    private Topping maTopping;
    private int soLuong;
    private double giaBan;
    private double thanhTien;

    public ChiTietHoaDonTopping() {
    }

    public ChiTietHoaDonTopping(ChiTietHoaDon chiTietHoaDon, Topping maTopping, int soLuong, double giaBan, double thanhTien) {
        this.chiTietHoaDon = chiTietHoaDon;
        this.maTopping = maTopping;
        this.soLuong = soLuong;
        this.giaBan = giaBan;
        this.thanhTien = thanhTien;
    }

    public ChiTietHoaDon getChiTietHoaDon() {
        return chiTietHoaDon;
    }

    public void setChiTietHoaDon(ChiTietHoaDon chiTietHoaDon) {
        this.chiTietHoaDon = chiTietHoaDon;
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
    }

    public double getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(double giaBan) {
        this.giaBan = giaBan;
    }

    public double getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(double thanhTien) {
        this.thanhTien = thanhTien;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ChiTietHoaDonTopping that = (ChiTietHoaDonTopping) o;
        return Objects.equals(chiTietHoaDon, that.chiTietHoaDon) && Objects.equals(maTopping, that.maTopping);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chiTietHoaDon, maTopping);
    }

    @Override
    public String toString() {
        return "ChiTietHoaDonTopping{" +
                "chiTietHoaDon=" + chiTietHoaDon +
                ", maTopping=" + maTopping +
                ", soLuong=" + soLuong +
                ", giaBan=" + giaBan +
                ", thanhTien=" + thanhTien +
                '}';
    }
}
