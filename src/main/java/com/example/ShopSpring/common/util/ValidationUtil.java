package com.example.ShopSpring.common.util;

import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;

public class ValidationUtil {
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);

        return email != null && pattern.matcher(email).matches();
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        String phoneRegex = "^[0-9]{10,11}$";
        Pattern pattern = Pattern.compile(phoneRegex);

        return phoneNumber!= null && pattern.matcher(phoneRegex).matches();
    }

    public static boolean isValidPassword(String password){
        return  password != null && password.length() >= 8;
    }
}
