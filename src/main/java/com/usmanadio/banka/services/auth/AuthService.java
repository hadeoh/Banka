package com.usmanadio.banka.services.auth;

import com.usmanadio.banka.models.user.User;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

public interface AuthService {
    void createUser(User user);
    void verifyUser(String token);
    void signInUser(String email, String password, HttpServletResponse response);
    void resetPassword(String email);
    void setNewPassword(UUID id, String newPassword, String token);
}
