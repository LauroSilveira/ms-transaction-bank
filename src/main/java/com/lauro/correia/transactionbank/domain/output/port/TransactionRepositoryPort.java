package com.lauro.correia.transactionbank.domain.output.port;

import com.lauro.correia.transactionbank.domain.model.transaction.Transfer;

public interface TransactionRepositoryPort {

    Transfer save(Transfer transfer);
}
