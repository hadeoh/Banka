package com.usmanadio.banka.repositories;

import com.usmanadio.banka.models.account.Account;
import com.usmanadio.banka.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    boolean existsByAccountNumber(String accountNumber);
    Account findByAccountNumber(String accountNumber);
    Integer deleteByAccountNumber(String accountNumber);
    List<Account> findAllByUser(User user);
}
