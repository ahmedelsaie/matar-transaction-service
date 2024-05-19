package com.matar.transaction.DomainEvent;

import com.matar.transaction.model.trnsaction.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionCreatedEvent {

    private Transaction transaction;
}
