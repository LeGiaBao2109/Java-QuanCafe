package com.quanlycafe.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class DonHang {
    private String maDH;
    private int stt;
    private LocalDateTime ngayTao;
    private NhanVien maNV;
    private KhachHang maKH;
    private boolean trangThai;
    private double tongTien;

    public DonHang() {
    }

    public DonHang(String maDH, int stt, LocalDateTime ngayTao, NhanVien maNV, KhachHang maKH, boolean trangThai) {
        this.maDH = maDH;
        this.stt = stt;
        this.ngayTao = ngayTao;
        this.maNV = maNV;
        this.maKH = maKH;
        this.trangThai = trangThai;
        this.tongTien = 0;
    }

    public String getMaDH() {
        return maDH;
    }

    public void setMaDH(String maDH) {
        this.maDH = maDH;
    }

    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
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

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DonHang donHang = (DonHang) o;
        return Objects.equals(maDH, donHang.maDH);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maDH);
    }

    @Override
    public String toString() {
        return "DonHang{" +
                "maDH='" + maDH + '\'' +
                ", stt=" + stt +
                ", ngayTao=" + ngayTao +
                ", maNV=" + maNV +
                ", maKH=" + maKH +
                ", trangThai=" + trangThai +
                ", tongTien=" + tongTien +
                '}';
    }
}