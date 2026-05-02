package com.quanlycafe.entity;

public enum LoaiGiamGia {
    DIEM_TICH_LUY("Giảm giá bằng điểm tích lũy"),
    KHACH_HANG_MOI("Ưu đãi lần đầu mua hàng"),
    SU_KIEN("Giảm giá theo chương trình/Sự kiện"),
    KHONG_GIAM("Không áp dụng giảm giá");

    private String moTa;

    LoaiGiamGia(String moTa) {
        this.moTa = moTa;
    }

    public String getMoTa() {
        return moTa;
    }
}