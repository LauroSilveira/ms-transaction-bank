package com.lauro.correia.transactionbank.infrastructure.adapters.out.repository.transaction.entity;

import com.lauro.correia.transactionbank.domain.model.transaction.Status;
import com.lauro.correia.transactionbank.domain.model.transaction.TransactionType;
import com.lauro.correia.transactionbank.domain.model.transaction.Transfer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import transaction_db.transactions.bank_transaction.Value;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Entity
@Table(name = "bank_transaction", schema = "transactions")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID transactionId;
    private String description;
    private BigDecimal amount;
    private LocalDateTime transferAt;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    public Transaction(final Value after) {
        this.id = after.getId();
        this.transactionId = UUID.fromString(after.getTransactionId());
        this.description = after.getDescription();
        this.amount = after.getAmount();
        this.transferAt = convertToLocalDateTime(after.getTransferAt());
        this.status = Status.valueOf(after.getStatus());
        this.transactionType = TransactionType.valueOf(after.getTransactionType());

    }

    private static LocalDateTime convertToLocalDateTime(final long localDateTime) {
        return LocalDateTime.ofInstant(
                Instant.EPOCH.plus(localDateTime, ChronoUnit.MICROS),
                ZoneOffset.UTC
        );
    }

    public static Transaction of(Transfer transfer) {
        return new Transaction(null, transfer.transactionId(), transfer.description(), transfer.amount(),
                transfer.transferAt(), transfer.status(), transfer.transactionType());
    }
}
