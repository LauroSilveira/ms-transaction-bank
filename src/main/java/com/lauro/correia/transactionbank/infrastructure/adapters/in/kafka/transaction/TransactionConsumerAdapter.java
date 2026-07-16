package com.lauro.correia.transactionbank.infrastructure.adapters.in.kafka.transaction;

import com.lauro.correia.transactionbank.domain.model.exception.TransactionFormatMessageException;
import com.lauro.correia.transactionbank.domain.model.transaction.ForbiddenWords;
import com.lauro.correia.transactionbank.domain.model.transaction.Status;
import com.lauro.correia.transactionbank.infrastructure.adapters.out.repository.audit.Audit;
import com.lauro.correia.transactionbank.infrastructure.adapters.out.repository.audit.AuditRepository;
import com.lauro.correia.transactionbank.infrastructure.adapters.out.repository.transaction.TransactionRepository;
import com.lauro.correia.transactionbank.infrastructure.adapters.out.repository.transaction.entity.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.BackOff;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;
import transaction_db.transactions.bank_transaction.Envelope;

import java.nio.charset.StandardCharsets;
import java.util.Map;


@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionConsumerAdapter {
    private final TransactionRepository transactionRepository;
    private final AuditRepository auditRepository;

    @KafkaListener(
            id = "transaction-consumer-main",
            topics = "${ms-transaction-bank.kafka.connect.transaction-create}",
            groupId = "${spring.kafka.consumer.group-id}")

    @RetryableTopic(
            backOff = @BackOff(value = 3000L),
            attempts = "3",
            include = TransactionFormatMessageException.class)
    public void transactionConsumer(final Envelope envelope,
                                    @Header(KafkaHeaders.RECEIVED_PARTITION) final int partition,
                                    @Header(KafkaHeaders.OFFSET) final int offset) {
        final var transaction = new Transaction(envelope.getAfter());
        log.info("[TransactionConsumer] Message received in Partition: {} - Offset: {}: message: {}", partition,
                offset, transaction);
        if (transaction.getDescription().contains(ForbiddenWords.ERROR.name())) {
            throw new TransactionFormatMessageException("Invalid message format");
        }
        transaction.setStatus(Status.PROCESSED);
        this.transactionRepository.save(transaction);
    }

    @DltHandler
    public void dltConsumer(final Envelope envelope,
                            @Header("kafka_original-topic") final byte[] originalTopic,
                            @Headers final Map<String, Object> headers) {
        log.info("[TransactionConsumer] DLT Consumer Message received: {}", envelope);
        headers.forEach((key, value) -> log.info("{} = {}", key, value));
        final var originalTopicUTF = new String(originalTopic, StandardCharsets.UTF_8);
        final var audit = new Audit(envelope.getAfter(), originalTopicUTF, Status.PENDING_RETRY);
        auditRepository.save(audit);
    }
}
