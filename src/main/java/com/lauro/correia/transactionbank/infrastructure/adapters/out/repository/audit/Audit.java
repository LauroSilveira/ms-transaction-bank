package com.lauro.correia.transactionbank.infrastructure.adapters.out.repository.audit;

import com.lauro.correia.transactionbank.domain.model.transaction.Status;
import jakarta.persistence.*;
import lombok.*;
import transaction_db.transactions.bank_transaction.Value;


import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Entity
@Table(name = "audit", schema = "transactions")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID transactionId;
    private String failureReason;
    private String topicOrigin;
    @Enumerated(EnumType.STRING)
    private Status status;
    private LocalDateTime transferAt;

    public Audit(Value value, String originalTopic, Status status) {
        this.transactionId = UUID.fromString(value.getTransactionId());
        this.topicOrigin = originalTopic;
        this.status = status;
        this.transferAt = convertToLocalDateTime(value.getTransferAt());
    }

    private static LocalDateTime convertToLocalDateTime(final long localDateTime) {
        return LocalDateTime.ofInstant(
                Instant.EPOCH.plus(localDateTime, ChronoUnit.MICROS),
                ZoneOffset.UTC
        );
    }
}
