package com.usmanadio.banka.services.transaction;

import com.usmanadio.banka.exceptions.CustomException;
import com.usmanadio.banka.models.account.Account;
import com.usmanadio.banka.models.transaction.Transaction;
import com.usmanadio.banka.models.transaction.TransactionType;
import com.usmanadio.banka.models.user.User;
import com.usmanadio.banka.repositories.AccountRepository;
import com.usmanadio.banka.repositories.TransactionRepository;
import com.usmanadio.banka.repositories.UserRepository;
import com.usmanadio.banka.security.JwtTokenProvider;
import com.usmanadio.banka.services.utils.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

@Service
public class TransactionServiceImpl implements TransactionService {

    private TransactionRepository transactionRepository;
    private JwtTokenProvider jwtTokenProvider;
    private UserRepository userRepository;
    private EmailSender emailSender;
    private AccountRepository accountRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, JwtTokenProvider jwtTokenProvider,
                                  UserRepository userRepository, EmailSender emailSender,
                                  AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.emailSender = emailSender;
        this.accountRepository = accountRepository;
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_STAFF')")
    public Transaction creditAccount(Transaction transaction, String accountNumber, HttpServletRequest request) {
        User user = userRepository.findByEmail(jwtTokenProvider.getEmail(jwtTokenProvider.resolveToken(request)));
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new CustomException("Account does not exist", HttpStatus.BAD_REQUEST);
        }
        Double newBalance = account.getAccountBalance() + transaction.getAmount();
        transaction.setAccount(account);
        transaction.setOldBalance(account.getAccountBalance());
        transaction.setNewBalance(newBalance);
        transaction.setTransactionType(TransactionType.CREDIT);
        transaction.setCashierId(user.getId());
        account.setAccountBalance(newBalance);
        accountRepository.save(account);
        Transaction newTransaction = transactionRepository.save(transaction);
        String message = "Hi, " + user.getFullName() + "\n" +
                "\tYour account has been credited with an amount of " + transaction.getAmount();
        emailSender.sendEmail(user.getEmail(), "CREDIT"+ accountNumber, message);
        return newTransaction;
    }
}
