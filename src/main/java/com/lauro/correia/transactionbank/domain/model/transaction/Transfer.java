package com.lauro.correia.transactionbank.domain.model.transaction;

import com.lauro.correia.transactionbank.infrastructure.adapters.in.rest.transfer.TransferDTO;
import com.lauro.correia.transactionbank.infrastructure.adapters.out.repository.transaction.entity.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record Transfer(
        Long id,
        UUID transactionId,
        String description,
        BigDecimal amount,
        LocalDateTime transferAt,
        Status status,
        TransactionType transactionType) {

    public static Transfer toDomain(Transaction entity) {
        return new Transfer(entity.getId(),
                entity.getTransactionId(),
                entity.getDescription(),
                entity.getAmount(),
                entity.getTransferAt(),
                entity.getStatus(),
                entity.getTransactionType());
    }

    public static Transfer toDomain(final TransferDTO transferDTO) {
        return  new Transfer(
                transferDTO.id(),
                transferDTO.transactionId(),
                transferDTO.description(),
                transferDTO.amount(),
                transferDTO.transferAt(),
                transferDTO.status(),
                transferDTO.transactionType()
        );
    }
}
