package com.matar.transaction.controller.user.platform;

import com.matar.transaction.controller.user.platform.dto.CreateTransactionDtoForUser;
import com.matar.transaction.controller.user.platform.dto.TransactionFilterForUser;
import com.matar.transaction.dtos.transaction.TransactionDto;
import com.matar.transaction.dtos.transaction.TransactionFilter;
import com.matar.transaction.dtos.user.CreateTransactionDto;
import com.matar.transaction.model.user.User;
import com.matar.transaction.services.TransactionServiceImpl;
import com.matar.transaction.services.user.AuthenticationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("user-platform/transactions")
@RestController
@AllArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "user-platform apis")
@PreAuthorize("hasAuthority('USER')")
public class TransactionController {

    private final TransactionServiceImpl transactionService;

    private final AuthenticationService authenticationService;

    private final ModelMapper modelMapper;

    @PostMapping
    public TransactionDto createTransaction(@RequestBody @Valid CreateTransactionDtoForUser createTransactionDtoForUser) {
        User currentUser = authenticationService.getCurrentAuthenticatedUser();
        CreateTransactionDto createTransactionDto =
                modelMapper.map(createTransactionDtoForUser, CreateTransactionDto.class);
        createTransactionDto.setUserId(currentUser.getId());
        return transactionService.createTransaction(createTransactionDto);
    }

    @GetMapping("/{id}")
    public TransactionDto getTransactionById(@PathVariable String id) {
        User currentUser = authenticationService.getCurrentAuthenticatedUser();
        return transactionService.getTransaction(id, currentUser.getId());
    }

    @GetMapping
    public Page<TransactionDto> getTransactions(TransactionFilterForUser filter,
                                                @PageableDefault(
                                                        size = 10,
                                                        sort = {"updatedAt", "createdAt"},
                                                        direction = Sort.Direction.DESC) Pageable pageable) {
        TransactionFilter transactionFilter =
                modelMapper.map(filter, TransactionFilter.class);
        User currentUser = authenticationService.getCurrentAuthenticatedUser();
        transactionFilter.setUserId(currentUser.getId());
        return transactionService.getAll(transactionFilter, pageable);
    }
}
