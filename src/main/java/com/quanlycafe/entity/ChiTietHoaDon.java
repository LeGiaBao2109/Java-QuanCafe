package com.quanlycafe.entity;

import java.util.Objects;

public class ChiTietHoaDon {
    private HoaDon maHD;
    private KichCo maSize;
    private int soLuong;
    private double thanhTien;
    private String ghiChu;

    public ChiTietHoaDon() {
    }

    public ChiTietHoaDon(HoaDon maHD, KichCo maSize, int soLuong, double thanhTien, String ghiChu) {
        this.maHD = maHD;
        this.maSize = maSize;
        this.soLuong = soLuong;
        this.thanhTien = thanhTien;
        this.ghiChu = ghiChu;
    }

    public HoaDon getMaHD() {
        return maHD;
    }

    public void setMaHD(HoaDon maHD) {
        this.maHD = maHD;
    }

    public KichCo getMaSize() {
        return maSize;
    }

    public void setMaSize(KichCo maSize) {
        this.maSize = maSize;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(double thanhTien) {
        this.thanhTien = thanhTien;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ChiTietHoaDon that = (ChiTietHoaDon) o;
        return Objects.equals(maHD, that.maHD) && Objects.equals(maSize, that.maSize);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maHD, maSize);
    }

    @Override
    public String toString() {
        return "ChiTietHoaDon{" +
                "maHD=" + maHD +
                ", maSize=" + maSize +
                ", soLuong=" + soLuong +
                ", thanhTien=" + thanhTien +
                ", ghiChu='" + ghiChu + '\'' +
                '}';
    }
}
