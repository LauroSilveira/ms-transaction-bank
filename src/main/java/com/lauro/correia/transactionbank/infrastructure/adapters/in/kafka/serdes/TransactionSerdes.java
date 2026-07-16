package com.lauro.correia.transactionbank.infrastructure.adapters.in.kafka.serdes;

import com.lauro.correia.transactionbank.infrastructure.adapters.in.rest.transfer.TransferDTO;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;

public class TransactionSerdes extends Serdes.WrapperSerde<TransferDTO> {

    public TransactionSerdes() {
        super(new JacksonJsonSerializer<>(), new JacksonJsonDeserializer<>(TransferDTO.class));
    }

    public static Serde<TransferDTO> serdes() {
        JacksonJsonSerializer<TransferDTO> serializer = new JacksonJsonSerializer<>();
        JacksonJsonDeserializer<TransferDTO> deserializer = new JacksonJsonDeserializer<>(TransferDTO.class);
        return Serdes.serdeFrom(serializer, deserializer);
    }
}
