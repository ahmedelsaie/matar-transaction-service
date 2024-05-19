package com.matar.transaction.controller.user.platform.dto;

import com.matar.transaction.model.trnsaction.State;
import lombok.Data;

import java.util.EnumSet;

@Data
public class TransactionFilterForUser {
    private EnumSet<State> state;

    private String domainId;
}
