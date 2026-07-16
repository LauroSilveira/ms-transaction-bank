package com.lauro.correia.transactionbank.infrastructure.adapters.in.kafka.serdes;

import com.lauro.correia.transactionbank.infrastructure.adapters.avro.TransferRecord;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serde;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class KStreamTransferRecordSerde {

    @Bean
    public Serde<TransferRecord> transferRecordSerde(
            @Value("${spring.kafka.streams.properties.schema.registry.url}") final String schemaRegistryUrl) {
        final SpecificAvroSerde<TransferRecord> serde = new SpecificAvroSerde<>();
        serde.configure(Map.of(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl),
                false);
        return serde;
    }
}
