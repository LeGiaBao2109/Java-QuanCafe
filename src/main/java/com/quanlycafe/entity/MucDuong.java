package com.quanlycafe.entity;

public enum MucDuong {
    KHONG_DUONG("0% Đường"),
    IT_DUONG("25% Đường"),
    VUA_DUONG("50% Đường"),
    BINH_THUONG("100% Đường");

    private final String label;

    MucDuong(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
