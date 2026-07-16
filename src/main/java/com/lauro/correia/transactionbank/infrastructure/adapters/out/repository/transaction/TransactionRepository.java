package com.lauro.correia.transactionbank.infrastructure.adapters.out.repository.transaction;

import com.lauro.correia.transactionbank.infrastructure.adapters.out.repository.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<Transaction> findByTransactionId(UUID transactionId);
}
