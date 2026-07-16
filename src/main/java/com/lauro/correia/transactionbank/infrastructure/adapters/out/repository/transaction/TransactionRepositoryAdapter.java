package com.lauro.correia.transactionbank.infrastructure.adapters.out.repository.transaction;

import com.lauro.correia.transactionbank.domain.model.transaction.Transfer;
import com.lauro.correia.transactionbank.domain.output.port.TransactionRepositoryPort;
import com.lauro.correia.transactionbank.infrastructure.adapters.out.repository.transaction.entity.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class TransactionRepositoryAdapter implements TransactionRepositoryPort {
    private final TransactionRepository transactionRepository;

    @Override
    public Transfer save(final Transfer transfer) {
        final var entity = transactionRepository.save(Transaction.of(transfer));
        return Transfer.toDomain(entity);
    }
}
