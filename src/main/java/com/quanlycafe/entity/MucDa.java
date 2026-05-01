package com.quanlycafe.entity;

public enum MucDa {
    KHONG_DA("0% Đá"),
    IT_DA("25% Đá"),
    VUA_DA("50% Đá"),
    BINH_THUONG("100% Đá");

    private final String label;

    MucDa(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
