package com.quanlycafe.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChiTietHoaDon {
    private String maCTHD;
    private DonHang donHang;
    private String maSize;
    private int soLuong;
    private double donGia;
    private String ghiChu;
    private double thanhTien;
    private MucDa luongDa;
    private MucDuong luongDuong;
    private List<String> dsTopping = new ArrayList<>();

    public ChiTietHoaDon() {
    }

    public ChiTietHoaDon(String maCTHD, DonHang donHang, String maSize, int soLuong, double donGia, String ghiChu, double thanhTien, MucDa luongDa, MucDuong luongDuong, List<String> dsTopping) {
        this.maCTHD = maCTHD;
        this.donHang = donHang;
        this.maSize = maSize;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.ghiChu = ghiChu;
        this.thanhTien = thanhTien;
        this.luongDa = luongDa;
        this.luongDuong = luongDuong;
        this.dsTopping = dsTopping;
    }

    public String getMaCTHD() {
        return maCTHD;
    }

    public void setMaCTHD(String maCTHD) {
        this.maCTHD = maCTHD;
    }

    public DonHang getDonHang() {
        return donHang;
    }

    public void setDonHang(DonHang donHang) {
        this.donHang = donHang;
    }

    public String getMaSize() {
        return maSize;
    }

    public void setMaSize(String maSize) {
        this.maSize = maSize;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public double getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(double thanhTien) {
        this.thanhTien = thanhTien;
    }

    public MucDa getLuongDa() {
        return luongDa;
    }

    public void setLuongDa(MucDa luongDa) {
        this.luongDa = luongDa;
    }

    public MucDuong getLuongDuong() {
        return luongDuong;
    }

    public void setLuongDuong(MucDuong luongDuong) {
        this.luongDuong = luongDuong;
    }

    public List<String> getDsTopping() {
        return dsTopping;
    }

    public void setDsTopping(List<String> dsTopping) {
        this.dsTopping = dsTopping;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChiTietHoaDon that = (ChiTietHoaDon) o;
        return Objects.equals(maCTHD, that.maCTHD);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maCTHD);
    }

    @Override
    public String toString() {
        return "ChiTietHoaDon{" +
                "maCTHD='" + maCTHD + '\'' +
                ", maSize='" + maSize + '\'' +
                ", soLuong=" + soLuong +
                ", thanhTien=" + thanhTien +
                '}';
    }
}