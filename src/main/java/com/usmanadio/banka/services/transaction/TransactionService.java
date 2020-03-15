package com.usmanadio.banka.services.transaction;

import com.usmanadio.banka.models.transaction.Transaction;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

public interface TransactionService {
    Transaction creditAccount(Transaction transaction, String accountNumber, HttpServletRequest request);

    Transaction debitAccount(Transaction transaction, String accountNumber, HttpServletRequest request);

    Transaction getATransaction(UUID id);
}
