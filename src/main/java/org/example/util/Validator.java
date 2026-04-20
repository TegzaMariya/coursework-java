package org.example.util;

public class Validator {
    private Validator() {
    }

    public static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean isValidEmail(String email) {
        if (isEmpty(email)) return true;
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    public static boolean isValidPhone(String phone) {
        if (isEmpty(phone)) return true;
        return phone.matches("^[+0-9()\\-\\s]{5,20}$");
    }

    public static boolean isValidUrl(String url) {
        if (isEmpty(url)) return true;
        return url.startsWith("http://") || url.startsWith("https://");
    }
}
