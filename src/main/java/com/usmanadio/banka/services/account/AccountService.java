package com.usmanadio.banka.services.account;

import com.usmanadio.banka.dto.account.AccountRequest;
import com.usmanadio.banka.models.account.Account;

import javax.servlet.http.HttpServletRequest;

public interface AccountService {
    Account createAccount(AccountRequest accountRequest, HttpServletRequest request);
}
