package com.usmanadio.banka.controllers;

import com.usmanadio.banka.dto.auth.LoginRequest;
import com.usmanadio.banka.dto.auth.NewPasswordRequest;
import com.usmanadio.banka.dto.auth.PasswordResetRequest;
import com.usmanadio.banka.dto.auth.SignUpRequest;
import com.usmanadio.banka.models.user.Role;
import com.usmanadio.banka.models.user.User;
import com.usmanadio.banka.responses.Response;
import com.usmanadio.banka.services.auth.AuthService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("auth")
public class AuthController {

    private AuthService authService;
    private ModelMapper modelMapper;

    @Autowired
    public AuthController(AuthService authService, ModelMapper modelMapper) {
        this.authService = authService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("signUp")
    public ResponseEntity<Response<String>> createUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        authService.createUser(modelMapper.map(signUpRequest, User.class));
        Response<String> response = new Response<>(HttpStatus.CREATED);
        response.setMessage("You have successfully signed up on Banka");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("verifyEmail/{token}")
    public ResponseEntity<Response<String>> verifyUser(@PathVariable String token) {
        authService.verifyUser(token);
        Response<String> response = new Response<>(HttpStatus.ACCEPTED);
        response.setMessage("You are now a verified user of Banka");
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @PostMapping("login")
    public ResponseEntity<Response<String>> signInUser(@Valid @RequestBody LoginRequest loginRequest,
                                                                    HttpServletResponse response) {
        authService.signInUser(loginRequest.getEmail(), loginRequest.getPassword(), response);
        Response<String> res = new Response<>(HttpStatus.OK);
        res.setMessage("User successfully logged in");
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("password-reset")
    public ResponseEntity<Response<String>> resetPassword(@Valid @RequestBody
                                                                  PasswordResetRequest passwordResetRequest) {
        authService.resetPassword(passwordResetRequest.getEmail());
        Response<String> response = new Response<>(HttpStatus.OK);
        response.setMessage("A password reset link has been sent to your email");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("set-new-password")
    public ResponseEntity<Response<String>> setNewPassword(@RequestParam("id") UUID id,
                                                           @Valid @RequestBody NewPasswordRequest newPasswordRequest,
                                                           @RequestParam String token) {
        authService.setNewPassword(id, newPasswordRequest.getNewPassword(), token);
        Response<String> response = new Response<>(HttpStatus.OK);
        response.setMessage("Password successfully reset");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("user")
    public ResponseEntity<Response<User>> changeUserRole(@RequestParam UUID id, @RequestParam Role role) {
        User user = authService.changeUserRole(id, role);
        Response<User> response = new Response<>(HttpStatus.ACCEPTED);
        response.setMessage("User role successfully changed to " + role);
        response.setData(user);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }
}
