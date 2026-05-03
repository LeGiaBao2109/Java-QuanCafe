package com.quanlycafe.ui;

import com.quanlycafe.dao.SanPhamDAO;
import com.quanlycafe.ui.component.Sidebar;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class BanHangModernForm extends JFrame {

    private final Color COLOR_BG = new Color(253, 248, 245);
    private final Color COLOR_WHITE = Color.WHITE;
    private final Color COLOR_BORDER = new Color(222, 204, 190);
    private final Color COLOR_PRIMARY_DARK = new Color(92, 64, 51);
    private final Color COLOR_PRIMARY_LIGHT = new Color(188, 143, 111);
    private final Color COLOR_TEXT_MAIN = new Color(60, 42, 33);

    private JPanel rightCartPanel; 
    private JPanel gridPanel;      
    private JSplitPane splitPane;  
    
    private JTable table; 
    private DefaultTableModel cartTableModel; 
    private JLabel lblTamTinh, lblTongTien;   
    
    private List<JButton> tabButtons = new ArrayList<>(); 
    private DecimalFormat formatter = new DecimalFormat("#,###đ"); 

    public BanHangModernForm(String role, String tenNV) {
        setTitle("BaristaPro - Phần mềm Quản lý Bán hàng");
        setSize(1366, 768); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        Sidebar sidebar = new Sidebar(role, tenNV, null); 
        add(sidebar, BorderLayout.WEST);

        JPanel mainContentPanel = new JPanel(new BorderLayout());

        mainContentPanel.add(createModernHeader(), BorderLayout.NORTH);

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(createLeftMenuPanel());
        
        rightCartPanel = createRightCartPanel();
        splitPane.setRightComponent(rightCartPanel);
        
        splitPane.setDividerLocation(800); 
        splitPane.setResizeWeight(0.7);
        splitPane.setBorder(null); 
        
        rightCartPanel.setVisible(false);
        
        mainContentPanel.add(splitPane, BorderLayout.CENTER);
        add(mainContentPanel, BorderLayout.CENTER);
    }

    private void loadProducts(String maDM) {
        gridPanel.removeAll(); 

        SanPhamDAO dao = new SanPhamDAO();
        List<Object[]> dsSanPham = dao.laySanPhamBanHangPOS(maDM);

        for (Object[] sp : dsSanPham) {
            String tenSP = sp[0].toString();
            int gia = Integer.parseInt(sp[1].toString());
            gridPanel.add(createProductCard(tenSP, gia));
        }

        gridPanel.revalidate(); 
        gridPanel.repaint();
    }

    private JPanel createLeftMenuPanel() {
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(COLOR_BG);

        JPanel topArea = new JPanel(new BorderLayout());
        topArea.setBackground(COLOR_WHITE);
        topArea.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER));

        gridPanel = new JPanel(new GridLayout(0, 4, 15, 15)); 
        gridPanel.setOpaque(false);
        gridPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel tabMenu = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        tabMenu.setOpaque(false);
        
        SanPhamDAO dao = new SanPhamDAO();
        List<Object[]> dsDanhMuc = dao.layDanhSachDanhMuc();

        boolean isFirst = true;
        for (Object[] dm : dsDanhMuc) {
            String maDM = dm[0].toString();
            String tenDM = dm[1].toString();
            
            tabMenu.add(createTabButton(tenDM, maDM, isFirst));
            
            if (isFirst) {
                loadProducts(maDM);
                isFirst = false;
            }
        }

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        searchPanel.setOpaque(false);
        JTextField txtSearch = new JTextField("🔍 Nhập mã/Tên món cần tìm...", 30);
        txtSearch.setPreferredSize(new Dimension(0, 35));
        searchPanel.add(txtSearch);

        topArea.add(tabMenu, BorderLayout.NORTH);
        topArea.add(searchPanel, BorderLayout.CENTER);

        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setOpaque(false);
        wrapperPanel.add(gridPanel, BorderLayout.NORTH);

        JScrollPane scrollMenu = new JScrollPane(wrapperPanel);
        scrollMenu.setBorder(null);
        scrollMenu.setOpaque(false);
        scrollMenu.getViewport().setOpaque(false);
        scrollMenu.getVerticalScrollBar().setUnitIncrement(16);

        leftPanel.add(topArea, BorderLayout.NORTH);
        leftPanel.add(scrollMenu, BorderLayout.CENTER);
        return leftPanel;
    }

    private void addToCart(String name, int price) {
        if (!rightCartPanel.isVisible()) {
            rightCartPanel.setVisible(true);
            splitPane.setDividerLocation(800); 
        }

        cartTableModel.addRow(new Object[]{name, 1, formatter.format(price), price});
        
        int newRowIdx = cartTableModel.getRowCount() - 1;
        table.setRowSelectionInterval(newRowIdx, newRowIdx);

        recalculateTotal();
    }

    private void recalculateTotal() {
        int total = 0;
        for (int i = 0; i < cartTableModel.getRowCount(); i++) {
            int qty = Integer.parseInt(cartTableModel.getValueAt(i, 1).toString());
            int unitPrice = (int) cartTableModel.getValueAt(i, 3); 
            total += (qty * unitPrice);
        }
        
        lblTamTinh.setText(formatter.format(total));
        lblTongTien.setText(formatter.format(total)); 
        
        if (cartTableModel.getRowCount() == 0) {
            rightCartPanel.setVisible(false);
            lblTamTinh.setText("0đ");
            lblTongTien.setText("0đ");
        }
    }

    private JPanel createRightCartPanel() {
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(COLOR_WHITE);
        rightPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, COLOR_BORDER)); 

        JPanel cartHeader = new JPanel(new BorderLayout());
        cartHeader.setBackground(COLOR_WHITE);
        cartHeader.setBorder(new EmptyBorder(15, 15, 15, 15));
        JLabel lblTable = new JLabel("Giỏ hàng");
        lblTable.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTable.setForeground(COLOR_TEXT_MAIN);
        cartHeader.add(lblTable, BorderLayout.NORTH);

        String[] columnNames = {"Tên món", "SL", "Thành tiền", "UnitPrice"};
        cartTableModel = new DefaultTableModel(null, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        table = new JTable(cartTableModel);
        table.setRowHeight(40);
        table.setShowGrid(true);
        table.setGridColor(COLOR_BORDER);
        table.getTableHeader().setBackground(COLOR_BG);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);

        table.getColumnModel().removeColumn(table.getColumnModel().getColumn(3));

        JScrollPane scrollTable = new JScrollPane(table);
        scrollTable.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, COLOR_BORDER));
        
        table.setFillsViewportHeight(true);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (table.rowAtPoint(e.getPoint()) == -1) {
                    table.clearSelection();
                }
            }
        });

        scrollTable.getViewport().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                table.clearSelection();
            }
        });

        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        controlsPanel.setBackground(COLOR_WHITE);
        
        JButton btnGiam = new JButton(" - ");
        JButton btnTang = new JButton(" + ");
        JButton btnXoa = new JButton("Xóa món");
        
        styleControlButton(btnGiam);
        styleControlButton(btnTang);
        styleControlButton(btnXoa);
        btnXoa.setForeground(Color.RED); 

        controlsPanel.add(btnXoa);
        controlsPanel.add(btnGiam);
        controlsPanel.add(btnTang);

        btnXoa.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                cartTableModel.removeRow(row);
                recalculateTotal();
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một món trong bảng để xóa!");
            }
        });

        btnTang.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int qty = Integer.parseInt(cartTableModel.getValueAt(row, 1).toString());
                int price = (int) cartTableModel.getValueAt(row, 3);
                qty++;
                cartTableModel.setValueAt(qty, row, 1);
                cartTableModel.setValueAt(formatter.format(qty * price), row, 2);
                recalculateTotal();
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng để tăng số lượng!");
            }
        });

        btnGiam.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int qty = Integer.parseInt(cartTableModel.getValueAt(row, 1).toString());
                int price = (int) cartTableModel.getValueAt(row, 3);
                qty--;
                if (qty <= 0) {
                    cartTableModel.removeRow(row); 
                } else {
                    cartTableModel.setValueAt(qty, row, 1);
                    cartTableModel.setValueAt(formatter.format(qty * price), row, 2);
                }
                recalculateTotal();
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng để giảm số lượng!");
            }
        });

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(COLOR_WHITE);
        bottomPanel.setBorder(new EmptyBorder(0, 15, 15, 15));

        JPanel calcPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        calcPanel.setOpaque(false);
        
        lblTamTinh = new JLabel("0đ");
        lblTongTien = new JLabel("0đ");
        lblTongTien.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTongTien.setForeground(COLOR_PRIMARY_DARK);

        calcPanel.add(createRowPanel("Tạm tính:", lblTamTinh, false));
        calcPanel.add(createRowPanel("Giảm giá:", new JLabel("0đ"), false));
        calcPanel.add(createRowPanel("Tổng thanh toán:", lblTongTien, true)); 

        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(new EmptyBorder(15, 0, 0, 0));
        
        JButton btnHuy = new JButton("Hủy đơn");
        btnHuy.setPreferredSize(new Dimension(0, 50));
        btnHuy.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnHuy.setBackground(COLOR_BG);
        btnHuy.setForeground(COLOR_TEXT_MAIN);
        
        btnHuy.addActionListener(e -> {
            cartTableModel.setRowCount(0); 
            recalculateTotal(); 
        });

        JButton btnThanhToan = new JButton("Thanh toán");
        btnThanhToan.setBackground(COLOR_PRIMARY_DARK); 
        btnThanhToan.setForeground(COLOR_WHITE);
        btnThanhToan.setFont(new Font("SansSerif", Font.BOLD, 16));

        btnPanel.add(btnHuy);
        btnPanel.add(btnThanhToan);

        bottomPanel.add(calcPanel, BorderLayout.NORTH);
        bottomPanel.add(btnPanel, BorderLayout.SOUTH);

        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.add(scrollTable, BorderLayout.CENTER);
        centerWrapper.add(controlsPanel, BorderLayout.SOUTH); 

        rightPanel.add(cartHeader, BorderLayout.NORTH);
        rightPanel.add(centerWrapper, BorderLayout.CENTER);
        rightPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        return rightPanel;
    }
    
    private void styleControlButton(JButton btn) {
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBackground(COLOR_BG);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private JPanel createProductCard(String name, int price) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(COLOR_WHITE);
        card.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1)); 
        card.setCursor(new Cursor(Cursor.HAND_CURSOR)); 

        JLabel lblImg = new JLabel("Ảnh", SwingConstants.CENTER);
        lblImg.setPreferredSize(new Dimension(100, 120));
        lblImg.setBackground(new Color(250, 242, 235)); 
        lblImg.setOpaque(true);

        JPanel info = new JPanel(new BorderLayout());
        info.setBackground(COLOR_WHITE);
        info.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JLabel lblName = new JLabel(name);
        lblName.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblName.setForeground(COLOR_TEXT_MAIN);
        
        JLabel lblPrice = new JLabel(formatter.format(price));
        lblPrice.setForeground(COLOR_PRIMARY_LIGHT); 

        info.add(lblName, BorderLayout.WEST);
        info.add(lblPrice, BorderLayout.EAST);

        card.add(lblImg, BorderLayout.CENTER);
        card.add(info, BorderLayout.SOUTH);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                addToCart(name, price);
            }
        });

        return card;
    }

    private JButton createTabButton(String title, String maDM, boolean isActive) {
        JButton btn = new JButton(title);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(120, 45));
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        if (isActive) {
            styleActiveTab(btn);
        } else {
            styleInactiveTab(btn);
        }
        
        tabButtons.add(btn); 

        btn.addActionListener(e -> {
            for (JButton t : tabButtons) {
                styleInactiveTab(t);
            }
            styleActiveTab(btn);
            loadProducts(maDM);
        });

        return btn;
    }

    private void styleActiveTab(JButton btn) {
        btn.setForeground(COLOR_PRIMARY_DARK); 
        btn.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, COLOR_PRIMARY_DARK)); 
        btn.setBorderPainted(true);
    }

    private void styleInactiveTab(JButton btn) {
        btn.setForeground(new Color(120, 100, 90)); 
        btn.setBorderPainted(false);
    }

    private JPanel createModernHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COLOR_WHITE);
        header.setPreferredSize(new Dimension(0, 65));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER));

        JPanel leftHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        leftHeader.setOpaque(false);

        header.add(leftHeader, BorderLayout.WEST);
        return header;
    }

    private JPanel createRowPanel(String text, JLabel rightLabel, boolean isBold) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        JLabel lblLeft = new JLabel(text);
        
        if (isBold) {
            lblLeft.setFont(new Font("SansSerif", Font.BOLD, 16));
            lblLeft.setForeground(COLOR_TEXT_MAIN);
        } else {
            lblLeft.setForeground(new Color(120, 100, 90));
        }
        
        panel.add(lblLeft, BorderLayout.WEST);
        panel.add(rightLabel, BorderLayout.EAST);
        return panel;
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } 
        catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new BanHangModernForm("STAFF", "Test User").setVisible(true));
    }
}