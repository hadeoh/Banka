package com.usmanadio.banka.services.auth;

import com.usmanadio.banka.models.user.User;

public interface AuthService {
    void createUser(User user);
    void verifyUser(String token);
    String signInUser(String email, String password);
}
