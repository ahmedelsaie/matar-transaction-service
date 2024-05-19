package com.matar.transaction.services;

import com.matar.transaction.DomainEvent.TransactionChangedEvent;
import com.matar.transaction.DomainEvent.TransactionCreatedEvent;
import com.matar.transaction.dtos.transaction.TransactionDto;
import com.matar.transaction.dtos.transaction.TransactionFilter;
import com.matar.transaction.dtos.user.CreateTransactionDto;
import com.matar.transaction.exceptions.EntityNotFoundException;
import com.matar.transaction.exceptions.HasAlreadyTransactionException;
import com.matar.transaction.exceptions.TransactionStateException;
import com.matar.transaction.gateway.GateWayResponse;
import com.matar.transaction.gateway.Gateway;
import com.matar.transaction.model.trnsaction.State;
import com.matar.transaction.model.trnsaction.Transaction;
import com.matar.transaction.repositories.TransactionRepository;
import com.matar.transaction.repositories.specifications.TransactionSpecification;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class TransactionServiceImpl {

    private final TransactionRepository transactionRepository;

    private final ModelMapper modelMapper;

    private final Gateway gateway;

    private final KafkaPublisherServiceImpl kafkaPublisherService;

    @Transactional
    public TransactionDto createTransaction(CreateTransactionDto createTransactionDto) {
        Transaction transaction = getAlreadyInitiatedTransaction(createTransactionDto).
                orElseGet(() -> createNewTransaction(createTransactionDto));

        return modelMapper.map(transaction, TransactionDto.class);
    }

    private Optional<Transaction> getAlreadyInitiatedTransaction(CreateTransactionDto createTransactionDto) {
        List<Transaction> hangingTransactions = transactionRepository.
                findAll(TransactionSpecification.inHanginStates(createTransactionDto.getUserId(),
                        createTransactionDto.getDomainId()));

        ensureNoPendingTrasnaction(hangingTransactions);

        return hangingTransactions.stream().filter(e -> e.getState() == State.INITIATED).findFirst();
    }

    private Transaction createNewTransaction(CreateTransactionDto createTransactionDto) {
        Transaction transaction = new Transaction();
        modelMapper.map(createTransactionDto, transaction);

        GateWayResponse gateWayResponse = gateway.createTransaction(transaction);

        transaction.start(gateWayResponse);

        transaction = transactionRepository.save(transaction);
        kafkaPublisherService.publish(new TransactionCreatedEvent(transaction), "topic2");

        return transaction;
    }

    private void ensureNoPendingTrasnaction(List<Transaction> hangingTransactions) {
        hangingTransactions.stream().filter(e -> e.getState() == State.PENDING).findFirst().
                ifPresent(transaction -> {
                    throw new HasAlreadyTransactionException(transaction);
                });
    }

    public TransactionDto getTransaction(String id, Integer userId) {
        return transactionRepository.findByIdAndUserId(id, userId).
                map(e -> modelMapper.map(e, TransactionDto.class)).
                orElseThrow(() -> new EntityNotFoundException(Transaction.class, "id", id));
    }

    public TransactionDto getTransaction(String id) {
        return modelMapper.map(getById(id), TransactionDto.class);
    }

    public Page<TransactionDto> getAll(TransactionFilter transactionFilter, Pageable pageable) {
        Specification<Transaction> specification = TransactionSpecification.
                getUserSearchSpecification(transactionFilter);

        return transactionRepository
                .findAll(specification, pageable)
                .map(transaction -> modelMapper.map(transaction, TransactionDto.class));
    }

    @Transactional
    public TransactionDto updateTransaction(String id) {
        Transaction transaction = getById(id);

        if (transaction.getState().isTerminalState())
            throw new TransactionStateException("transaction already in terminal state");

        GateWayResponse gateWayResponse = gateway.getTransactionStatus(transaction);

        return modelMapper.map(updateTransaction(transaction, gateWayResponse), TransactionDto.class);
    }

    private Transaction updateTransaction(Transaction transaction, GateWayResponse gateWayResponse) {
        if (gateWayResponse.getState() == transaction.getState())
            return transaction;

        transaction.updateWithGateWay(gateWayResponse);
        kafkaPublisherService.publish(new TransactionChangedEvent(transaction.getState(), transaction),
                "topic3");

        return transactionRepository.save(transaction);
    }

    private Transaction getById(String id) {
        return transactionRepository.
                findById(id).
                orElseThrow(() -> new EntityNotFoundException(Transaction.class, "id", id));
    }
}
