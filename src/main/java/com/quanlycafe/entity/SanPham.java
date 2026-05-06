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
    private LocalDateTime ngayCapNhat;

    public SanPham() {
    }

    public SanPham(String maSP, String tenSP, String anhSP, DanhMuc maDM, String donViTinh, int tonKho, boolean trangThai, LocalDateTime ngayTao, LocalDateTime ngayCapNhat) {
        setMaSP(maSP);
        setTenSP(tenSP);
        this.anhSP = anhSP;
        this.maDM = maDM;
        this.donViTinh = donViTinh;
        setTonKho(tonKho);
        this.trangThai = trangThai;
        this.ngayTao = ngayTao;
        this.ngayCapNhat = ngayCapNhat;
    }

    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        if (maSP == null || !maSP.trim().matches("^SP\\d+$")) {
            throw new IllegalArgumentException("Mã sản phẩm không hợp lệ! Phải bắt đầu bằng 'SP' theo sau là các chữ số (VD: SP01).");
        }
        this.maSP = maSP.trim();
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        if (tenSP == null || tenSP.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên sản phẩm không được để trống!");
        }
        this.tenSP = tenSP.trim();
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
        if (tonKho < 0) {
            throw new IllegalArgumentException("Tồn kho không được phép nhỏ hơn 0!");
        }
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

    public LocalDateTime getNgayCapNhat() {
        return ngayCapNhat;
    }

    public void setNgayCapNhat(LocalDateTime ngayCapNhat) {
        this.ngayCapNhat = ngayCapNhat;
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
                ", ngayCapNhat=" + ngayCapNhat +
                '}';
    }
}