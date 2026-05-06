package com.quanlycafe.entity;

import java.util.Objects;

public class NhanVien {
    private String maNV;
    private String tenNV;
    private String sdt;
    private RoleNhanVien roleNV;
    private boolean trangThai;

    public NhanVien() {
        this.trangThai = true;
    }

    public NhanVien(String maNV, String tenNV, String sdt, RoleNhanVien roleNV, boolean trangThai) {
        setMaNV(maNV);
        setTenNV(tenNV);
        setSdt(sdt);
        this.roleNV = roleNV;
        this.trangThai = trangThai;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        if(!maNV.matches("NV\\d{2}"))
            throw new IllegalArgumentException("Mã nhân viên bắt đầu NV và có 2 chữ số, vd: NV01");
        this.maNV = maNV;
    }

    public String getTenNV() {
        return tenNV;
    }

    public void setTenNV(String tenNV) {
        if(!tenNV.matches("^[\\p{Lu}][\\p{Ll}]+(\\s[\\p{Lu}][\\p{Ll}]+)+$"))
            throw new IllegalArgumentException("Tên bắt đầu chữ in hoa và 2 từ trở lên");
        this.tenNV = tenNV;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        if(!sdt.matches("0\\d{9}"))
            throw new IllegalArgumentException("Số điện thoại gồm 10 số và bắt đầu bằng số 0");
        this.sdt = sdt;
    }

    public RoleNhanVien getRoleNV() {
        return roleNV;
    }

    public void setRoleNV(RoleNhanVien roleNV) {
        this.roleNV = roleNV;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        NhanVien nhanVien = (NhanVien) o;
        return Objects.equals(maNV, nhanVien.maNV);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(maNV);
    }

    @Override
    public String toString() {
        return "NhanVien{" +
                "maNV='" + maNV + '\'' +
                ", tenNV='" + tenNV + '\'' +
                ", sdt='" + sdt + '\'' +
                ", roleNV=" + roleNV +
                ", trangThai=" + trangThai +
                '}';
    }
}
