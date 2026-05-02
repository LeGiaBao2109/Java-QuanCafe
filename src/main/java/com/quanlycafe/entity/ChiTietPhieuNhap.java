package com.quanlycafe.entity;

import java.util.Objects;

public class ChiTietPhieuNhap {
    private PhieuNhap maPhieu;
    private SanPham maSP;
    private int soLuong;
    private double donGiaNhap;
    private double thanhTien; // Thuộc tính dẫn xuất

    public ChiTietPhieuNhap() {
    }

    // Constructor 4 tham số để DAO gọi được, thanhTien sẽ tự tính
    public ChiTietPhieuNhap(PhieuNhap maPhieu, SanPham maSP, int soLuong, double donGiaNhap) {
        this.maPhieu = maPhieu;
        this.maSP = maSP;
        this.soLuong = soLuong;
        this.donGiaNhap = donGiaNhap;
        tinhThanhTien(); // Tự tính khi khởi tạo
    }

    // Hàm tính toán dùng chung
    private void tinhThanhTien() {
        this.thanhTien = this.soLuong * this.donGiaNhap;
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

    // Cập nhật lại thanhTien mỗi khi thay đổi số lượng[cite: 5]
    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
        tinhThanhTien();
    }

    public double getDonGiaNhap() {
        return donGiaNhap;
    }

    // Cập nhật lại thanhTien mỗi khi thay đổi đơn giá[cite: 5]
    public void setDonGiaNhap(double donGiaNhap) {
        this.donGiaNhap = donGiaNhap;
        tinhThanhTien();
    }

    public double getThanhTien() {
        return thanhTien;
    }

    // Không nên có setThanhTien vì nó được tính từ cột khác[cite: 5]

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