package com.quanlycafe.entity;

public enum MucDuong {
    DUONG_0("0% Đường"),
    DUONG_25("25% Đường"),
    DUONG_50("50% Đường"),
    DUONG_75("75% Đường"),
    DUONG_100("100% Đường");

    private final String label;

    MucDuong(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}