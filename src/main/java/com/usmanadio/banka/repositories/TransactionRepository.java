package com.usmanadio.banka.repositories;

import com.usmanadio.banka.models.account.Account;
import com.usmanadio.banka.models.transaction.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    Slice<Transaction> findByAccount(Account account, Pageable pageable);
}
