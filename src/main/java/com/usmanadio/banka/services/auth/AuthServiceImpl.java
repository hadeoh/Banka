package com.usmanadio.banka.services.auth;

import com.usmanadio.banka.exceptions.CustomException;
import com.usmanadio.banka.models.user.EmailVerificationStatus;
import com.usmanadio.banka.models.user.Role;
import com.usmanadio.banka.models.user.User;
import com.usmanadio.banka.repositories.UserRepository;
import com.usmanadio.banka.security.JwtTokenProvider;
import com.usmanadio.banka.services.utils.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthServiceImpl implements AuthService {

    private UserRepository userRepository;
    private JwtTokenProvider jwtTokenProvider;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private EmailSender emailSender;
    private AuthenticationManager authenticationManager;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, JwtTokenProvider jwtTokenProvider,
                           BCryptPasswordEncoder bCryptPasswordEncoder, EmailSender emailSender,
                           AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.emailSender = emailSender;
        this.authenticationManager = authenticationManager;
    }

    public void createUser(User user) {
        if (userRepository.existsByEmailOrPhoneNumber(user.getEmail(), user.getPhoneNumber())) {
            throw new CustomException("Email or phone number already exists", HttpStatus.CONFLICT);
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singletonList(Role.ROLE_CLIENT));
        String token = jwtTokenProvider.generateToken(user.getEmail());
        user.setEmailVerificationToken(token);
        userRepository.save(user);
        String url = "http://localhost:3000/verify-email/" + token;
        String message =
                "Hey" + user.getFullName() + ",\n" +
                "You just created an account with Banka\n" +
                "You are required to use the following link to verify your account\n" + url + "\n" +
                "Do something outside today!\n" +
                " –Your friends at Banka";
        emailSender.sendEmail(user.getEmail(), "Registration Verification", message);
    }

    public void verifyUser(String token) {
        User user = userRepository.findByEmailVerificationToken(token);
        if (user == null) {
            throw new CustomException("Unable to verify that you registered here", HttpStatus.BAD_REQUEST);
        }
        user.setEmailVerificationStatus(EmailVerificationStatus.VERIFIED);
        userRepository.save(user);
    }

    public String signInUser(String email, String password) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));
        if (authentication == null) {
            throw new CustomException("User not found", HttpStatus.BAD_REQUEST);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userRepository.findByEmail(email);
        if (user.getEmailVerificationStatus() == EmailVerificationStatus.UNVERIFIED) {
            throw new CustomException("You haven't verified your account yet", HttpStatus.BAD_REQUEST);
        }
        return jwtTokenProvider.createToken(user.getId(), email, user.getRoles());
    }
}
