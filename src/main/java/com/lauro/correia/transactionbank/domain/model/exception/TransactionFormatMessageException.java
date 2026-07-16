package com.lauro.correia.transactionbank.domain.model.exception;

public final class TransactionFormatMessageException extends DomainException {

    public TransactionFormatMessageException(final String invalidMessageFormat) {
        super(invalidMessageFormat);
    }
}
