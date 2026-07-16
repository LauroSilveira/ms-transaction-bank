package com.lauro.correia.transactionbank.domain.model.transaction;

import java.util.UUID;

public record TransactionKey(Long id, UUID transactionId) {
}
