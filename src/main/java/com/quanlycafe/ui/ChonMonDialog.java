package com.quanlycafe.ui;

import com.quanlycafe.entity.KichCo;
import com.quanlycafe.entity.MucDa;
import com.quanlycafe.entity.MucDuong;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class ChonMonDialog extends JDialog {
    private final Color COLOR_PRIMARY_DARK = new Color(92, 64, 51);
    private final Color COLOR_BG = Color.WHITE;
    private final Color COLOR_BORDER = new Color(222, 204, 190);

    private String tenMonGoc;
    private String maDM;
    private boolean isDrink;
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
    private List<KichCo> dsKichCo;

    public ChonMonDialog(Window owner, String tenMon, int giaGoc, String maDM, List<KichCo> dsKichCo) {
        super(owner, "Tùy chỉnh món: " + tenMon, Dialog.ModalityType.APPLICATION_MODAL);
        this.tenMonGoc = tenMon;
        this.giaGoc = giaGoc;
        this.giaTong = giaGoc;
        this.maDM = maDM;
        this.dsKichCo = dsKichCo != null ? dsKichCo : new ArrayList<>();
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

        for (KichCo kc : dsKichCo) {
            String displayLabel = kc.getTenSize();
            if (displayLabel.equalsIgnoreCase("Size L")) {
                radSizeL = new JRadioButton(displayLabel + " (+" + formatter.format(10000) + ")");
                radSizeL.setBackground(COLOR_BG);
                radSizeL.setActionCommand(kc.getMaSize());
                radSizeL.addActionListener(e -> updateTotalPrice());
                sizeGroup.add(radSizeL);
                pnl.add(radSizeL);
            } else if (displayLabel.equalsIgnoreCase("Size M")) {
                radSizeM = new JRadioButton(displayLabel + " (+0đ)", true);
                radSizeM.setBackground(COLOR_BG);
                radSizeM.setActionCommand(kc.getMaSize());
                radSizeM.addActionListener(e -> updateTotalPrice());
                sizeGroup.add(radSizeM);
                pnl.add(radSizeM);
            }
        }

        if (radSizeM == null && radSizeL == null) {
            radSizeM = new JRadioButton("Size M (+0đ)", true);
            radSizeM.setActionCommand("S01");
            radSizeM.setBackground(COLOR_BG);
            sizeGroup.add(radSizeM);
            pnl.add(radSizeM);
        }

        return pnl;
    }

    private JPanel createDuongPanel() {
        JPanel pnl = new JPanel(new GridLayout(0, 1));
        pnl.setBackground(COLOR_BG);
        pnl.setBorder(createCustomTitledBorder("Lượng Đường"));

        duongGroup = new ButtonGroup();
        for (MucDuong md : MucDuong.values()) {
            boolean isDefault = (md == MucDuong.DUONG_100);
            JRadioButton rad = new JRadioButton(md.getLabel(), isDefault);
            rad.setBackground(COLOR_BG);
            rad.setActionCommand(md.getLabel());
            duongGroup.add(rad);
            pnl.add(rad);
        }
        return pnl;
    }

    private JPanel createDaPanel() {
        JPanel pnl = new JPanel(new GridLayout(0, 1));
        pnl.setBackground(COLOR_BG);
        pnl.setBorder(createCustomTitledBorder("Lượng Đá"));

        daGroup = new ButtonGroup();
        for (MucDa md : MucDa.values()) {
            boolean isDefault = (md == MucDa.DA_100);
            JRadioButton rad = new JRadioButton(md.getLabel(), isDefault);
            rad.setBackground(COLOR_BG);
            rad.setActionCommand(md.getLabel());
            daGroup.add(rad);
            pnl.add(rad);
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

        if (isDrink) {
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
            String sizeText = "Size M";
            if (radSizeL != null && radSizeL.isSelected()) sizeText = "Size L";
            sizeStr = " (" + sizeText + ")";

            try {
                String duong = duongGroup.getSelection().getActionCommand();
                String da = daGroup.getSelection().getActionCommand();

                List<String> listTuyChinh = new ArrayList<>();
                if (!duong.equals(MucDuong.DUONG_100.getLabel())) listTuyChinh.add(duong);
                if (!da.equals(MucDa.DA_100.getLabel())) listTuyChinh.add(da);
                tuyChinhStr = listTuyChinh.isEmpty() ? "" : "<br/><i style='font-size:10px; color:gray;'>" + String.join(", ", listTuyChinh) + "</i>";
            } catch (Exception e) {
            }

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
        if (isDrink && chkToppings != null) {
            for (JCheckBox chk : chkToppings) {
                if (chk.isSelected()) {
                    selected.add(chk.getName());
                }
            }
        }
        return selected;
    }

    public void setExistingData(int soLuong, String ghiChu, List<String> selectedToppings) {
        resetToDefault();
        spnSoLuong.setValue(soLuong);
        txtGhiChu.setText(ghiChu);

        if (isDrink && chkToppings != null && selectedToppings != null) {
            for (JCheckBox chk : chkToppings) {
                if (selectedToppings.contains(chk.getName())) {
                    chk.setSelected(true);
                }
            }
        }
        updateTotalPrice();
    }

    public MucDa getSelectedMucDa() {
        try {
            String cmd = daGroup.getSelection().getActionCommand();
            return MucDa.fromLabel(cmd);
        } catch (Exception e) {
            return MucDa.DA_100;
        }
    }

    public MucDuong getSelectedMucDuong() {
        try {
            String cmd = duongGroup.getSelection().getActionCommand();
            return MucDuong.fromLabel(cmd);
        } catch (Exception e) {
            return MucDuong.DUONG_100;
        }
    }

    private void resetToDefault() {
        if (spnSoLuong != null) spnSoLuong.setValue(1);
        if (txtGhiChu != null) txtGhiChu.setText("");
        if (radSizeM != null) radSizeM.setSelected(true);
        if (chkToppings != null) {
            for (JCheckBox chk : chkToppings) chk.setSelected(false);
        }
        updateRadioButtonGroup(daGroup, MucDa.DA_100.getLabel());
        updateRadioButtonGroup(duongGroup, MucDuong.DUONG_100.getLabel());
    }

    private void updateRadioButtonGroup(ButtonGroup group, String label) {
        if (group == null) return;
        Enumeration<AbstractButton> enu = group.getElements();
        while (enu.hasMoreElements()) {
            AbstractButton btn = enu.nextElement();
            if (btn.getActionCommand().equals(label)) {
                btn.setSelected(true);
                break;
            }
        }
    }

    public void setFullData(int soLuong, String ghiChu, List<String> toppings, String maSize, MucDa da, MucDuong duong) {
        resetToDefault();
        spnSoLuong.setValue(soLuong);
        txtGhiChu.setText(ghiChu);

        if (isDrink) {
            if (maSize != null && sizeGroup != null) {
                Enumeration<AbstractButton> enu = sizeGroup.getElements();
                while (enu.hasMoreElements()) {
                    AbstractButton btn = enu.nextElement();
                    if (btn.getActionCommand().equals(maSize)) {
                        btn.setSelected(true);
                        break;
                    }
                }
            }

            if (da != null && daGroup != null) {
                updateRadioButtonGroup(daGroup, da.getLabel());
            }

            if (duong != null && duongGroup != null) {
                updateRadioButtonGroup(duongGroup, duong.getLabel());
            }

            if (toppings != null && chkToppings != null) {
                for (JCheckBox chk : chkToppings) {
                    chk.setSelected(toppings.contains(chk.getName()));
                }
            }
        }
        updateTotalPrice();
    }

    public String getSelectedMaSize() {
        if (!isDrink) {
            return dsKichCo.stream()
                    .filter(kc -> kc.getTenSize().equalsIgnoreCase("Size M") || kc.getTenSize().equalsIgnoreCase("Tiêu chuẩn"))
                    .map(KichCo::getMaSize)
                    .findFirst()
                    .orElse("S01");
        }
        if (sizeGroup.getSelection() != null) {
            return sizeGroup.getSelection().getActionCommand();
        }
        return "S01";
    }

    public String getGhiChuThuan() {
        return txtGhiChu.getText().trim();
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