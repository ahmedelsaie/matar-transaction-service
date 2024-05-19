package com.matar.transaction.repositories.specifications;

import com.matar.transaction.dtos.transaction.TransactionFilter;
import com.matar.transaction.model.trnsaction.State;
import com.matar.transaction.model.trnsaction.Transaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import java.util.EnumSet;

@NoArgsConstructor
@Slf4j
public class TransactionSpecification {
    public static Specification<Transaction> hasUserId(Integer userId) {
        if (userId == null) {
            return Specification.where(null);
        }

        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("userId"), userId);
    }

    public static Specification<Transaction> hasDomainId(String domainId) {
        if (domainId == null) {
            return Specification.where(null);
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("domainId"), domainId);
    }

    public static Specification<Transaction> hasState(EnumSet<State> state) {
        if (state == null || state.isEmpty()) {
            return Specification.where(null);
        }
        return (root, query, criteriaBuilder) -> {
            CriteriaBuilder.In<State> inClause = criteriaBuilder.in(root.get("state"));
            state.forEach(inClause::value);
            return inClause;
        };
    }


    public static Specification<Transaction> getUserSearchSpecification(TransactionFilter filter) {
        return TransactionSpecification.hasDomainId(filter.getDomainId())
                .and(TransactionSpecification.hasUserId(filter.getUserId()))
                .and(TransactionSpecification.hasState(filter.getState()));
    }


    public static Specification<Transaction> inHanginStates() {
        return (root, query, criteriaBuilder) -> {
            CriteriaBuilder.In<State> inClause = criteriaBuilder.in(root.get("state"));
            State.getHangingStates().forEach(inClause::value);
            return inClause;
        };
    }

    public static Specification<Transaction> inHanginStates(Integer userId, String domainId) {
        return TransactionSpecification.hasDomainId(domainId)
                .and(TransactionSpecification.hasUserId(userId))
                .and(TransactionSpecification.inHanginStates());
    }

}
