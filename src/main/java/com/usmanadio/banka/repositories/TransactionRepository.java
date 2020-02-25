package com.usmanadio.banka.repositories;

import com.usmanadio.banka.models.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
}
