package com.lauro.correia.transactionbank.infrastructure.adapters.in.rest.transfer;

import com.lauro.correia.transactionbank.domain.model.transaction.Status;
import com.lauro.correia.transactionbank.domain.model.transaction.TransactionType;
import com.lauro.correia.transactionbank.domain.model.transaction.Transfer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransferDTO(
        Long id,
        UUID transactionId,
        String description,
        BigDecimal amount,
        LocalDateTime transferAt,
        Status status,
        TransactionType transactionType) {

    public static TransferDTO of(final Transfer transaction) {
        return new TransferDTO(
                transaction.id(),
                transaction.transactionId(),
                transaction.description(),
                transaction.amount(),
                transaction.transferAt(),
                transaction.status(),
                transaction.transactionType());
    }
}
