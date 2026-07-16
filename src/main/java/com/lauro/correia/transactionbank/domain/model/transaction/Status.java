package com.lauro.correia.transactionbank.domain.model.transaction;

public enum Status {
    PROCESSED,
    PENDING,
    MANUAL_RESOLVED,
    PENDING_RETRY
}
