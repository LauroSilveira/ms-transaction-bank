package com.lauro.correia.transactionbank.domain.input.port;

import com.lauro.correia.transactionbank.domain.model.transaction.Transfer;

public interface TransferUseCasePort {

    Transfer execute(Transfer transfer);
}
