package com.quanlycafe;

import com.quanlycafe.ui.LoginDialog1;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }		

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                LoginDialog1 login = new LoginDialog1();
                login.setLocationRelativeTo(null);
                login.setVisible(true);
            }
        });
    }
}