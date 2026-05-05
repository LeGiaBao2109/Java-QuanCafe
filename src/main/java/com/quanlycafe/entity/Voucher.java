package com.quanlycafe.entity;

import java.text.DecimalFormat;
import java.util.Objects;

public class Voucher {
    private String maCT;
    private String tenCT;
    private int diemCanDoi;
    private LoaiGiamGia loaiGiamGia;
    private boolean trangThai;
    private double giaTriGiam;

    private static final DecimalFormat df = new DecimalFormat("#,###đ");

    public Voucher() {
    }

    public Voucher(String maCT, String tenCT, int diemCanDoi, LoaiGiamGia loaiGiamGia, boolean trangThai) {
        this.maCT = maCT;
        this.tenCT = tenCT;
        this.diemCanDoi = diemCanDoi;
        this.loaiGiamGia = loaiGiamGia;
        this.trangThai = trangThai;
    }

    public Voucher(String maCT, String tenCT, int diemCanDoi, LoaiGiamGia loaiGiamGia, boolean trangThai, double giaTriGiam) {
        this.maCT = maCT;
        this.tenCT = tenCT;
        this.diemCanDoi = diemCanDoi;
        this.loaiGiamGia = loaiGiamGia;
        this.trangThai = trangThai;
        this.giaTriGiam = giaTriGiam;
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

    public void setGiaTriGiam(double giaTriGiam) {
        this.giaTriGiam = giaTriGiam;
    }

    public double getGiaTriGiam() {
        if (this.giaTriGiam > 0) {
            return this.giaTriGiam;
        }
        return (loaiGiamGia != null) ? loaiGiamGia.getGiaTri() : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Voucher voucher = (Voucher) o;
        return Objects.equals(maCT, voucher.maCT);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maCT);
    }

    @Override
    public String toString() {
        if (loaiGiamGia == null) return tenCT;
        if (loaiGiamGia.name().equals("DOI_MON_FREE")) {
            return tenCT + " (Tốn " + diemCanDoi + " điểm)";
        }

        String detail = loaiGiamGia.isLaPhanTram() ? (int) giaTriGiam + "%" : df.format(giaTriGiam);

        return tenCT + " - Giảm: " + detail;
    }
}