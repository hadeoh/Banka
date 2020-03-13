package com.usmanadio.banka.services.account;

import com.usmanadio.banka.dto.account.AccountRequest;
import com.usmanadio.banka.exceptions.CustomException;
import com.usmanadio.banka.models.account.Account;
import com.usmanadio.banka.models.account.AccountStatus;
import com.usmanadio.banka.models.account.AccountType;
import com.usmanadio.banka.models.account.DomiciliaryAccount;
import com.usmanadio.banka.models.user.User;
import com.usmanadio.banka.repositories.AccountRepository;
import com.usmanadio.banka.repositories.DomiciliaryAccountRepository;
import com.usmanadio.banka.repositories.UserRepository;
import com.usmanadio.banka.security.JwtTokenProvider;
import com.usmanadio.banka.services.utils.AccountNumberCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    @PreAuthorize("hasRole('ROLE_CLIENT')")
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

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    public Account setAccountStatus(String accountNumber, AccountStatus accountStatus) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new CustomException("Account does not exist", HttpStatus.NOT_FOUND);
        }
        account.setAccountStatus(accountStatus);
        return accountRepository.save(account);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    public void deleteAccount(String accountNumber) {
        Integer res = accountRepository.deleteByAccountNumber(accountNumber);
        if (res == null || res == 0) {
            throw new CustomException("Account with account number " + accountNumber + " does not exist",
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public List<Account> viewOwnAccounts(HttpServletRequest request) {
        User user = userRepository.findByEmail(jwtTokenProvider.getEmail(jwtTokenProvider.resolveToken(request)));
        return accountRepository.findAllByUser(user);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    public List<Account> viewAllAccounts(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Slice<Account> accounts = accountRepository.findAll(pageable);
        return accounts.getContent();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    public Account viewAnAccount(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    public List<Account> viewUserAccounts(UUID userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new CustomException("User not found", HttpStatus.NOT_FOUND);
        }
        return accountRepository.findAllByUser(user.get());
    }
}
