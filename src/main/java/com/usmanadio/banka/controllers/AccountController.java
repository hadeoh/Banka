package com.usmanadio.banka.controllers;

import com.usmanadio.banka.dto.account.AccountRequest;
import com.usmanadio.banka.dto.account.AccountStatusRequest;
import com.usmanadio.banka.models.account.Account;
import com.usmanadio.banka.models.account.AccountStatus;
import com.usmanadio.banka.responses.Response;
import com.usmanadio.banka.services.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("accounts")
public class AccountController {

    private AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
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

    @PatchMapping("{accountNumber}")
    public ResponseEntity<Response<Account>> setAccountStatus(@PathVariable String accountNumber,
                                                              @RequestBody AccountStatusRequest accountStatus) {
        Account account = accountService.setAccountStatus(accountNumber, accountStatus.getAccountStatus());
        Response<Account> response = new Response<>(HttpStatus.ACCEPTED);
        response.setMessage("Account deactivated successfully");
        if (accountStatus.getAccountStatus() == AccountStatus.ACTIVE) {
            response.setMessage("Account activated successfully");
        }
        response.setData(account);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("{accountNumber}")
    public ResponseEntity<Response<String>> deleteAccount(@PathVariable String accountNumber) {
        accountService.deleteAccount(accountNumber);
        Response<String> response = new Response<>(HttpStatus.OK);
        response.setMessage("Account with account number " + accountNumber +" deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/own")
    public ResponseEntity<Response<List<Account>>> viewOwnAccounts(HttpServletRequest request) {
        List<Account> ownAccounts = accountService.viewOwnAccounts(request);
        Response<List<Account>> response = new Response<>(HttpStatus.OK);
        response.setMessage("All own accounts retrieved successfully");
        response.setData(ownAccounts);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Response<List<Account>>> viewAllAccounts(@RequestParam(defaultValue = "1") Integer page,
                                                                   @RequestParam(defaultValue = "10") Integer size) {
        List<Account> accounts = accountService.viewAllAccounts(page, size);
        Response<List<Account>> response = new Response<>(HttpStatus.OK);
        response.setMessage("All accounts retrieved successfully");
        response.setData(accounts);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("{accountNumber}")
    public ResponseEntity<Response<Account>> viewAnAccount(@PathVariable String accountNumber) {
        Account account = accountService.viewAnAccount(accountNumber);
        Response<Account> response = new Response<>(HttpStatus.OK);
        response.setMessage("Account details retrieved successfully");
        response.setData(account);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("user")
    public ResponseEntity<Response<List<Account>>> viewUserAccounts(@RequestParam UUID userId) {
        List<Account> accounts = accountService.viewUserAccounts(userId);
        Response<List<Account>> response = new Response<>(HttpStatus.OK);
        response.setMessage("All user accounts retrieved successfully");
        response.setData(accounts);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
