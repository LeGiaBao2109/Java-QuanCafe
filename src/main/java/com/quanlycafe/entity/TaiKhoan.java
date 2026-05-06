package com.quanlycafe.entity;

import java.util.Objects;

public class TaiKhoan {
    private String maTK;
    private String tenDangNhap;
    private String matKhau;
    private NhanVien maNV;

    public TaiKhoan() {
    }

    public TaiKhoan(String maTK, String tenDangNhap, String matKhau, NhanVien maNV) {
        setMaTK(maTK);
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.maNV = maNV;
    }

    public String getMaTK() {
        return maTK;
    }

    public void setMaTK(String maTK) {
        if(!maTK.matches("TK\\d{2}"))
            throw new IllegalArgumentException("Mã tài khoản bắt đầu TK và có 2 chữ số, vd: TK01");
        this.maTK = maTK;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
    	
        this.matKhau = matKhau;
    }

    public NhanVien getMaNV() {
        return maNV;
    }

    public void setMaNV(NhanVien maNV) {
        this.maNV = maNV;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TaiKhoan taiKhoan = (TaiKhoan) o;
        return Objects.equals(maTK, taiKhoan.maTK);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(maTK);
    }

    @Override
    public String toString() {
        return "TaiKhoan{" +
                "maTK='" + maTK + '\'' +
                ", tenDangNhap='" + tenDangNhap + '\'' +
                ", matKhau='" + matKhau + '\'' +
                ", maNV=" + maNV +
                '}';
    }
}
