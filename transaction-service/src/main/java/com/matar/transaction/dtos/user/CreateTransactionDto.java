package com.matar.transaction.dtos.user;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateTransactionDto {

    private String domainId;

    private BigDecimal money;

    private Integer userId;
}
