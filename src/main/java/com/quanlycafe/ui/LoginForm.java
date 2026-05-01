package com.quanlycafe.ui;

import javax.swing.*;
import java.awt.*;

public class LoginForm extends JInternalFrame {

    public LoginForm() {
        super("Login Form", true, true, true, true);

        setSize(1200, 500);
        setLayout(new BorderLayout());

        JPanel pLeft = new JPanel();
        pLeft.setPreferredSize(new Dimension(450, 500));
        pLeft.setLayout(new BorderLayout());

        ImageIcon imgIcon = new ImageIcon("");
        Image image = imgIcon.getImage().getScaledInstance(450, 500, Image.SCALE_SMOOTH);
        JLabel lblImage = new JLabel(new ImageIcon());
        pLeft.add(lblImage);
    }
}
