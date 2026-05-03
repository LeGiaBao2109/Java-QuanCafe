package com.quanlycafe.ui;

import com.quanlycafe.dao.SanPhamDAO;
import com.quanlycafe.ui.component.Sidebar;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class QuanLyForm extends JFrame {

    private final Color COLOR_TOP_BG = new Color(232, 215, 203);
    private final Color COLOR_HEADER_BOT = new Color(185, 137, 118);
    private final Color COLOR_ROW_BG = new Color(214, 214, 214);
    private final Color COLOR_WHITE = Color.WHITE;

    private List<Object[]> allData;
    private int currentPage = 1;
    private int rowsPerPage = 10;
    private int totalPages = 1;

    private DefaultTableModel model;
    private JLabel lblTotal;
    private JTextField txtPage;

    private CardLayout cardLayout;
    private JPanel cardPanel;

    public QuanLyForm(String role, String tenNV) { 
        setTitle("BaristaPro - Quản lý Thực đơn");
        setSize(1280, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        SanPhamDAO dao = new SanPhamDAO();
        allData = dao.layDanhSachSanPhamQuanLy();
        totalPages = (int) Math.ceil((double) allData.size() / rowsPerPage);
        if (totalPages == 0) totalPages = 1;

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        JPanel pnlSanPham = createSanPhamPanel(role, tenNV);
        JPanel pnlNhanVien = createPlaceholderPanel("Giao diện Quản lý Nhân viên");
        JPanel pnlBaoCao = createPlaceholderPanel("Giao diện Báo cáo Thống kê");

        cardPanel.add(pnlSanPham, "SanPham");
        cardPanel.add(pnlNhanVien, "NhanVien");
        cardPanel.add(pnlBaoCao, "BaoCao");

        Sidebar sidebarAdmin = new Sidebar(role, tenNV, menu -> cardLayout.show(cardPanel, menu));
        add(sidebarAdmin, BorderLayout.WEST);
        add(cardPanel, BorderLayout.CENTER);

        updateTableData();
    }

    private JPanel createSanPhamPanel(String role, String tenNV) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_WHITE);

        panel.add(createTopActionBar(role, tenNV), BorderLayout.NORTH);
        panel.add(createTableArea(), BorderLayout.CENTER);
        panel.add(createPaginationBar(), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createPlaceholderPanel(String text) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_WHITE);
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 24));
        label.setForeground(new Color(60, 42, 33));
        panel.add(label);
        return panel;
    }

    private JPanel createTopActionBar(String role, String tenNV) {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(COLOR_TOP_BG);
        topBar.setBorder(new EmptyBorder(15, 20, 15, 20));

        JPanel pnlLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlLeft.setOpaque(false);
        
        pnlLeft.add(createActionButton("Thêm"));
        pnlLeft.add(createActionButton("Sửa"));
        pnlLeft.add(createActionButton("Xóa"));

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlSearch.setOpaque(false);
        pnlSearch.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));

        JTextField txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(200, 40));
        txtSearch.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(120, 90, 80), 1),
                BorderFactory.createEmptyBorder(0, 10, 0, 10)
        ));

        JButton btnSearch = createActionButton("Tìm kiếm");
        btnSearch.setPreferredSize(new Dimension(100, 40));

        pnlSearch.add(txtSearch);
        pnlSearch.add(Box.createHorizontalStrut(10));
        pnlSearch.add(btnSearch);

        pnlLeft.add(pnlSearch);

        JPanel pnlInfo = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        pnlInfo.setOpaque(false);
        
        String roleDisplay = role.equals("ADMIN") ? "(Admin) " : "(Nhân viên) ";
        JLabel lblAdminInfo = new JLabel(roleDisplay + tenNV + " \uD83D\uDC64");
        
        lblAdminInfo.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblAdminInfo.setForeground(new Color(60, 42, 33));
        pnlInfo.add(lblAdminInfo);

        topBar.add(pnlLeft, BorderLayout.WEST);
        topBar.add(pnlInfo, BorderLayout.EAST);

        return topBar;
    }

    private JScrollPane createTableArea() {
        String[] columns = {"Loại món", "Mã món", "Tên món", "Nhóm thực đơn", "Đơn vị tính", "Giá"};

        model = new DefaultTableModel(null, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(45);
        table.setShowGrid(true);
        table.setGridColor(COLOR_WHITE);
        table.setBackground(COLOR_ROW_BG);
        table.setSelectionBackground(new Color(170, 200, 230));
        table.setFillsViewportHeight(true);

        table.getTableHeader().setPreferredSize(new Dimension(0, 45));
        
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setBackground(COLOR_HEADER_BOT);
                label.setForeground(COLOR_WHITE);
                label.setFont(new Font("SansSerif", Font.BOLD, 14));
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, COLOR_WHITE));
                return label;
            }
        };
        table.getTableHeader().setDefaultRenderer(headerRenderer);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(COLOR_WHITE);

        return scrollPane;
    }

    private JPanel createPaginationBar() {
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 12));
        bottomBar.setBackground(COLOR_HEADER_BOT);

        JButton btnFirst = createPageButton("<<");
        JButton btnPrev = createPageButton("<");
        
        JLabel lblTrang = new JLabel("Trang");
        lblTrang.setForeground(COLOR_WHITE);
        lblTrang.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        txtPage = new JTextField("1", 3);
        txtPage.setHorizontalAlignment(JTextField.CENTER);
        txtPage.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        lblTotal = new JLabel("trên " + totalPages + " trang");
        lblTotal.setForeground(COLOR_WHITE);
        lblTotal.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        JButton btnNext = createPageButton(">");
        JButton btnLast = createPageButton(">>");

        btnFirst.addActionListener(e -> { 
            currentPage = 1; 
            updateTableData(); 
        });
        
        btnPrev.addActionListener(e -> { 
            if (currentPage > 1) { 
                currentPage--; 
                updateTableData(); 
            } 
        });
        
        btnNext.addActionListener(e -> { 
            if (currentPage < totalPages) { 
                currentPage++; 
                updateTableData(); 
            } 
        });
        
        btnLast.addActionListener(e -> { 
            currentPage = totalPages; 
            updateTableData(); 
        });
        
        txtPage.addActionListener(e -> {
            try {
                int p = Integer.parseInt(txtPage.getText());
                if (p >= 1 && p <= totalPages) {
                    currentPage = p;
                    updateTableData();
                } else {
                    txtPage.setText(String.valueOf(currentPage));
                }
            } catch (NumberFormatException ex) {
                txtPage.setText(String.valueOf(currentPage));
            }
        });

        bottomBar.add(btnFirst);
        bottomBar.add(btnPrev);
        bottomBar.add(lblTrang);
        bottomBar.add(txtPage);
        bottomBar.add(lblTotal);
        bottomBar.add(btnNext);
        bottomBar.add(btnLast);

        return bottomBar;
    }

    private void updateTableData() {
        model.setRowCount(0);
        int start = (currentPage - 1) * rowsPerPage;
        int end = Math.min(start + rowsPerPage, allData.size());
        
        for (int i = start; i < end; i++) {
            model.addRow(allData.get(i));
        }
        
        if (txtPage != null) {
            txtPage.setText(String.valueOf(currentPage));
        }
        if (lblTotal != null) {
            lblTotal.setText("trên " + totalPages + " trang");
        }
    }

    private JButton createActionButton(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(100, 40));
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setBackground(COLOR_WHITE);
        btn.setForeground(new Color(60, 42, 33));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createLineBorder(new Color(120, 90, 80), 1));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }	

    private JButton createPageButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 16));
        btn.setForeground(COLOR_WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } 
        catch (Exception e) {}
        
        SwingUtilities.invokeLater(() -> new QuanLyForm("ADMIN", "Usertest").setVisible(true)); 
    }
}