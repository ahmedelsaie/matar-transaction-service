package com.matar.transaction.DomainEvent;

import com.matar.transaction.model.trnsaction.State;
import com.matar.transaction.model.trnsaction.Transaction;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TransactionChangedEvent {

    private State oldState;

    private State newState;

    private Transaction transaction;

    public TransactionChangedEvent(State oldState, Transaction transaction) {
        this.oldState = oldState;
        this.newState = transaction.getState();
        this.transaction = transaction;
    }
}
