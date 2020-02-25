package com.usmanadio.banka.repositories;

import com.usmanadio.banka.models.transfer.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferRepository extends JpaRepository<Transfer, Integer> {
}
