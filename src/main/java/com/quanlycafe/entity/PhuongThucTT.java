package com.quanlycafe.entity;

public enum PhuongThucTT {
    TIEN_MAT("Tiền mặt"),
    CHUYEN_KHOAN("Chuyển khoản / QR");

    private String moTa;

    PhuongThucTT(String moTa) {
        this.moTa = moTa;
    }

    public String getMoTa() {
        return moTa;
    }
}
