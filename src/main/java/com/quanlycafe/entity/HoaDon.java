package com.quanlycafe.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class HoaDon {
    private String maHD;
    private LocalDateTime ngayTao;
    private NhanVien maNV;
    private KhachHang maKH;
    private Voucher voucher;
    private String phuongThucTT;
    private double tongTien;

    public HoaDon() {
    }

    public HoaDon(String maHD, LocalDateTime ngayTao, NhanVien maNV, KhachHang maKH, Voucher voucher, String phuongThucTT, double tongTien) {
        this.maHD = maHD;
        this.ngayTao = ngayTao;
        this.maNV = maNV;
        this.maKH = maKH;
        this.voucher = voucher;
        this.phuongThucTT = phuongThucTT;
        this.tongTien = tongTien;
    }

    public String getMaHD() {
        return maHD;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public LocalDateTime getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(LocalDateTime ngayTao) {
        this.ngayTao = ngayTao;
    }

    public NhanVien getMaNV() {
        return maNV;
    }

    public void setMaNV(NhanVien maNV) {
        this.maNV = maNV;
    }

    public KhachHang getMaKH() {
        return maKH;
    }

    public void setMaKH(KhachHang maKH) {
        this.maKH = maKH;
    }

    public Voucher getVoucher() {
        return voucher;
    }

    public void setVoucher(Voucher voucher) {
        this.voucher = voucher;
    }

    public String getPhuongThucTT() {
        return phuongThucTT;
    }

    public void setPhuongThucTT(String phuongThucTT) {
        this.phuongThucTT = phuongThucTT;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        HoaDon hoaDon = (HoaDon) o;
        return Objects.equals(maHD, hoaDon.maHD);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(maHD);
    }

    @Override
    public String toString() {
        return "HoaDon{" +
                "maHD='" + maHD + '\'' +
                ", ngayTao=" + ngayTao +
                ", maNV=" + maNV +
                ", maKH=" + maKH +
                ", voucher=" + voucher +
                ", phuongThucTT='" + phuongThucTT + '\'' +
                ", tongTien=" + tongTien +
                '}';
    }
}
