package com.usmanadio.banka.repositories;

import com.usmanadio.banka.models.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {
}
