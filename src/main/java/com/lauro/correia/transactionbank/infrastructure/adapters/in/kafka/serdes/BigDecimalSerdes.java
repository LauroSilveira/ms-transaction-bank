package com.lauro.correia.transactionbank.infrastructure.adapters.in.kafka.serdes;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

import java.math.BigDecimal;

public class BigDecimalSerdes {
    public static Serde<BigDecimal> serdes() {
        return Serdes.serdeFrom(
                (topic, data) -> data.toString().getBytes(),
                (topic, data) -> new BigDecimal(new String(data))
        );
    }
}
