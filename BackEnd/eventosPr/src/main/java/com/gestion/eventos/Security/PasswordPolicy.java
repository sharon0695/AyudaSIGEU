package com.gestion.eventos.Security;

public final class PasswordPolicy {
    private PasswordPolicy() {}

    public static boolean isValid(String password) {
        if (password == null) return false;
        int len = password.length();
        if (len < 8 || len > 64) return false;
        boolean hasUpper = false, hasLower = false, hasDigit = false, hasSpecial = false;
        for (int i = 0; i < len; i++) {
            char c = password.charAt(i);
            if (Character.isWhitespace(c)) return false;
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else hasSpecial = true;
        }
        return hasUpper && hasLower && hasDigit && hasSpecial;
    }

    public static String requirementsMessage() {
        return "La contraseña debe tener entre 8 y 64 caracteres, incluir al menos una mayúscula, una minúscula, un número y un carácter especial, y no contener espacios";
    }
}
