package com.usmanadio.banka.repositories;

import com.usmanadio.banka.models.account.Account;
import com.usmanadio.banka.models.account.AccountStatus;
import com.usmanadio.banka.models.account.AccountType;
import com.usmanadio.banka.models.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    boolean existsByAccountNumber(String accountNumber);
    Account findByAccountNumber(String accountNumber);
    Integer deleteByAccountNumber(String accountNumber);
    List<Account> findAllByUser(User user);
    Slice<Account> findAllByAccountStatus(AccountStatus accountStatus, Pageable pageable);
    Slice<Account> findAllByAccountType(AccountType accountType, Pageable pageable);
}
