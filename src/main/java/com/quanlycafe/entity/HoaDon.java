package com.quanlycafe.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class HoaDon {
    private String maHD;
    private DonHang maDH;
    private Voucher maVoucher;
    private LocalDateTime ngayThanhToan;
    private PhuongThucTT phuongThucTT;
    private double tongTienCuoi;

    public HoaDon() {
    }

    public HoaDon(String maHD, DonHang maDH, Voucher maVoucher, LocalDateTime ngayThanhToan, PhuongThucTT phuongThucTT, double tongTienCuoi) {
        this.maHD = maHD;
        this.maDH = maDH;
        this.maVoucher = maVoucher;
        this.ngayThanhToan = ngayThanhToan;
        this.phuongThucTT = phuongThucTT;
        this.tongTienCuoi = tongTienCuoi;
    }

    public String getMaHD() {
        return maHD;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public DonHang getMaDH() {
        return maDH;
    }

    public void setMaDH(DonHang maDH) {
        this.maDH = maDH;
    }

    public Voucher getMaVoucher() {
        return maVoucher;
    }

    public void setMaVoucher(Voucher maVoucher) {
        this.maVoucher = maVoucher;
    }

    public LocalDateTime getNgayThanhToan() {
        return ngayThanhToan;
    }

    public void setNgayThanhToan(LocalDateTime ngayThanhToan) {
        this.ngayThanhToan = ngayThanhToan;
    }

    public PhuongThucTT getPhuongThucTT() {
        return phuongThucTT;
    }

    public void setPhuongThucTT(PhuongThucTT phuongThucTT) {
        this.phuongThucTT = phuongThucTT;
    }

    public double getTongTienCuoi() {
        return tongTienCuoi;
    }

    public void setTongTienCuoi(double tongTienCuoi) {
        this.tongTienCuoi = tongTienCuoi;
    }

    public void apDungVoucher(Voucher v) {
        this.maVoucher = v;
    }

    public void tinhTongTien(double tongTienHang, double thueSuat) {
        double tienThue = tongTienHang * thueSuat;
        double giamGia = (this.maVoucher != null) ? this.maVoucher.getGiaTriGiam() : 0;
        this.tongTienCuoi = (tongTienHang + tienThue) - giamGia;
        if (this.tongTienCuoi < 0) this.tongTienCuoi = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HoaDon hoaDon = (HoaDon) o;
        return Objects.equals(maHD, hoaDon.maHD);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maHD);
    }

    @Override
    public String toString() {
        return "HoaDon{" +
                "maHD='" + maHD + '\'' +
                ", maDH=" + maDH +
                ", maVoucher=" + maVoucher +
                ", ngayThanhToan=" + ngayThanhToan +
                ", phuongThucTT=" + phuongThucTT +
                ", tongTienCuoi=" + tongTienCuoi +
                '}';
    }
}