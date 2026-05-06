package com.quanlycafe.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.quanlycafe.dao.NhanVienDAO;
import com.quanlycafe.dao.SanPhamDAO;

public class NhanVienPanel extends JPanel{
	 private final Color COLOR_BG = new Color(253, 248, 245);
	    private final Color COLOR_SURFACE = Color.WHITE;
	    private final Color COLOR_PRIMARY = new Color(92, 64, 51);
	    private final Color COLOR_BORDER = new Color(230, 220, 210);
	    private final Color COLOR_TABLE_HEADER = new Color(245, 240, 235);
	    private final Color COLOR_TABLE_ROW_ALT = new Color(250, 248, 246);

	    private List<Object[]> originalData; 
	    private List<Object[]> displayData;
//	    private List<Object[]> allData;
	    private int currentPage = 1;
	    private int rowsPerPage = 10;
	    private int totalPages = 1;

	    
	    
	    private DefaultTableModel model;
	    private JTable table;
	    private JLabel lblTotal;
	    private JTextField txtPage;
	    private JTextField txtSearch;
	    
	    private JButton btnAdd;
	    private JButton btnEdit;
	    private JButton btnDelete;
	    
	    
	    
	    public NhanVienPanel(String role, String tenNV) {
	        setLayout(new BorderLayout(0, 15));
	        setBackground(COLOR_BG);
	        setOpaque(false);

	        

	        JPanel tableContainer = new JPanel(new BorderLayout());
	        tableContainer.setBackground(COLOR_SURFACE);
	        tableContainer.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));
	        
	        tableContainer.add(createTableArea(), BorderLayout.CENTER);
	        tableContainer.add(createPaginationBar(), BorderLayout.SOUTH);

	        add(createTopActionBar(role, tenNV), BorderLayout.NORTH);
	        add(tableContainer, BorderLayout.CENTER);

	        table.getSelectionModel().addListSelectionListener(e -> {
	            if (!e.getValueIsAdjusting()) {
	                boolean isSelected = table.getSelectedRow() != -1;
	                toggleButtonState(btnEdit, isSelected, COLOR_PRIMARY);
	                toggleButtonState(btnDelete, isSelected, new Color(220, 53, 69)); 
	            }
	        });
	        loadAndFormatData(); 
	        updateTableData();
	    }
	    private void loadAndFormatData() {
	        NhanVienDAO dao = new NhanVienDAO();
	        List<Object[]> rawData = dao.layDanhSachNhanVienQuanLy();
	        originalData = new ArrayList<>();

	        for (Object[] row : rawData) {
	            String maNV = row[0].toString();
	            String maTK = row[1].toString();
	            String tenDangNhap = row[2].toString();
	            String matkhau = row[3].toString();
	            String tenNV = row[4].toString(); 
	            String role = row[5].toString(); 
	            String dienthoai = row[6].toString(); 

	            

	            Object[] formattedRow = new Object[]{maNV, maTK, tenDangNhap, matkhau, tenNV, role, dienthoai};
	            originalData.add(formattedRow);
	        }

	        displayData = new ArrayList<>(originalData);
	        calculatePagination();
	        
	        
	    }

	    private void calculatePagination() {
	        totalPages = (int) Math.ceil((double) displayData.size() / rowsPerPage);
	        if (totalPages == 0) totalPages = 1;
	        if (currentPage > totalPages) currentPage = totalPages;
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
	        
	        btnAdd = createModernButton("Thêm Mới", true);
	        btnEdit = createModernButton("Sửa", false); 
	        btnDelete = createModernButton("Xóa", false); 
	        
	        btnAdd.addActionListener(e -> openCrudDialog("ADD"));
	        btnEdit.addActionListener(e -> openCrudDialog("EDIT"));
	        btnDelete.addActionListener(e -> openCrudDialog("DELETE"));

	        pnlLeft.add(btnAdd);
	        pnlLeft.add(btnEdit);
	        pnlLeft.add(btnDelete);

	        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
	        pnlSearch.setOpaque(false);
	        pnlSearch.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 0));

	        txtSearch = new JTextField();
	        txtSearch.setPreferredSize(new Dimension(250, 38));
	        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
	        txtSearch.setBorder(BorderFactory.createCompoundBorder(
	                BorderFactory.createLineBorder(COLOR_BORDER, 1),
	                BorderFactory.createEmptyBorder(0, 10, 0, 10)
	        ));
	        txtSearch.setToolTipText("Nhập mã hoặc tên nhân viên...");

	        JButton btnSearch = createModernButton("Tìm Kiếm", false);
	        btnSearch.setPreferredSize(new Dimension(100, 38));
	        
	        btnSearch.addActionListener(e -> performSearch(txtSearch.getText()));
	        txtSearch.addActionListener(e -> performSearch(txtSearch.getText()));

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
	    private void performSearch(String keyword) {
	        String lowerKeyword = keyword.trim().toLowerCase();
	        
	        if (lowerKeyword.isEmpty()) {
	            displayData = new ArrayList<>(originalData);
	        } else {
	            displayData = new ArrayList<>();
	            for (Object[] row : originalData) {
	                String maNV = row[0].toString().toLowerCase();
	                String tenNV = row[1].toString().toLowerCase();
	                
	                if (maNV.contains(lowerKeyword) || tenNV.contains(lowerKeyword)) {
	                    displayData.add(row);
	                }
	            }
	        }
	        
	        currentPage = 1; 
	        calculatePagination();
	        updateTableData();
	    }
	    private void toggleButtonState(JButton btn, boolean isActive, Color activeColor) {
	        if (isActive) {
	            btn.setBackground(activeColor);
	            btn.setForeground(Color.WHITE);
	            btn.setBorder(BorderFactory.createEmptyBorder());
	        } else {
	            btn.setBackground(COLOR_SURFACE);
	            btn.setForeground(COLOR_PRIMARY);
	            btn.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));
	        }
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
	            rowData = displayData.get(actualRow); 
	        }

	        CrudNhanVienDialog dialog = new CrudNhanVienDialog(owner, mode, rowData);
	        dialog.setVisible(true);

	        if (dialog.isSuccess()) {
	            // Khi thêm/sửa/xóa thành công -> Tải lại dữ liệu từ CSDL
	            loadAndFormatData();
	            // Lọc lại dữ liệu trên bảng theo ô tìm kiếm (nếu người dùng đang tìm kiếm dở)
	            performSearch(txtSearch.getText()); 
	        }
	    }
	    
	    private JScrollPane createTableArea() {
	        String[] columns = {"Mã nhân viên", "Tên đăng nhập", "Tên nhân viên", "Role", "Số điện thoại"};

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
	        int end = Math.min(start + rowsPerPage, displayData.size());
	        
	        for (int i = start; i < end; i++) {
	            model.addRow(displayData.get(i));
	        }
	        
	        if (txtPage != null) txtPage.setText(String.valueOf(currentPage));
	        if (lblTotal != null) lblTotal.setText("/ " + totalPages);
	        if (table != null) table.clearSelection();
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
