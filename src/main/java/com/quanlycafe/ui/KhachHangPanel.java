package com.quanlycafe.ui;

import com.quanlycafe.dao.KhachHangDAO;
import com.quanlycafe.entity.KhachHang;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class KhachHangPanel extends JPanel {

    private final Color COLOR_BG = new Color(253, 248, 245);
    private final Color COLOR_SURFACE = Color.WHITE;
    private final Color COLOR_PRIMARY = new Color(92, 64, 51);
    private final Color COLOR_BORDER = new Color(230, 220, 210);
    private final Color COLOR_TABLE_HEADER = new Color(245, 240, 235);
    private final Color COLOR_TABLE_ROW_ALT = new Color(250, 248, 246);

    private List<Object[]> allData;
    private List<Object[]> filteredData;
    private int currentPage = 1;
    private int rowsPerPage = 10;
    private int totalPages = 1;

    private DefaultTableModel model;
    private JTable table;
    private JLabel lblTotal;
    private JTextField txtPage;
    private JTextField txtSearch;

    public KhachHangPanel() {
        setLayout(new BorderLayout(0, 15));
        setBackground(COLOR_BG);
        setOpaque(false);

        loadData();

        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(COLOR_SURFACE);
        tableContainer.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));

        tableContainer.add(createTableArea(), BorderLayout.CENTER);
        tableContainer.add(createPaginationBar(), BorderLayout.SOUTH);

        add(createTopActionBar(), BorderLayout.NORTH);
        add(tableContainer, BorderLayout.CENTER);

        updateTableData();
    }

    private void loadData() {
        allData = new ArrayList<>();
        KhachHangDAO dao = new KhachHangDAO();
        List<KhachHang> dsKhachHang = dao.layTatCaKhachHang();

        if (dsKhachHang != null) {
            for (KhachHang kh : dsKhachHang) {
                if (kh.getSdt() != null && kh.getSdt().equals("0000000000")) {
                    continue;
                }
                allData.add(new Object[]{
                        kh.getMaKH(),
                        kh.getTenKH(),
                        kh.getSdt(),
                        kh.getDiemTL()
                });
            }
        }
        filteredData = new ArrayList<>(allData);
        calculateTotalPages();
    }

    private void calculateTotalPages() {
        totalPages = (int) Math.ceil((double) filteredData.size() / rowsPerPage);
        if (totalPages == 0) totalPages = 1;
    }

    private void filterData(String keyword) {
        filteredData.clear();
        String searchKey = keyword.toLowerCase().trim();

        for (Object[] row : allData) {
            String maKH = row[0].toString().toLowerCase();
            String tenKH = row[1].toString().toLowerCase();
            String sdt = row[2].toString().toLowerCase();

            if (maKH.contains(searchKey) || tenKH.contains(searchKey) || sdt.contains(searchKey)) {
                filteredData.add(row);
            }
        }

        currentPage = 1;
        calculateTotalPages();
        updateTableData();
    }

    private void showEditDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maKH = table.getValueAt(selectedRow, 0).toString();
        String tenHT = table.getValueAt(selectedRow, 1).toString();
        String sdtHT = table.getValueAt(selectedRow, 2).toString();

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        JTextField txtTenMoi = new JTextField(tenHT);
        JTextField txtSdtMoi = new JTextField(sdtHT);

        panel.add(new JLabel("Tên khách hàng:"));
        panel.add(txtTenMoi);
        panel.add(new JLabel("Số điện thoại:"));
        panel.add(txtSdtMoi);

        int result = JOptionPane.showConfirmDialog(this, panel, "Sửa thông tin: " + maKH, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String tenMoi = txtTenMoi.getText().trim();
            String sdtMoi = txtSdtMoi.getText().trim();

            if (tenMoi.isEmpty() || sdtMoi.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không được để trống thông tin!");
                return;
            }

            KhachHangDAO dao = new KhachHangDAO();
            if (dao.capNhatKhachHang(maKH, tenMoi, sdtMoi)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                reloadAllData();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
            }
        }
    }

    private void reloadAllData() {
        txtSearch.setText("");
        loadData();
        currentPage = 1;
        updateTableData();
    }

    private JPanel createTopActionBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(COLOR_SURFACE);
        topBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1),
                new EmptyBorder(12, 20, 12, 20)
        ));

        JPanel pnlLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        pnlLeft.setOpaque(false);

        JButton btnEdit = createModernButton("✎ Sửa thông tin", false);
        btnEdit.addActionListener(e -> showEditDialog());
        pnlLeft.add(btnEdit);

        JButton btnReload = createModernButton("↻ Làm mới", false);
        btnReload.setPreferredSize(new Dimension(120, 38));
        btnReload.addActionListener(e -> reloadAllData());
        pnlLeft.add(btnReload);

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlSearch.setOpaque(false);
        pnlSearch.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 0));

        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(350, 38));
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1),
                BorderFactory.createEmptyBorder(0, 10, 0, 10)
        ));

        JButton btnSearch = createModernButton("Tìm Kiếm", false);
        btnSearch.setPreferredSize(new Dimension(100, 38));

        btnSearch.addActionListener(e -> filterData(txtSearch.getText()));
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    filterData(txtSearch.getText());
                }
            }
        });

        pnlSearch.add(txtSearch);
        pnlSearch.add(Box.createHorizontalStrut(5));
        pnlSearch.add(btnSearch);

        pnlLeft.add(pnlSearch);
        topBar.add(pnlLeft, BorderLayout.WEST);

        return topBar;
    }

    private JScrollPane createTableArea() {
        String[] columns = {"Mã KH", "Tên Khách Hàng", "Số Điện Thoại", "Điểm Tích Lũy"};
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

        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(300);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);

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
        int end = Math.min(start + rowsPerPage, filteredData.size());

        for (int i = start; i < end; i++) {
            model.addRow(filteredData.get(i));
        }

        if (txtPage != null) txtPage.setText(String.valueOf(currentPage));
        if (lblTotal != null) lblTotal.setText("/ " + totalPages);
    }

    private JButton createModernButton(String text, boolean isPrimary) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(140, 38));
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