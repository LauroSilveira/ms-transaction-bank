package com.lauro.correia.transactionbank.domain.model.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public abstract sealed class DomainException extends RuntimeException permits TransactionFormatMessageException {
    private final String message;
}
