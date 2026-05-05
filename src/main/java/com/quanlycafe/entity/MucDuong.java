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

    public static String getLabelFromCode(String code) {
        if (code == null || code.isEmpty()) return "Bình thường";
        for (MucDuong mdu : MucDuong.values()) {
            if (mdu.name().equalsIgnoreCase(code)) {
                return mdu.getLabel();
            }
        }
        return code;
    }

    public static MucDuong fromLabel(String label) {
        for (MucDuong mdu : MucDuong.values()) {
            if (mdu.label.equalsIgnoreCase(label)) {
                return mdu;
            }
        }
        return DUONG_100;
    }
}