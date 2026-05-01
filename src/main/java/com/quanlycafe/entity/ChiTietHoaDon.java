package com.quanlycafe.entity;

import java.util.Objects;

public class ChiTietHoaDon {
    private String maCTHD;
    private DonHang maDH;
    private KichCo maSize;
    private int soLuong;
    private double donGia;
    private String ghiChu;
    private double thanhTien;
    private MucDa luongDa;
    private MucDuong luongDuong;

    public ChiTietHoaDon() {
    }

    public ChiTietHoaDon(String maCTHD, DonHang maDH, KichCo maSize, int soLuong, double donGia, String ghiChu, double thanhTien, MucDa luongDa, MucDuong luongDuong) {
        this.maCTHD = maCTHD;
        this.maDH = maDH;
        this.maSize = maSize;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.ghiChu = ghiChu;
        this.thanhTien = thanhTien;
        this.luongDa = luongDa;
        this.luongDuong = luongDuong;
    }

    public String getMaCTHD() {
        return maCTHD;
    }

    public void setMaCTHD(String maCTHD) {
        this.maCTHD = maCTHD;
    }

    public DonHang getMaDH() {
        return maDH;
    }

    public void setMaDH(DonHang maDH) {
        this.maDH = maDH;
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

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public double getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(double thanhTien) {
        this.thanhTien = thanhTien;
    }

    public MucDa getLuongDa() {
        return luongDa;
    }

    public void setLuongDa(MucDa luongDa) {
        this.luongDa = luongDa;
    }

    public MucDuong getLuongDuong() {
        return luongDuong;
    }

    public void setLuongDuong(MucDuong luongDuong) {
        this.luongDuong = luongDuong;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ChiTietHoaDon that = (ChiTietHoaDon) o;
        return Objects.equals(maCTHD, that.maCTHD);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(maCTHD);
    }

    @Override
    public String toString() {
        return "ChiTietHoaDon{" +
                "maCTHD='" + maCTHD + '\'' +
                ", maDH=" + maDH +
                ", maSize=" + maSize +
                ", soLuong=" + soLuong +
                ", donGia=" + donGia +
                ", ghiChu='" + ghiChu + '\'' +
                ", thanhTien=" + thanhTien +
                ", luongDa=" + luongDa +
                ", luongDuong=" + luongDuong +
                '}';
    }
}
