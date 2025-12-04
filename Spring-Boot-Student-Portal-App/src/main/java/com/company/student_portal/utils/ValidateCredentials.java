package com.company.student_portal.utils;

import org.springframework.stereotype.Component;

@Component
public class ValidateCredentials {
    public boolean isPasswordValid(String password) {
        if (password.length() < 8) return false;
        if (!password.matches(".*[A-Z].*")) return false;
        if (!password.matches(".*[a-z].*")) return false;
        if (!password.matches(".*\\d.*")) return false;
        return true;
    }

    public boolean isUsernameValid(String username) {
        return username.length() >= 8;
    }

}
