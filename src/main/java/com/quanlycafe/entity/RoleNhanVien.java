package com.quanlycafe.entity;

public enum RoleNhanVien {
    ADMIN("Quản trị hệ thống"),
    NHAN_VIEN("Nhân viên bán hàng");

    private final String moTa;

    // Constructor của Enum
    RoleNhanVien(String moTa) {
        this.moTa = moTa;
    }

    // Getter để lấy mô tả tiếng Việt khi cần hiển thị lên giao diện (GUI)
    public String getMoTa() {
        return moTa;
    }
}
