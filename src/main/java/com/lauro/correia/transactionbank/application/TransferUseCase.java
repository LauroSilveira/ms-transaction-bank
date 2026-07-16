package com.lauro.correia.transactionbank.application;

import com.lauro.correia.transactionbank.domain.input.port.TransferUseCasePort;
import com.lauro.correia.transactionbank.domain.model.transaction.Transfer;
import com.lauro.correia.transactionbank.domain.output.port.TransactionRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransferUseCase implements TransferUseCasePort {

    private final TransactionRepositoryPort transactionRepositoryPort;
    //private final TransactionEventPublishPort transactionPublisherPort;

    @Override
    public Transfer execute(final Transfer transfer) {
        return this.transactionRepositoryPort.save(transfer);
        // This method is no long needed since the data is saved in a database
        // and Kafka connect reads it from there.
        //transactionPublisherPort.publish(transferSaved);
    }
}
