package com.usmanadio.banka.controllers;

import com.usmanadio.banka.dto.account.AccountRequest;
import com.usmanadio.banka.models.account.Account;
import com.usmanadio.banka.responses.Response;
import com.usmanadio.banka.services.account.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("accounts")
public class AccountController {

    private AccountService accountService;
    private ModelMapper modelMapper;

    @Autowired
    public AccountController(AccountService accountService, ModelMapper modelMapper) {
        this.accountService = accountService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<Response<Account>> createAccount(@Valid @RequestBody AccountRequest accountRequest,
                                                           HttpServletRequest request) {
        Account account = accountService.createAccount(accountRequest, request);
        Response<Account> response = new Response<>(HttpStatus.CREATED);
        response.setMessage("You have successfully created a " + accountRequest.getAccountType() + " account");
        response.setData(account);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}