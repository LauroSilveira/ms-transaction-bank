package com.lauro.correia.transactionbank.infrastructure.adapters.out.kafka;

import com.lauro.correia.transactionbank.domain.model.transaction.Transfer;
import com.lauro.correia.transactionbank.domain.output.port.TransactionEventPublishPort;
import com.lauro.correia.transactionbank.infrastructure.adapters.avro.Status;
import com.lauro.correia.transactionbank.infrastructure.adapters.avro.TransactionType;
import com.lauro.correia.transactionbank.infrastructure.adapters.avro.TransferRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionPublishAdapter implements TransactionEventPublishPort {

    @Value("${ms-transaction-bank.kafka.topics.transaction-create}")
    private String topic;

    private final KafkaTemplate<String, TransferRecord> kafkaTemplate;

    @Override
    public void publish(final Transfer transfer) {
        var transferRecord = TransferRecord.newBuilder()
                .setId(transfer.id())
                .setTransactionId(transfer.transactionId())
                .setDescription(transfer.description())
                .setAmount(transfer.amount())
                .setTransferAt(transfer.transferAt().toInstant(ZoneOffset.UTC))
                .setStatus(Status.valueOf(transfer.status().name()))
                .setTransactionType(TransactionType.valueOf(transfer.transactionType().name()))
                .build();
        kafkaTemplate.send(topic, transferRecord.getTransactionId().toString(), transferRecord);
    }
}
