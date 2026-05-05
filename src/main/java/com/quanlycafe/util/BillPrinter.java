package com.quanlycafe.util;

import com.quanlycafe.entity.ChiTietHoaDon;
import com.quanlycafe.entity.HoaDon;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BillPrinter {
    private static final DecimalFormat formatter = new DecimalFormat("#,###đ");
    private static final DateTimeFormatter dFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter tFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public static String generateBillHTML(HoaDon hd, List<ChiTietHoaDon> dsCT) {
        StringBuilder sb = new StringBuilder();

        double giaBanDau = 0;
        for (ChiTietHoaDon ct : dsCT) {
            giaBanDau += ct.getThanhTien();
        }

        double tong = hd.getTongTienCuoi();
        double giaGiam = giaBanDau - tong;

        sb.append("<html><body style='width: 100%; font-family: sans-serif; font-size: 12px; padding: 10px; color: black;'>");
        sb.append("<h2 style='text-align: center; margin-bottom: 2px; font-size: 16px;'>BARISTAPRO</h2>");
        sb.append("<p style='text-align: center; margin-top: 0; font-size: 10px;'>Hệ thống Quản lý Bán hàng chuyên nghiệp</p>");
        sb.append("<div style='border-top: 1px dashed black; margin: 10px 0;'></div>");

        sb.append("<table style='width: 100%; font-size: 11px;'>");
        sb.append("<tr><td colspan='2'><b>Mã HD:</b> ").append(hd.getMaHD()).append("</td></tr>");

        String tenNV = (hd.getMaDH() != null && hd.getMaDH().getMaNV() != null)
                ? hd.getMaDH().getMaNV().getTenNV()
                : "Quản trị viên";
        sb.append("<tr><td colspan='2'><b>Nhân viên:</b> ").append(tenNV).append("</td></tr>");

        String tenKH = (hd.getMaDH() != null && hd.getMaDH().getMaKH() != null)
                ? hd.getMaDH().getMaKH().getTenKH()
                : "Khách vãng lai";
        sb.append("<tr><td colspan='2'><b>Khách hàng:</b> ").append(tenKH).append("</td></tr>");

        sb.append("<tr>");
        sb.append("<td><b>Ngày:</b> ").append(hd.getNgayThanhToan().format(dFormatter)).append("</td>");
        sb.append("<td align='right'><b>Giờ:</b> ").append(hd.getNgayThanhToan().format(tFormatter)).append("</td>");
        sb.append("</tr>");
        sb.append("</table>");

        sb.append("<div style='border-top: 1px dashed black; margin: 10px 0;'></div>");

        sb.append("<table style='width: 100%; border-collapse: collapse; font-size: 11px;'>");
        sb.append("<tr style='border-bottom: 1px solid black;'>");
        sb.append("<th align='left' style='padding-bottom: 5px;'>Sản phẩm</th>");
        sb.append("<th align='center' style='padding-bottom: 5px;'>SL</th>");
        sb.append("<th align='right' style='padding-bottom: 5px;'>Thành tiền</th>");
        sb.append("</tr>");

        for (ChiTietHoaDon ct : dsCT) {
            String[] parts = ct.getGhiChu().split(" \\| ");
            String tenMonFull = parts[0];
            String ghiChuThucSu = parts.length > 1 ? parts[1] : "";

            sb.append("<tr>");
            sb.append("<td style='padding-top: 5px;'><b>").append(tenMonFull).append("</b></td>");
            sb.append("<td align='center' style='padding-top: 5px;'>").append(ct.getSoLuong()).append("</td>");
            sb.append("<td align='right' style='padding-top: 5px;'>").append(formatter.format(ct.getThanhTien())).append("</td>");
            sb.append("</tr>");

            sb.append("<tr><td colspan='3' style='font-style: italic; color: #555; font-size: 10px; padding-left: 10px;'>");
            sb.append("- ").append(ct.getLuongDa() != null ? ct.getLuongDa().getLabel() : "Bình thường")
                    .append(", ").append(ct.getLuongDuong() != null ? ct.getLuongDuong().getLabel() : "Bình thường");

            if (ct.getDsTopping() != null && !ct.getDsTopping().isEmpty()) {
                sb.append("<br>+ Topping: ").append(String.join(", ", ct.getDsTopping()));
            }

            if (!ghiChuThucSu.trim().isEmpty()) {
                sb.append("<br>* Ghi chú: ").append(ghiChuThucSu);
            }
            sb.append("</td></tr>");
        }

        sb.append("</table>");

        sb.append("<div style='border-top: 1px dashed black; margin: 10px 0;'></div>");
        sb.append("<table style='width: 100%; font-size: 12px;'>");

        sb.append("<tr><td align='right'>Giá ban đầu:</td>");
        sb.append("<td align='right'>").append(formatter.format(giaBanDau)).append("</td></tr>");

        if (giaGiam > 0) {
            sb.append("<tr><td align='right'>Giảm giá:</td>");
            sb.append("<td align='right' style='color: red;'>-").append(formatter.format(giaGiam)).append("</td></tr>");
        }

        sb.append("<tr><td align='right' style='padding-top: 5px;'><b>TỔNG:</b></td>");
        sb.append("<td align='right' style='font-size: 15px; padding-top: 5px;'><b>").append(formatter.format(tong)).append("</b></td></tr>");

        sb.append("<tr><td align='right' style='font-size: 10px; padding-top: 10px;'>PTTT:</td>");
        sb.append("<td align='right' style='font-size: 10px; padding-top: 10px;'>").append(hd.getPhuongThucTT() != null ? hd.getPhuongThucTT().getMoTa() : "N/A").append("</td></tr>");
        sb.append("</table>");

        sb.append("<div style='margin-top: 20px; text-align: center;'>");
        sb.append("<p style='font-style: italic; font-size: 10px;'>Cảm ơn quý khách!</p>");
        sb.append("</div>");

        sb.append("</body></html>");
        return sb.toString();
    }
}