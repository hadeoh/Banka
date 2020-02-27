package com.usmanadio.banka.controllers;

import com.usmanadio.banka.dto.auth.SignUpRequest;
import com.usmanadio.banka.models.user.User;
import com.usmanadio.banka.responses.Response;
import com.usmanadio.banka.services.auth.AuthService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
}
