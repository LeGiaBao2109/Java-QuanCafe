package com.quanlycafe.entity;

public enum LoaiGiamGia {
    DIEM_TICH_LUY("Giảm giá bằng điểm tích lũy", 20000, false),
    KHACH_HANG_MOI("Ưu đãi lần đầu mua hàng", 10, true),
    SU_KIEN("Giảm giá theo chương trình/Sự kiện", 15, true),
    DOI_MON_FREE("Đổi món miễn phí", 0, false),
    KHONG_GIAM("Không áp dụng giảm giá", 0, false);

    private String moTa;
    private double giaTri;
    private boolean laPhanTram;

    LoaiGiamGia(String moTa, double giaTri, boolean laPhanTram) {
        this.moTa = moTa;
        this.giaTri = giaTri;
        this.laPhanTram = laPhanTram;
    }

    public String getMoTa() {
        return moTa;
    }

    public double getGiaTri() {
        return giaTri;
    }

    public boolean isLaPhanTram() {
        return laPhanTram;
    }

    public static LoaiGiamGia fromString(String text) {
        for (LoaiGiamGia b : LoaiGiamGia.values()) {
            if (b.name().equalsIgnoreCase(text)) {
                return b;
            }
        }
        return KHONG_GIAM;
    }
}