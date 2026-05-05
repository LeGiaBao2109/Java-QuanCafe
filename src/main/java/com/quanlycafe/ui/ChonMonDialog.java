package com.quanlycafe.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ChonMonDialog extends JDialog {

    private final Color COLOR_PRIMARY_DARK = new Color(92, 64, 51);
    private final Color COLOR_BG = Color.WHITE;
    private final Color COLOR_BORDER = new Color(222, 204, 190);

    private String tenMonGoc;
    private String maDM; // Lưu mã danh mục
    private boolean isDrink; // Biến check loại món
    private int giaGoc;
    private int giaTong;

    private ButtonGroup sizeGroup, duongGroup, daGroup;
    private JRadioButton radSizeM, radSizeL;
    private List<JCheckBox> chkToppings;
    private JTextField txtGhiChu;
    private JSpinner spnSoLuong;
    private JButton btnXacNhan;

    private boolean isConfirmed = false;
    private String finalTenMonDetail;
    private int finalSoLuong;

    private DecimalFormat formatter = new DecimalFormat("#,###đ");

    public ChonMonDialog(Window owner, String tenMon, int giaGoc, String maDM) {
        super(owner, "Tùy chỉnh món: " + tenMon, Dialog.ModalityType.APPLICATION_MODAL);
        this.tenMonGoc = tenMon;
        this.giaGoc = giaGoc;
        this.giaTong = giaGoc;
        this.maDM = maDM; // Gán giá trị
        this.isDrink = (maDM != null) && !maDM.isEmpty() && !maDM.equalsIgnoreCase("DM002");

        if (tenMon.toLowerCase().contains("bánh")) {
            this.isDrink = false;
        }

        setSize(450, 650);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_BG);

        JPanel pnlBody = new JPanel();
        pnlBody.setLayout(new BoxLayout(pnlBody, BoxLayout.Y_AXIS));
        pnlBody.setBackground(COLOR_BG);
        pnlBody.setBorder(new EmptyBorder(10, 15, 10, 15));

        // CHỈ ADD NẾU LÀ NƯỚC (Bánh sẽ bỏ qua hết đoạn này)
        if (isDrink) {
            pnlBody.add(createSizePanel());
            pnlBody.add(Box.createVerticalStrut(10));

            JPanel pnlDuongDa = new JPanel(new GridLayout(1, 2, 10, 0));
            pnlDuongDa.setBackground(COLOR_BG);
            pnlDuongDa.add(createDuongPanel());
            pnlDuongDa.add(createDaPanel());
            pnlBody.add(pnlDuongDa);

            pnlBody.add(Box.createVerticalStrut(10));
            pnlBody.add(createToppingPanel());
            pnlBody.add(Box.createVerticalStrut(10));
        }

        // Ghi chú thì món nào cũng có
        pnlBody.add(createGhiChuPanel());

        JScrollPane scrollPane = new JScrollPane(pnlBody);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        add(createFooterPanel(), BorderLayout.SOUTH);

        updateTotalPrice();
    }

    private JPanel createSizePanel() {
        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnl.setBackground(COLOR_BG);
        pnl.setBorder(createCustomTitledBorder("Chọn Size"));

        sizeGroup = new ButtonGroup();
        radSizeM = new JRadioButton("Size M (+0đ)", true);
        radSizeL = new JRadioButton("Size L (+10.000đ)");

        radSizeM.setBackground(COLOR_BG);
        radSizeL.setBackground(COLOR_BG);

        radSizeM.addActionListener(e -> updateTotalPrice());
        radSizeL.addActionListener(e -> updateTotalPrice());

        sizeGroup.add(radSizeM);
        sizeGroup.add(radSizeL);

        pnl.add(radSizeM);
        pnl.add(radSizeL);
        return pnl;
    }

    private JPanel createDuongPanel() {
        JPanel pnl = new JPanel(new GridLayout(0, 1));
        pnl.setBackground(COLOR_BG);
        pnl.setBorder(createCustomTitledBorder("Lượng Đường"));

        duongGroup = new ButtonGroup();
        String[] mucDuong = {"Bình thường", "70% Đường", "50% Đường", "30% Đường", "Không Đường"};
        boolean isFirst = true;
        for (String md : mucDuong) {
            JRadioButton rad = new JRadioButton(md, isFirst);
            rad.setBackground(COLOR_BG);
            rad.setActionCommand(md);
            duongGroup.add(rad);
            pnl.add(rad);
            isFirst = false;
        }
        return pnl;
    }

    private JPanel createDaPanel() {
        JPanel pnl = new JPanel(new GridLayout(0, 1));
        pnl.setBackground(COLOR_BG);
        pnl.setBorder(createCustomTitledBorder("Lượng Đá"));

        daGroup = new ButtonGroup();
        String[] mucDa = {"Bình thường", "70% Đá", "50% Đá", "30% Đá", "Không Đá", "Uống Nóng"};
        boolean isFirst = true;
        for (String md : mucDa) {
            JRadioButton rad = new JRadioButton(md, isFirst);
            rad.setBackground(COLOR_BG);
            rad.setActionCommand(md);
            daGroup.add(rad);
            pnl.add(rad);
            isFirst = false;
        }
        return pnl;
    }

    private JPanel createToppingPanel() {
        JPanel pnl = new JPanel(new GridLayout(0, 2));
        pnl.setBackground(COLOR_BG);
        pnl.setBorder(createCustomTitledBorder("Thêm Topping"));

        chkToppings = new ArrayList<>();
        String[][] toppings = {
                {"Trân châu đen", "5000"}, {"Trân châu trắng", "5000"},
                {"Thạch đào", "5000"}, {"Thạch trái cây", "5000"},
                {"Phô mai viên", "8000"}, {"Kem Macchiato", "10000"}
        };

        for (String[] tp : toppings) {
            JCheckBox chk = new JCheckBox(tp[0] + " (+" + formatter.format(Integer.parseInt(tp[1])) + ")");
            chk.setBackground(COLOR_BG);
            chk.setActionCommand(tp[1]);
            chk.setName(tp[0]);
            chk.addActionListener(e -> updateTotalPrice());
            chkToppings.add(chk);
            pnl.add(chk);
        }
        return pnl;
    }

    private JPanel createGhiChuPanel() {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setBackground(COLOR_BG);
        pnl.setBorder(createCustomTitledBorder("Ghi chú thêm"));

        txtGhiChu = new JTextField();
        txtGhiChu.setPreferredSize(new Dimension(0, 35));
        txtGhiChu.setBorder(new LineBorder(COLOR_BORDER, 1));
        pnl.add(txtGhiChu, BorderLayout.CENTER);
        return pnl;
    }

    private JPanel createFooterPanel() {
        JPanel pnl = new JPanel(new BorderLayout(15, 0));
        pnl.setBackground(COLOR_BG);
        pnl.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDER, 1),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JPanel pnlSL = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlSL.setOpaque(false);
        pnlSL.add(new JLabel("Số lượng: "));
        spnSoLuong = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        spnSoLuong.setPreferredSize(new Dimension(80, 35));
        spnSoLuong.setFont(new Font("Segoe UI", Font.BOLD, 14));
        spnSoLuong.addChangeListener(e -> updateTotalPrice());
        pnlSL.add(spnSoLuong);

        btnXacNhan = new JButton("Thêm vào giỏ - " + formatter.format(giaTong));
        btnXacNhan.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnXacNhan.setBackground(COLOR_PRIMARY_DARK);
        btnXacNhan.setForeground(Color.WHITE);
        btnXacNhan.setFocusPainted(false);
        btnXacNhan.setOpaque(true);
        btnXacNhan.setBorderPainted(false);
        btnXacNhan.setPreferredSize(new Dimension(240, 45));
        btnXacNhan.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnXacNhan.addActionListener(e -> {
            buildFinalDetails();
            isConfirmed = true;
            dispose();
        });

        pnl.add(pnlSL, BorderLayout.WEST);
        pnl.add(btnXacNhan, BorderLayout.EAST);
        return pnl;
    }

    private void updateTotalPrice() {
        int tempPrice = giaGoc;

        if (isDrink) { // Chỉ tính thêm tiền nếu là nước và có bảng chọn
            if (radSizeL != null && radSizeL.isSelected()) tempPrice += 10000;

            if (chkToppings != null) {
                for (JCheckBox chk : chkToppings) {
                    if (chk.isSelected()) {
                        tempPrice += Integer.parseInt(chk.getActionCommand());
                    }
                }
            }
        }

        int sl = (int) spnSoLuong.getValue();
        giaTong = tempPrice * sl;

        if (btnXacNhan != null) {
            btnXacNhan.setText("Thêm " + sl + " món - " + formatter.format(giaTong));
        }
    }

    private void buildFinalDetails() {
        finalSoLuong = (int) spnSoLuong.getValue();

        String sizeStr = "";
        String tuyChinhStr = "";
        String toppingStr = "";

        if (isDrink) {
            // Size
            String size = radSizeL.isSelected() ? "Size L" : "Size M";
            sizeStr = " (" + size + ")";

            // Đường/Đá - Dùng try-catch hoặc kiểm tra null để tránh crash nếu chưa chọn
            try {
                String duong = duongGroup.getSelection().getActionCommand();
                String da = daGroup.getSelection().getActionCommand();

                List<String> listTuyChinh = new ArrayList<>();
                if (!duong.equals("Bình thường")) listTuyChinh.add(duong);
                if (!da.equals("Bình thường")) listTuyChinh.add(da);
                tuyChinhStr = listTuyChinh.isEmpty() ? "" : "<br/><i style='font-size:10px; color:gray;'>" + String.join(", ", listTuyChinh) + "</i>";
            } catch (Exception e) {
                // Phòng hờ nếu ButtonGroup chưa có lựa chọn
            }

            // Topping
            List<String> listTopping = new ArrayList<>();
            for (JCheckBox chk : chkToppings) {
                if (chk.isSelected()) {
                    int giaTopping = Integer.parseInt(chk.getActionCommand());
                    listTopping.add("+ " + chk.getName() + " (" + formatter.format(giaTopping) + ")");
                }
            }
            toppingStr = listTopping.isEmpty() ? "" : "<br/><span style='font-size:10px; color:#c0392b;'>" + String.join("<br/>", listTopping) + "</span>";
        }

        String ghiChuStr = txtGhiChu.getText().trim().isEmpty() ? "" : "<br/><span style='font-size:10px; color:#2980b9;'>* " + txtGhiChu.getText().trim() + "</span>";

        finalTenMonDetail = "<html><b>" + tenMonGoc + "</b>" + sizeStr
                + tuyChinhStr + toppingStr + ghiChuStr + "</html>";
    }

    private TitledBorder createCustomTitledBorder(String title) {
        TitledBorder border = BorderFactory.createTitledBorder(new LineBorder(COLOR_BORDER), title);
        border.setTitleFont(new Font("Segoe UI", Font.BOLD, 14));
        border.setTitleColor(COLOR_PRIMARY_DARK);
        return border;
    }

    public List<String> getSelectedToppingNames() {
        List<String> selected = new ArrayList<>();
        // Nếu là bánh hoặc không có bảng topping thì trả về list rỗng
        if (isDrink && chkToppings != null) {
            for (JCheckBox chk : chkToppings) {
                if (chk.isSelected()) {
                    // Lấy Name (ví dụ: "Trân châu đen") đã set ở hàm createToppingPanel
                    selected.add(chk.getName());
                }
            }
        }
        return selected;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public String getFinalTenMonDetail() {
        return finalTenMonDetail;
    }

    public int getDonGia() {
        return giaTong / finalSoLuong;
    }

    public int getSoLuong() {
        return finalSoLuong;
    }
}