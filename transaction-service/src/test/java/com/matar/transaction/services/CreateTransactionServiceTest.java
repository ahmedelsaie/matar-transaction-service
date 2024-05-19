package com.matar.transaction.services;

import com.matar.transaction.DomainEvent.TransactionCreatedEvent;
import com.matar.transaction.dtos.transaction.TransactionDto;
import com.matar.transaction.dtos.user.CreateTransactionDto;
import com.matar.transaction.model.trnsaction.State;
import com.matar.transaction.model.trnsaction.Transaction;
import com.matar.transaction.exceptions.HasAlreadyTransactionException;
import com.matar.transaction.repositories.TransactionRepository;
import com.matar.transaction.gateway.Gateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CreateTransactionServiceTest {

    @InjectMocks
    TransactionServiceImpl transactionService;

    @Spy
    Gateway gateway;

    @Mock
    KafkaPublisherServiceImpl kafkaPublisherService;

    @Spy
    ModelMapper modelMapper;

    @Mock
    TransactionRepository transactionRepository;

    CreateTransactionDto createTransactionDto;

    @BeforeEach
    public void before() {
        createTransactionDto = new CreateTransactionDto();
        createTransactionDto.setUserId(5);
        createTransactionDto.setDomainId("47");
        createTransactionDto.setMoney(BigDecimal.valueOf(500));

        when(transactionRepository.save(any())).thenAnswer(
                i -> i.<Transaction>getArgument(0));
    }

    @Test
    public void whenCreatingNewTransaction_alreadyOneIsInitiated_thenRetunrIt() {
        Transaction previousTransaction = preparePreviousTransaction(State.INITIATED);

        TransactionDto transactionDto = transactionService.createTransaction(createTransactionDto);

        Assertions.assertEquals(transactionDto.getId(), previousTransaction.getId());
        assertNoNewTransactionCreated();
    }

    @Test
    public void whenCreatingNewTransaction_alreadyOneIsPending_thenExceptionIsThrown() {
        preparePreviousTransaction(State.PENDING);

        Assertions.assertThrows(HasAlreadyTransactionException.class, () -> transactionService.createTransaction(createTransactionDto));

        assertNoNewTransactionCreated();
    }

    @Test
    public void whenCreatingNewTransaction_noHangingTransactions_newTransactionIsCreated() {
        when(transactionRepository.findAll(any())).thenReturn(Collections.EMPTY_LIST);

        TransactionDto transactionDto = transactionService.createTransaction(createTransactionDto);

        verify(kafkaPublisherService, times(1)).publish(any(TransactionCreatedEvent.class), any());
        verify(gateway, times(1)).createTransaction(any(Transaction.class));
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    private void assertNoNewTransactionCreated() {
        verify(transactionRepository, times(0)).save(any());
        verifyNoInteractions(kafkaPublisherService, gateway);
    }

    private Transaction preparePreviousTransaction(State state) {
        Transaction transaction = new Transaction();
        transaction.setState(state);
        when(transactionRepository.findAll(any())).thenReturn(List.of(transaction));
        return transaction;
    }
}
