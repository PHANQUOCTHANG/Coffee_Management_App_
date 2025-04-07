package com.example.javafxapp.Utils;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordUtils {
    // Mã hóa mật khẩu
    public static String hashPassword(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray()); // 12 là độ mạnh của bcrypt
    }

    // Kiểm tra mật khẩu nhập vào có khớp với mật khẩu đã mã hóa không
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.verifyer().verify(plainPassword.toCharArray(), hashedPassword).verified;
    }


}
