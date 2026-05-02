package com.quanlycafe.entity;

public enum MucDa {
    DA_0("0% Đá"),
    DA_25("25% Đá"),
    DA_50("50% Đá"),
    DA_75("75% Đá"),
    DA_100("100% Đá");

    private final String label;

    MucDa(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
