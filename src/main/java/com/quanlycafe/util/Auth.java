package com.quanlycafe.util;

import com.quanlycafe.entity.NhanVien;

public class Auth {
    public static NhanVien user = null;

    public static void clear() {
        user = null;
    }

    public static boolean isLoggedIn() {
        return user != null;
    }
}