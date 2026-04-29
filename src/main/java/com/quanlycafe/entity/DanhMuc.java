package com.quanlycafe.entity;

import java.util.Objects;

public class DanhMuc {
    private String maDM;
    private String tenDM;

    public DanhMuc() {
    }

    public DanhMuc(String maDM, String tenDM) {
        this.maDM = maDM;
        this.tenDM = tenDM;
    }

    public String getMaDM() {
        return maDM;
    }

    public void setMaDM(String maDM) {
        this.maDM = maDM;
    }

    public String getTenDM() {
        return tenDM;
    }

    public void setTenDM(String tenDM) {
        this.tenDM = tenDM;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DanhMuc danhMuc = (DanhMuc) o;
        return Objects.equals(maDM, danhMuc.maDM);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(maDM);
    }

    @Override
    public String toString() {
        return "DanhMuc{" +
                "maDM='" + maDM + '\'' +
                ", tenDM='" + tenDM + '\'' +
                '}';
    }
}
