package com.quanlycafe.entity;

import java.util.Objects;

public class ChiTietPhieuNhap {
    private PhieuNhap maPhieu;
    private SanPham maSP;
    private int soLuong;
    private double donGiaNhap;
    private double thanhTien;

    public ChiTietPhieuNhap() {
    }

    public ChiTietPhieuNhap(PhieuNhap maPhieu, SanPham maSP, int soLuong, double donGiaNhap, double thanhTien) {
        this.maPhieu = maPhieu;
        this.maSP = maSP;
        this.soLuong = soLuong;
        this.donGiaNhap = donGiaNhap;
        this.thanhTien = thanhTien;
    }

    public PhieuNhap getMaPhieu() {
        return maPhieu;
    }

    public void setMaPhieu(PhieuNhap maPhieu) {
        this.maPhieu = maPhieu;
    }

    public SanPham getMaSP() {
        return maSP;
    }

    public void setMaSP(SanPham maSP) {
        this.maSP = maSP;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getDonGiaNhap() {
        return donGiaNhap;
    }

    public void setDonGiaNhap(double donGiaNhap) {
        this.donGiaNhap = donGiaNhap;
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
        ChiTietPhieuNhap that = (ChiTietPhieuNhap) o;
        return Objects.equals(maPhieu, that.maPhieu) && Objects.equals(maSP, that.maSP);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maPhieu, maSP);
    }

    @Override
    public String toString() {
        return "ChiTietPhieuNhap{" +
                "maPhieu=" + maPhieu +
                ", maSP=" + maSP +
                ", soLuong=" + soLuong +
                ", donGiaNhap=" + donGiaNhap +
                ", thanhTien=" + thanhTien +
                '}';
    }
}
