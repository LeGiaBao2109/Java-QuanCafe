package com.quanlycafe.entity;

import java.util.Objects;

public class NhanVien {
    private String maNV;
    private String tenNV;
    private String sdt;
    private RoleNhanVien roleNV;
    private boolean trangThai;

    public NhanVien() {
    }

    public NhanVien(String maNV, String tenNV, String sdt, RoleNhanVien roleNV, boolean trangThai) {
        this.maNV = maNV;
        this.tenNV = tenNV;
        this.sdt = sdt;
        this.roleNV = roleNV;
        this.trangThai = trangThai;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getTenNV() {
        return tenNV;
    }

    public void setTenNV(String tenNV) {
        this.tenNV = tenNV;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
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
