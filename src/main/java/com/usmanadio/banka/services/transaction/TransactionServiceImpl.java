package com.usmanadio.banka.services.transaction;

import com.usmanadio.banka.exceptions.CustomException;
import com.usmanadio.banka.models.account.Account;
import com.usmanadio.banka.models.account.AccountType;
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
import java.util.Optional;
import java.util.UUID;

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
        Transaction newTransaction = transactionCalc(transaction, user, account, newBalance, TransactionType.CREDIT);
        String message = "Hi, " + user.getFullName() + "\n" +
                "Your account has been credited with an amount of " + transaction.getAmount() + "\n" +
                "Your balance is " + account.getAccountBalance();
        emailSender.sendEmail(user.getEmail(), "CREDIT "+ accountNumber, message);
        return newTransaction;
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_STAFF')")
    public Transaction debitAccount(Transaction transaction, String accountNumber, HttpServletRequest request) {
        User user = userRepository.findByEmail(jwtTokenProvider.getEmail(jwtTokenProvider.resolveToken(request)));
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new CustomException("Account does not exist", HttpStatus.BAD_REQUEST);
        }
        if (account.getAccountType() == AccountType.CURRENT) {
            if (account.getAccountBalance() - transaction.getAmount() <= 1000.00) {
                throw new CustomException("Insufficient balance", HttpStatus.BAD_REQUEST);
            }
        }
        if (transaction.getAmount() > account.getAccountBalance()) {
            throw new CustomException("Insufficient balance", HttpStatus.BAD_REQUEST);
        }
        Double newBalance = account.getAccountBalance() - transaction.getAmount();
        Transaction newTransaction = transactionCalc(transaction, user, account, newBalance, TransactionType.DEBIT);
        String message = "Hi, " + user.getFullName() + "\n" +
                "Your account has been debited with an amount of " + transaction.getAmount() + "\n" +
                "Your balance is " + account.getAccountBalance();
        emailSender.sendEmail(user.getEmail(), "DEBIT "+ accountNumber, message);
        return newTransaction;
    }

    public Transaction getATransaction(UUID id) {
        Optional<Transaction> transaction = transactionRepository.findById(id);
        if (transaction.isEmpty()) {
            throw new CustomException("Such transaction does not exist", HttpStatus.NOT_FOUND);
        }
        return transaction.get();
    }

    private Transaction transactionCalc(Transaction transaction, User user, Account account, Double newBalance, TransactionType debit) {
        transaction.setAccount(account);
        transaction.setOldBalance(account.getAccountBalance());
        transaction.setNewBalance(newBalance);
        transaction.setTransactionType(debit);
        transaction.setCashierId(user.getId());
        account.setAccountBalance(newBalance);
        accountRepository.save(account);
        return transactionRepository.save(transaction);
    }
}
