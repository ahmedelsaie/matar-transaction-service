package com.matar.transaction.exceptions;

import com.matar.transaction.model.trnsaction.Transaction;

public class HasAlreadyTransactionException extends RuntimeException {

    public HasAlreadyTransactionException(Transaction transaction) {
        super(getMessage(transaction));
    }

    private static String getMessage(Transaction transaction) {
        return String.format("User %s has already pending transaction for domain object of %s",
                transaction.getUserId(), transaction.getDomainId());
    }
}
