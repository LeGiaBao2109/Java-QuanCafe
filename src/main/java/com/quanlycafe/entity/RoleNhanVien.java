package com.quanlycafe.entity;

public enum RoleNhanVien {
    ADMIN("Quản trị hệ thống"),
    NHAN_VIEN("Nhân viên bán hàng");

    private final String moTa;

    RoleNhanVien(String moTa) {
        this.moTa = moTa;
    }

    public String getMoTa() {
        return moTa;
    }
}
