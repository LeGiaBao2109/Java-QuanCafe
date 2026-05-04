package com.quanlycafe.ui;

import com.quanlycafe.dao.SanPhamDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SanPhamPanel extends JPanel {

    private final Color COLOR_BG = new Color(253, 248, 245);
    private final Color COLOR_SURFACE = Color.WHITE;
    private final Color COLOR_PRIMARY = new Color(92, 64, 51);
    private final Color COLOR_BORDER = new Color(230, 220, 210);
    private final Color COLOR_TABLE_HEADER = new Color(245, 240, 235);
    private final Color COLOR_TABLE_ROW_ALT = new Color(250, 248, 246);

    private List<Object[]> allData;
    private int currentPage = 1;
    private int rowsPerPage = 10;
    private int totalPages = 1;

    private DefaultTableModel model;
    private JTable table;
    private JLabel lblTotal;
    private JTextField txtPage;

    public SanPhamPanel(String role, String tenNV) {
        setLayout(new BorderLayout(0, 15));
        setBackground(COLOR_BG);
        setOpaque(false);

        loadAndFormatData(); 

        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(COLOR_SURFACE);
        tableContainer.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));
        
        tableContainer.add(createTableArea(), BorderLayout.CENTER);
        tableContainer.add(createPaginationBar(), BorderLayout.SOUTH);

        add(createTopActionBar(role, tenNV), BorderLayout.NORTH);
        add(tableContainer, BorderLayout.CENTER);

        updateTableData();
    }

    private void loadAndFormatData() {
        SanPhamDAO dao = new SanPhamDAO();
        List<Object[]> rawData = dao.layDanhSachSanPhamQuanLy();
        allData = new ArrayList<>();

        for (Object[] row : rawData) {
            String maMon = row[1].toString(); 
            String tenMonGoc = row[2].toString(); 
            String nhom = row[3].toString(); 
            String dvt = row[4].toString(); 
            String gia = row[5].toString(); 

            String tenMon = tenMonGoc;
            String size = "Không có";

            if (tenMonGoc.contains("(Size ")) {
                int openParen = tenMonGoc.indexOf("(Size ");
                int closeParen = tenMonGoc.indexOf(")", openParen);
                
                if (closeParen != -1) {
                    tenMon = tenMonGoc.substring(0, openParen).trim();
                    size = tenMonGoc.substring(openParen + 6, closeParen).trim();
                }
            }

            Object[] formattedRow = new Object[]{maMon, tenMon, size, nhom, dvt, gia};
            allData.add(formattedRow);
        }

        totalPages = (int) Math.ceil((double) allData.size() / rowsPerPage);
        if (totalPages == 0) totalPages = 1;
    }

    private JPanel createTopActionBar(String role, String tenNV) {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(COLOR_SURFACE);
        topBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDER, 1),
            new EmptyBorder(12, 20, 12, 20)
        ));

        JPanel pnlLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        pnlLeft.setOpaque(false);
        
        JButton btnAdd = createModernButton("Thêm Mới", true);
        JButton btnEdit = createModernButton("Sửa", false);
        JButton btnDelete = createModernButton("Xóa", false);

        btnAdd.addActionListener(e -> openCrudDialog("ADD"));
        btnEdit.addActionListener(e -> openCrudDialog("EDIT"));
        btnDelete.addActionListener(e -> openCrudDialog("DELETE"));

        pnlLeft.add(btnAdd);
        pnlLeft.add(btnEdit);
        pnlLeft.add(btnDelete);

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlSearch.setOpaque(false);
        pnlSearch.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 0));

        JTextField txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(250, 38));
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1),
                BorderFactory.createEmptyBorder(0, 10, 0, 10)
        ));

        JButton btnSearch = createModernButton("Tìm Kiếm", false);
        btnSearch.setPreferredSize(new Dimension(100, 38));

        pnlSearch.add(txtSearch);
        pnlSearch.add(Box.createHorizontalStrut(5));
        pnlSearch.add(btnSearch);

        pnlLeft.add(pnlSearch);

        JPanel pnlInfo = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        pnlInfo.setOpaque(false);
        
        String roleDisplay = role.equals("ADMIN") ? "Quản lý" : "Nhân viên";
        JLabel lblAdminInfo = new JLabel("<html><span style='color:gray;font-size:11px;'>" + roleDisplay + "</span><br>" + tenNV + " 👤</html>");
        lblAdminInfo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblAdminInfo.setForeground(COLOR_PRIMARY);
        pnlInfo.add(lblAdminInfo);

        topBar.add(pnlLeft, BorderLayout.WEST);
        topBar.add(pnlInfo, BorderLayout.EAST);

        return topBar;
    }

    private void openCrudDialog(String mode) {
        Window owner = SwingUtilities.getWindowAncestor(this);
        Object[] rowData = null;

        if (!mode.equals("ADD")) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng trên bảng để thao tác!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int actualRow = (currentPage - 1) * rowsPerPage + selectedRow;
            rowData = allData.get(actualRow);
        }

        CrudSanPhamDialog dialog = new CrudSanPhamDialog(owner, mode, rowData);
        dialog.setVisible(true);

        if (dialog.isSuccess()) {
            System.out.println("Thực hiện Database ở đây. Refresh lại bảng...");
        }
    }

    private JScrollPane createTableArea() {
        String[] columns = {"Mã món", "Tên món", "Size", "Nhóm thực đơn", "Đơn vị tính", "Giá"};

        model = new DefaultTableModel(null, columns) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(model);
        table.setRowHeight(48);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(240, 235, 230));
        table.setBackground(COLOR_SURFACE);
        table.setSelectionBackground(new Color(235, 225, 215));
        table.setSelectionForeground(COLOR_PRIMARY);
        table.setFillsViewportHeight(true);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.getTableHeader().setPreferredSize(new Dimension(0, 50));
        table.getTableHeader().setBackground(COLOR_TABLE_HEADER);
        
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setBackground(COLOR_TABLE_HEADER);
                label.setForeground(new Color(100, 80, 70));
                label.setFont(new Font("Segoe UI", Font.BOLD, 13));
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, COLOR_BORDER));
                return label;
            }
        };
        table.getTableHeader().setDefaultRenderer(headerRenderer);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? COLOR_SURFACE : COLOR_TABLE_ROW_ALT);
                }
                return c;
            }
        };
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(COLOR_SURFACE);

        return scrollPane;
    }

    private JPanel createPaginationBar() {
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        bottomBar.setBackground(COLOR_SURFACE);
        bottomBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, COLOR_BORDER));

        JButton btnFirst = createPageButton("«");
        JButton btnPrev = createPageButton("‹");
        
        txtPage = new JTextField("1", 3);
        txtPage.setHorizontalAlignment(JTextField.CENTER);
        txtPage.setFont(new Font("Segoe UI", Font.BOLD, 13));
        txtPage.setBorder(BorderFactory.createLineBorder(COLOR_BORDER));
        
        lblTotal = new JLabel("/ " + totalPages);
        lblTotal.setForeground(Color.GRAY);
        lblTotal.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        JButton btnNext = createPageButton("›");
        JButton btnLast = createPageButton("»");

        btnFirst.addActionListener(e -> { currentPage = 1; updateTableData(); });
        btnPrev.addActionListener(e -> { if (currentPage > 1) { currentPage--; updateTableData(); } });
        btnNext.addActionListener(e -> { if (currentPage < totalPages) { currentPage++; updateTableData(); } });
        btnLast.addActionListener(e -> { currentPage = totalPages; updateTableData(); });
        
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
        
        if (txtPage != null) txtPage.setText(String.valueOf(currentPage));
        if (lblTotal != null) lblTotal.setText("/ " + totalPages);
    }

    private JButton createModernButton(String text, boolean isPrimary) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(110, 38));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (isPrimary) {
            btn.setBackground(COLOR_PRIMARY);
            btn.setForeground(Color.WHITE);
            btn.setBorder(BorderFactory.createEmptyBorder());
        } else {
            btn.setBackground(COLOR_SURFACE);
            btn.setForeground(COLOR_PRIMARY);
            btn.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));
        }
        return btn;
    }

    private JButton createPageButton(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(35, 30));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setForeground(COLOR_PRIMARY);
        btn.setBackground(COLOR_SURFACE);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}