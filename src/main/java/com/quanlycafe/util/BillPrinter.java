package com.quanlycafe.util;

import com.quanlycafe.entity.ChiTietHoaDon;
import com.quanlycafe.entity.HoaDon;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BillPrinter {
    private static final DecimalFormat formatter = new DecimalFormat("#,###đ");

    private static final DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static String generateBillHTML(HoaDon hd, List<ChiTietHoaDon> dsCT) {
        StringBuilder sb = new StringBuilder();

        sb.append("<html><body style='width: 100%; font-family: sans-serif; font-size: 12px; padding: 10px; color: black;'>");

        sb.append("<h2 style='text-align: center; margin-bottom: 2px; font-size: 16px;'>CAFE BIỂN GỌI</h2>");
        sb.append("<p style='text-align: center; margin-top: 0; font-size: 10px;'>35 Xuân Thủy, P. Thảo Điền, Quận 2</p>");

        sb.append("<div style='border-top: 1px dashed black; margin: 10px 0;'></div>");

        sb.append("<table style='width: 100%; font-size: 11px;'>");
        sb.append("<tr><td><b>Mã HD:</b> ").append(hd.getMaHD()).append("</td><td align='right'><b>Bàn:</b> ").append(hd.getMaDH() != null ? hd.getMaDH().getStt() : "-").append("</td></tr>");

        String tenNV = (hd.getMaDH() != null && hd.getMaDH().getMaNV() != null)
                ? hd.getMaDH().getMaNV().getTenNV()
                : "Quản trị viên";
        sb.append("<tr><td colspan='2'><b>Nhân viên:</b> ").append(tenNV).append("</td></tr>");

        String tenKH = (hd.getMaDH() != null && hd.getMaDH().getMaKH() != null)
                ? hd.getMaDH().getMaKH().getTenKH()
                : "Khách vãng lai";
        sb.append("<tr><td colspan='2'><b>Khách hàng:</b> ").append(tenKH).append("</td></tr>");

        sb.append("<tr><td colspan='2'><b>Ngày:</b> ").append(hd.getNgayThanhToan().format(dtFormatter)).append("</td></tr>");
        sb.append("</table>");

        sb.append("<div style='border-top: 1px dashed black; margin: 10px 0;'></div>");

        sb.append("<table style='width: 100%; border-collapse: collapse; font-size: 11px;'>");
        sb.append("<tr style='border-bottom: 1px solid black;'>");
        sb.append("<th align='left' style='padding-bottom: 5px;'>Sản phẩm</th>");
        sb.append("<th align='center' style='padding-bottom: 5px;'>SL</th>");
        sb.append("<th align='right' style='padding-bottom: 5px;'>Thành tiền</th>");
        sb.append("</tr>");

        for (ChiTietHoaDon ct : dsCT) {
            sb.append("<tr>");
            String tenMon = (ct.getMaSize() != null) ? ct.getMaSize().getMaSize() : "Sản phẩm";
            sb.append("<td style='padding-top: 5px;'><b>").append(tenMon).append("</b></td>");
            sb.append("<td align='center' style='padding-top: 5px;'>").append(ct.getSoLuong()).append("</td>");
            sb.append("<td align='right' style='padding-top: 5px;'>").append(formatter.format(ct.getThanhTien())).append("</td>");
            sb.append("</tr>");

            sb.append("<tr><td colspan='3' style='font-style: italic; color: #555; font-size: 10px; padding-left: 10px;'>");

            sb.append("- ").append(ct.getLuongDa()).append(", ").append(ct.getLuongDuong());

            if (ct.getDsTopping() != null && !ct.getDsTopping().isEmpty()) {
                sb.append("<br>+ Topping: ").append(String.join(", ", ct.getDsTopping()));
            }

            if (ct.getGhiChu() != null && !ct.getGhiChu().trim().isEmpty()) {
                sb.append("<br>* Ghi chú: ").append(ct.getGhiChu());
            }

            sb.append("</td></tr>");
        }

        sb.append("</table>");

        sb.append("<div style='border-top: 1px dashed black; margin: 10px 0;'></div>");
        sb.append("<table style='width: 100%; font-size: 12px;'>");

        if (hd.getMaVoucher() != null) {
            sb.append("<tr><td align='right'>Voucher (").append(hd.getMaVoucher().getMaCT()).append("):</td>");
            sb.append("<td align='right'>-").append(formatter.format(hd.getMaVoucher().getGiaTriGiam())).append("</td></tr>");
        }

        sb.append("<tr><td align='right'><b>TỔNG CỘNG:</b></td>");
        sb.append("<td align='right' style='font-size: 15px;'><b>").append(formatter.format(hd.getTongTienCuoi())).append("</b></td></tr>");

        sb.append("<tr><td align='right'>Hình thức:</td>");
        sb.append("<td align='right'>").append(hd.getPhuongThucTT()).append("</td></tr>");
        sb.append("</table>");

        // 5. Chân trang
        sb.append("<div style='margin-top: 20px; text-align: center;'>");
        sb.append("<p style='font-style: italic; font-size: 10px;'>Cảm ơn quý khách và hẹn gặp lại!</p>");
        sb.append("</div>");

        sb.append("</body></html>");
        return sb.toString();
    }
}