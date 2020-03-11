package com.usmanadio.banka.services.account;

import com.usmanadio.banka.dto.account.AccountRequest;
import com.usmanadio.banka.models.account.Account;
import com.usmanadio.banka.models.account.AccountType;
import com.usmanadio.banka.models.account.DomiciliaryAccount;
import com.usmanadio.banka.models.user.User;
import com.usmanadio.banka.repositories.AccountRepository;
import com.usmanadio.banka.repositories.DomiciliaryAccountRepository;
import com.usmanadio.banka.repositories.UserRepository;
import com.usmanadio.banka.security.JwtTokenProvider;
import com.usmanadio.banka.services.utils.AccountNumberCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;
    private UserRepository userRepository;
    private JwtTokenProvider jwtTokenProvider;
    private DomiciliaryAccountRepository domiciliaryAccountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, UserRepository userRepository,
                              JwtTokenProvider jwtTokenProvider,
                              DomiciliaryAccountRepository domiciliaryAccountRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.domiciliaryAccountRepository = domiciliaryAccountRepository;
    }

    @Transactional
    public Account createAccount(AccountRequest accountRequest, HttpServletRequest request) {
        AccountNumberCreator accountNumberCreator = new AccountNumberCreator();
        String accountNumber = accountNumberCreator.createAcctNumber();
        if (accountRepository.existsByAccountNumber(accountNumber)) {
            accountNumber = accountNumberCreator.createAcctNumber();
        }
        User user = userRepository.findByEmail(jwtTokenProvider.getEmail(jwtTokenProvider.resolveToken(request)));
        Account account = new Account();
        account.setUser(user);
        account.setAccountNumber(accountNumber);
        account.setAccountType(accountRequest.getAccountType());
        Account accountCreated = accountRepository.save(account);
        if (account.getAccountType() == AccountType.DOMICILIARY) {
            DomiciliaryAccount domiciliaryAccount = new DomiciliaryAccount();
            domiciliaryAccount.setAccount(accountCreated);
            domiciliaryAccount.setDomiciliaryAccountType(accountRequest.getDomiciliaryAccountType());
            domiciliaryAccountRepository.save(domiciliaryAccount);
        }
        return accountCreated;
    }
}