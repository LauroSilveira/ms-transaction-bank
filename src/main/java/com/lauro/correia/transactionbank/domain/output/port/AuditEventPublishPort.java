package com.lauro.correia.transactionbank.domain.output.port;

import com.lauro.correia.transactionbank.domain.model.transaction.Transfer;

public interface AuditEventPublishPort {
    void publish(Transfer transfer);
}
