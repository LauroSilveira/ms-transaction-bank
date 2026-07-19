package com.lauro.correia.transactionbank.infrastructure.adapters.in.kafka.audit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import transaction_db.transactions.bank_transaction.Envelope;


@Component
@RequiredArgsConstructor
@Slf4j
public class AudiConsumerAdapter {

    @KafkaListener(
            topics = "${ms-transaction-bank.kafka.connect.transaction-create}",
            groupId = "${spring.kafka.consumer.group-id}")
    public void audiConsumer(final Envelope transferDTO,
                             @Header(KafkaHeaders.RECEIVED_PARTITION) final int partition,
                             @Header(KafkaHeaders.OFFSET) final int offset) {
        log.info("[AudiConsumerAdapter] Message received in Partition: {} - Offset: {} - message: {}",
                partition, offset, transferDTO);
    }

}
