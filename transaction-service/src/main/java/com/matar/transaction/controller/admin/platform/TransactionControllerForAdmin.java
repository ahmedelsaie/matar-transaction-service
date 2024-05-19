package com.matar.transaction.controller.admin.platform;

import com.matar.transaction.dtos.transaction.TransactionDto;
import com.matar.transaction.dtos.transaction.TransactionFilter;
import com.matar.transaction.services.TransactionServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("admin-platform/transactions")
@RestController
@AllArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "admin-platform apis")
@PreAuthorize("hasAuthority('ADMIN')")
public class TransactionControllerForAdmin {

    private final TransactionServiceImpl transactionService;

    @GetMapping("/{id}")
    public TransactionDto getTransactionById(@PathVariable String id) {
        return transactionService.getTransaction(id);
    }

    @GetMapping
    public Page<TransactionDto> getTransactions(TransactionFilter filter,
                                                @ParameterObject
                                                @PageableDefault(
                                                        size = 10,
                                                        sort = {"updatedAt", "createdAt"},
                                                        direction = Sort.Direction.DESC) Pageable pageable) {
        return transactionService.getAll(filter, pageable);
    }
}
