package com.usmanadio.banka.services.transaction;

import com.usmanadio.banka.models.transaction.Transaction;

import javax.servlet.http.HttpServletRequest;

public interface TransactionService {
    Transaction creditAccount(Transaction transaction, String accountNumber, HttpServletRequest request);
}
