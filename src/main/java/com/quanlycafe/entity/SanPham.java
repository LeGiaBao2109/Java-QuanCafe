package com.quanlycafe.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class SanPham {
    private String maSP;
    private String tenSP;
    private String anhSP;
    private DanhMuc maDM;
    private String donViTinh;
    private int tonKho;
    private boolean trangThai;
    private LocalDateTime ngayTao;

    public SanPham() {
    }

    public SanPham(String maSP, String tenSP, String anhSP, DanhMuc maDM, String donViTinh, int tonKho, boolean trangThai, LocalDateTime ngayTao) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.anhSP = anhSP;
        this.maDM = maDM;
        this.donViTinh = donViTinh;
        this.tonKho = tonKho;
        this.trangThai = trangThai;
        this.ngayTao = ngayTao;
    }

    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public String getAnhSP() {
        return anhSP;
    }

    public void setAnhSP(String anhSP) {
        this.anhSP = anhSP;
    }

    public DanhMuc getMaDM() {
        return maDM;
    }

    public void setMaDM(DanhMuc maDM) {
        this.maDM = maDM;
    }

    public String getDonViTinh() {
        return donViTinh;
    }

    public void setDonViTinh(String donViTinh) {
        this.donViTinh = donViTinh;
    }

    public int getTonKho() {
        return tonKho;
    }

    public void setTonKho(int tonKho) {
        this.tonKho = tonKho;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    public LocalDateTime getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(LocalDateTime ngayTao) {
        this.ngayTao = ngayTao;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SanPham sanPham = (SanPham) o;
        return Objects.equals(maSP, sanPham.maSP);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(maSP);
    }

    @Override
    public String toString() {
        return "SanPham{" +
                "maSP='" + maSP + '\'' +
                ", tenSP='" + tenSP + '\'' +
                ", anhSP='" + anhSP + '\'' +
                ", maDM=" + maDM +
                ", donViTinh='" + donViTinh + '\'' +
                ", tonKho=" + tonKho +
                ", trangThai=" + trangThai +
                ", ngayTao=" + ngayTao +
                '}';
    }
}
