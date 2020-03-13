package com.usmanadio.banka.controllers;

import com.usmanadio.banka.dto.transaction.TransactionRequest;
import com.usmanadio.banka.models.transaction.Transaction;
import com.usmanadio.banka.responses.Response;
import com.usmanadio.banka.services.transaction.TransactionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("transactions")
public class TransactionController {

    private TransactionService transactionService;
    private ModelMapper modelMapper;

    @Autowired
    public TransactionController(TransactionService transactionService, ModelMapper modelMapper) {
        this.transactionService = transactionService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("{accountNumber}/credit")
    public ResponseEntity<Response<Transaction>> creditAccount(@PathVariable String accountNumber, @Valid @RequestBody
            TransactionRequest transactionRequest, HttpServletRequest request) {
        Transaction transaction = transactionService.creditAccount(modelMapper.map(transactionRequest,
                Transaction.class), accountNumber, request);
        Response<Transaction> response = new Response<>(HttpStatus.CREATED);
        response.setMessage("Account with account number " + accountNumber + " has been successfully credited with " +
                transactionRequest.getAmount());
        response.setData(transaction);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
