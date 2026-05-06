package com.quanlycafe.ui.component;

import com.quanlycafe.ui.LoginDialog1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Sidebar extends JPanel {

    private final Color COLOR_BORDER = new Color(235, 230, 225);
    private final Color COLOR_PRIMARY_DARK = new Color(92, 64, 51);
    private final Color COLOR_TEXT_MAIN = new Color(70, 55, 45);
    private final Color COLOR_BG = new Color(255, 255, 255);
    private final Color COLOR_HOVER = new Color(248, 245, 242);
    private final Color COLOR_DANGER = new Color(220, 53, 69);

    private List<JButton> menuButtons = new ArrayList<>();

    public Sidebar(String role, String userName, Consumer<String> onMenuClick) {
        setLayout(new BorderLayout());
        setBackground(COLOR_BG);
        setPreferredSize(new Dimension(240, 0));
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, COLOR_BORDER));

        JPanel topMenu = new JPanel();
        topMenu.setLayout(new BoxLayout(topMenu, BoxLayout.Y_AXIS));
        topMenu.setBackground(COLOR_BG);
        topMenu.setBorder(new EmptyBorder(30, 15, 20, 15));

        JLabel lblName = new JLabel(userName);
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblName.setForeground(COLOR_PRIMARY_DARK);
        lblName.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblRole = new JLabel(role.equalsIgnoreCase("ADMIN") ? "Quản lý hệ thống" : "Nhân viên vận hành");
        lblRole.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblRole.setForeground(new Color(140, 130, 120));
        lblRole.setAlignmentX(Component.LEFT_ALIGNMENT);

        topMenu.add(lblName);
        topMenu.add(Box.createVerticalStrut(5));
        topMenu.add(lblRole);
        topMenu.add(Box.createVerticalStrut(35));

        if (role.equalsIgnoreCase("ADMIN")) {
            JButton btnSP = createNavButton("Sản phẩm & Menu", true);
            JButton btnNV = createNavButton("Quản lý nhân viên", false);
            JButton btnBC = createNavButton("Báo cáo doanh thu", false);

            btnSP.addActionListener(e -> { setActive(btnSP); if (onMenuClick != null) onMenuClick.accept("SanPham"); });
            btnNV.addActionListener(e -> { setActive(btnNV); if (onMenuClick != null) onMenuClick.accept("NhanVien"); });
            btnBC.addActionListener(e -> { setActive(btnBC); if (onMenuClick != null) onMenuClick.accept("BaoCao"); });

            topMenu.add(btnSP); topMenu.add(Box.createVerticalStrut(10));
            topMenu.add(btnNV); topMenu.add(Box.createVerticalStrut(10));
            topMenu.add(btnBC);

        } else {
            JButton btnOrder = createNavButton("Order bán hàng", true);
            JButton btnBill = createNavButton("Quản lý hóa đơn", false);
            JButton btnKH = createNavButton("Khách hàng", false);
            JButton btnNhapKho = createNavButton("Nhập kho hàng", false);
            JButton btnBaoCaoCa = createNavButton("Báo cáo chốt ca", false);

            btnOrder.addActionListener(e -> { setActive(btnOrder); if (onMenuClick != null) onMenuClick.accept("BanHang"); });
            btnBill.addActionListener(e -> { setActive(btnBill); if (onMenuClick != null) onMenuClick.accept("HoaDon"); });
            btnKH.addActionListener(e -> { setActive(btnKH); if (onMenuClick != null) onMenuClick.accept("KhachHang"); });
            btnNhapKho.addActionListener(e -> { setActive(btnNhapKho); if (onMenuClick != null) onMenuClick.accept("NhapKho"); });
            btnBaoCaoCa.addActionListener(e -> { setActive(btnBaoCaoCa); if (onMenuClick != null) onMenuClick.accept("ThongKeNV"); });

            topMenu.add(btnOrder); topMenu.add(Box.createVerticalStrut(10));
            topMenu.add(btnBill); topMenu.add(Box.createVerticalStrut(10));
            topMenu.add(btnKH); topMenu.add(Box.createVerticalStrut(10));
            topMenu.add(btnNhapKho); topMenu.add(Box.createVerticalStrut(10));
            topMenu.add(btnBaoCaoCa);
        }

        JPanel bottomMenu = new JPanel(new BorderLayout());
        bottomMenu.setBackground(COLOR_BG);
        bottomMenu.setBorder(new EmptyBorder(15, 15, 25, 15));

        JButton btnLogout = createNavButton("Đăng xuất", false);
        btnLogout.setForeground(COLOR_DANGER);

        btnLogout.addActionListener(e -> {
            UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.PLAIN, 14));
            UIManager.put("OptionPane.buttonFont", new Font("Segoe UI", Font.BOLD, 13));

            int confirm = JOptionPane.showConfirmDialog(
                    null,
                    "Bạn có chắc chắn muốn đăng xuất?",
                    "Xác nhận",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                Window currentWindow = SwingUtilities.getWindowAncestor(this);
                if (currentWindow != null) {
                    currentWindow.dispose();
                    new LoginDialog1().setVisible(true);
                }
            }
        });

        bottomMenu.add(btnLogout, BorderLayout.CENTER);

        add(topMenu, BorderLayout.NORTH);
        add(bottomMenu, BorderLayout.SOUTH);
    }

    private JButton createNavButton(String text, boolean isActive) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getBackground() != COLOR_BG) {
                    g2.setColor(getBackground());
                    g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 12, 12));
                }
                super.paintComponent(g2);
                g2.dispose();
            }
        };

        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(210, 48));
        btn.setPreferredSize(new Dimension(210, 48));
        btn.setBorder(new EmptyBorder(0, 15, 0, 0));

        if (isActive) {
            btn.setBackground(COLOR_PRIMARY_DARK);
            btn.setForeground(Color.WHITE);
        } else {
            btn.setBackground(COLOR_BG);
            btn.setForeground(COLOR_TEXT_MAIN);
        }

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (btn.getBackground() != COLOR_PRIMARY_DARK) {
                    btn.setBackground(COLOR_HOVER);
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (btn.getBackground() != COLOR_PRIMARY_DARK) {
                    btn.setBackground(COLOR_BG);
                }
            }
        });

        menuButtons.add(btn);
        return btn;
    }

    private void setActive(JButton activeBtn) {
        for (JButton btn : menuButtons) {
            if (btn.getText().contains("Đăng xuất")) continue;
            btn.setBackground(COLOR_BG);
            btn.setForeground(COLOR_TEXT_MAIN);
        }
        activeBtn.setBackground(COLOR_PRIMARY_DARK);
        activeBtn.setForeground(Color.WHITE);
    }
}