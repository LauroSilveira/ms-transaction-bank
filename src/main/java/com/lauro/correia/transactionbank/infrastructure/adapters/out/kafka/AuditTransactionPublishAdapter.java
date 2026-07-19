package com.lauro.correia.transactionbank.infrastructure.adapters.out.kafka;

import com.lauro.correia.transactionbank.domain.model.transaction.Transfer;
import com.lauro.correia.transactionbank.domain.output.port.AuditEventPublishPort;
import com.lauro.correia.transactionbank.infrastructure.adapters.avro.Status;
import com.lauro.correia.transactionbank.infrastructure.adapters.avro.TransactionType;
import com.lauro.correia.transactionbank.infrastructure.adapters.avro.TransferRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuditTransactionPublishAdapter implements AuditEventPublishPort {

    @Value("${ms-transaction-bank.kafka.topics.transaction-audit}")
    private String topic;

    private final KafkaTemplate<UUID, TransferRecord> kafkaTemplate;
    // This method is no long needed since the data is saved in a database
    // and Kafka connect reads it from there.
    @Override
    public void publish(final Transfer transfer) {
        var transferRecord = TransferRecord.newBuilder()
                .setId(transfer.id())
                .setAmount(transfer.amount())
                .setDescription(transfer.description())
                .setStatus(Status.valueOf(transfer.status().name()))
                .setTransactionId(transfer.transactionId())
                .setTransferAt(transfer.transferAt().toInstant(ZoneOffset.UTC))
                .setTransactionType(TransactionType.valueOf(transfer.transactionType().name()))
                .build();
        kafkaTemplate.send(topic, transferRecord.getTransactionId(), transferRecord);
    }
}
