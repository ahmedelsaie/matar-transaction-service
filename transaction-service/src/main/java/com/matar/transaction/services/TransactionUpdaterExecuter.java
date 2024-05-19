package com.matar.transaction.services;

import com.matar.transaction.model.trnsaction.Transaction;
import com.matar.transaction.repositories.TransactionRepository;
import com.matar.transaction.repositories.specifications.TransactionSpecification;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
public class TransactionUpdaterExecuter {

    private final TransactionRepository transactionRepository;

    private final TransactionServiceImpl transactionService;

    @Scheduled(fixedRate = 3600000)
    public void scheduleFixedRateTask() {
        log.info("Fixed rate task - " + System.currentTimeMillis() / 1000);

        List<Transaction> hangingTransactions = transactionRepository.
                findAll(TransactionSpecification.inHanginStates());

        hangingTransactions.forEach(this::updateTransaction);
    }

    private void updateTransaction(Transaction transaction) {
        try {
            log.info("trying to update transaction {}", transaction.getId());
            transactionService.updateTransaction(transaction.getId());
        } catch (Exception e) {
            log.info("could not update transaction of id {}", transaction.getId());
        }
    }
}
