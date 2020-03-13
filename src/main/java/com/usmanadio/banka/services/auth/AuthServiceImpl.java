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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service
public class AuthServiceImpl implements AuthService {

    private UserRepository userRepository;
    private JwtTokenProvider jwtTokenProvider;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private EmailSender emailSender;
    private AuthenticationManager authenticationManager;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository,
                           JwtTokenProvider jwtTokenProvider, BCryptPasswordEncoder bCryptPasswordEncoder,
                           EmailSender emailSender, AuthenticationManager authenticationManager) {
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
        emailSender.sendEmail(user.getEmail(), "Banka Registration Verification", message);
    }

    public void verifyUser(String token) {
        User user = userRepository.findByEmailVerificationToken(token);
        if (user == null) {
            throw new CustomException("Unable to verify that you registered here", HttpStatus.BAD_REQUEST);
        }
        user.setEmailVerificationStatus(EmailVerificationStatus.VERIFIED);
        userRepository.save(user);
    }

    public void signInUser(String email, String password, HttpServletResponse response) {

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
        String token = jwtTokenProvider.createToken(user.getId(), email, user.getRoles(), 86400000);
        response.addHeader("token", token);
    }

    public void resetPassword(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new CustomException("You are yet to register on Banka", HttpStatus.BAD_REQUEST);
        }
        String token = jwtTokenProvider.createToken(user.getId(), user.getEmail(),
                user.getRoles(), 3600000);
        System.out.println(token);
        String url = "http://localhost:3000/password-reset/" + user.getId() + token;

        String message =
                "Hey" + user.getFullName() + ",\n" +
                        "We heard that you forgot your Banka password. Sorry about that!\n" +
                        "But don’t worry! You can use the following link to reset your password:\n" + url + "\n" +
                        "If you don’t use this link within 1 hour, it will expire.\n" +
                        "Do something outside today!\n" +
                        "– Your friends at Banka";
        emailSender.sendEmail(user.getEmail(), "Banka Reset Password", message);
    }

    public void setNewPassword(UUID id, String newPassword, String token) {
        if (jwtTokenProvider.isTokenExpired(token)) {
            throw new CustomException("The token has expired", HttpStatus.FORBIDDEN);
        }
        String email = jwtTokenProvider.getEmail(token);
        if (!userRepository.existsByEmail(email)) {
            throw new CustomException("There is a compromise", HttpStatus.BAD_REQUEST);
        }
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            user.get().setPassword(bCryptPasswordEncoder.encode(newPassword));
            userRepository.save(user.get());
        } else {
            throw new CustomException("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public User changeUserRole(UUID id, Role role) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new CustomException("User does not exist", HttpStatus.NOT_FOUND);
        }
        List<Role> roles = new ArrayList<>(Collections.singletonList(role));
        user.get().setRoles(roles);
        return userRepository.save(user.get());
    }
}
