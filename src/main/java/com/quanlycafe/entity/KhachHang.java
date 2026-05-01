package com.quanlycafe.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class KhachHang {
    private String maKH;
    private String tenKH;
    private String sdt;
    private LocalDateTime ngayTao;
    private int diemTL;

    public KhachHang() {
    }

    public KhachHang(String maKH, String tenKH, String sdt, LocalDateTime ngayTao, int diemTL) {
        this.maKH = maKH;
        this.tenKH = tenKH;
        this.sdt = sdt;
        this.ngayTao = ngayTao;
        this.diemTL = diemTL;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public LocalDateTime getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(LocalDateTime ngayTao) {
        this.ngayTao = ngayTao;
    }

    public int getDiemTL() {
        return diemTL;
    }

    public void setDiemTL() {
        this.diemTL = diemTL;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        KhachHang khachHang = (KhachHang) o;
        return Objects.equals(maKH, khachHang.maKH);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(maKH);
    }

    @Override
    public String toString() {
        return "KhachHang{" +
                "maKH='" + maKH + '\'' +
                ", tenKH='" + tenKH + '\'' +
                ", sdt='" + sdt + '\'' +
                ", ngayTao=" + ngayTao +
                ", diemTL=" + diemTL +
                '}';
    }
}
