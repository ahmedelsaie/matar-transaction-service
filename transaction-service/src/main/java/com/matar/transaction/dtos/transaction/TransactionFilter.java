package com.matar.transaction.dtos.transaction;

import com.matar.transaction.model.trnsaction.State;
import lombok.Data;

import java.util.EnumSet;

@Data
public class TransactionFilter {
    private EnumSet<State> state;

    private Integer userId;

    private String domainId;
}
