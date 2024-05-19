package com.matar.transaction.dtos.transaction;

import com.matar.transaction.model.trnsaction.State;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionDto {
    private String id;

    private State state;

    private Integer userId;

    private String domainId;

    private BigDecimal money;

    private String paymentGatewayCheckoutId;
}
