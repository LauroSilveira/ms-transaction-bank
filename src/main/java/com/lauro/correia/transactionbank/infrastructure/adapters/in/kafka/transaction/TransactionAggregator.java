package com.lauro.correia.transactionbank.infrastructure.adapters.in.kafka.transaction;

import com.lauro.correia.transactionbank.infrastructure.adapters.avro.TransferRecord;
import com.lauro.correia.transactionbank.infrastructure.adapters.in.kafka.serdes.BigDecimalSerdes;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;

import java.math.BigDecimal;

@Configuration
@EnableKafkaStreams
@Slf4j
public class TransactionAggregator {

    @Autowired
    public void aggregateTransaction(final StreamsBuilder streamsBuilder,
                                     final Serde<TransferRecord> transferRecordSerde,
                                     @Value("${ms-transaction-bank.kafka.topics.transaction-create}") final String inputTopic,
                                     @Value("${ms-transaction-bank.kafka.topics.transaction-audit}") final String outputTopic) {

        streamsBuilder.stream(inputTopic, Consumed.with(Serdes.String(), transferRecordSerde))
                .peek((key, value) -> log.info("[TransactionAggregator] Message received: {} - value: {}", key, value))
                .groupBy((key, value) -> value.getTransactionType().name())
                .aggregate(
                        () -> BigDecimal.ZERO,
                        (key, value, aggregate) -> aggregate.add(value.getAmount()),
                        Materialized.with(Serdes.String(), BigDecimalSerdes.serdes()
                        )
                )
                .toStream()
                .to(outputTopic, Produced.with(Serdes.String(), BigDecimalSerdes.serdes()));
    }
}
