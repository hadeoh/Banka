package com.usmanadio.banka.repositories;

import com.usmanadio.banka.models.transfer.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransferRepository extends JpaRepository<Transfer, UUID> {
}
