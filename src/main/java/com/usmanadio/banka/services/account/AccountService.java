package com.usmanadio.banka.services.account;

import com.usmanadio.banka.dto.account.AccountRequest;
import com.usmanadio.banka.models.account.Account;
import com.usmanadio.banka.models.account.AccountStatus;
import com.usmanadio.banka.models.account.AccountType;
import com.usmanadio.banka.models.transaction.Transaction;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

public interface AccountService {
    Account createAccount(AccountRequest accountRequest, HttpServletRequest request);

    Account setAccountStatus(String accountNumber, AccountStatus accountStatus);

    void deleteAccount(String accountNumber);

    List<Account> viewOwnAccounts(HttpServletRequest request);

    List<Account> viewAllAccounts(Integer page, Integer size);

    Account viewAnAccount(String accountNumber);

    List<Account> viewUserAccounts(UUID userId);

    List<Account> viewAccountsBasedOnStatus(AccountStatus accountStatus, Integer page, Integer size);

    List<Account> viewAccountsBasedOnType(AccountType accountType, Integer page, Integer size);

    List<Transaction> getTransactions(String accountNumber, Integer page, Integer size, String sortBy);
}
