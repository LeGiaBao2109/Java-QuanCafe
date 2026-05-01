package com.quanlycafe.entity;

import java.util.Objects;

public class Voucher {
    private String maCT;
    private String tenCT;
    private int diemCanDoi;
    private LoaiGiamGia loaiGiamGia;
    private boolean trangThai;

    public Voucher() {
    }

    public Voucher(String maCT, String tenCT, int diemCanDoi, LoaiGiamGia loaiGiamGia, boolean trangThai) {
        this.maCT = maCT;
        this.tenCT = tenCT;
        this.diemCanDoi = diemCanDoi;
        this.loaiGiamGia = loaiGiamGia;
        this.trangThai = trangThai;
    }

    public String getMaCT() {
        return maCT;
    }

    public void setMaCT(String maCT) {
        this.maCT = maCT;
    }

    public String getTenCT() {
        return tenCT;
    }

    public void setTenCT(String tenCT) {
        this.tenCT = tenCT;
    }

    public int getDiemCanDoi() {
        return diemCanDoi;
    }

    public void setDiemCanDoi(int diemCanDoi) {
        this.diemCanDoi = diemCanDoi;
    }

    public LoaiGiamGia getLoaiGiamGia() {
        return loaiGiamGia;
    }

    public void setLoaiGiamGia(LoaiGiamGia loaiGiamGia) {
        this.loaiGiamGia = loaiGiamGia;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Voucher voucher = (Voucher) o;
        return Objects.equals(maCT, voucher.maCT);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(maCT);
    }

    @Override
    public String toString() {
        return "Voucher{" +
                "maCT='" + maCT + '\'' +
                ", tenCT='" + tenCT + '\'' +
                ", diemCanDoi=" + diemCanDoi +
                ", loaiGiamGia=" + loaiGiamGia +
                ", trangThai=" + trangThai +
                '}';
    }
}
