package com.usmanadio.banka.repositories;

import com.usmanadio.banka.models.account.DomiciliaryAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DomiciliaryAccountRepository extends JpaRepository<DomiciliaryAccount, UUID> {
}
