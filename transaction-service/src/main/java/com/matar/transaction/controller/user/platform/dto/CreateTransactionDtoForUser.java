package com.matar.transaction.controller.user.platform.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateTransactionDtoForUser {

    @NotBlank(message = "domain id cannot be empty")
    private String domainId;

    @DecimalMin(value = "0", inclusive = false)
    @Digits(integer = 10, fraction = 2)
    private BigDecimal money;
}
