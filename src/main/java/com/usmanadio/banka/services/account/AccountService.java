package com.usmanadio.banka.services.account;

import com.usmanadio.banka.dto.account.AccountRequest;
import com.usmanadio.banka.models.account.Account;
import com.usmanadio.banka.models.account.AccountStatus;

import javax.servlet.http.HttpServletRequest;

public interface AccountService {
    Account createAccount(AccountRequest accountRequest, HttpServletRequest request);
    Account setAccountStatus(String accountNumber, AccountStatus accountStatus);
    void deleteAccount(String accountNumber);
}
