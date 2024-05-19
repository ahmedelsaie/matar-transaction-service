package com.matar.transaction.gateway;

import com.matar.transaction.model.trnsaction.State;
import com.matar.transaction.model.trnsaction.Transaction;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class Gateway {

    public GateWayResponse createTransaction(Transaction transaction) {
        return new GateWayResponse(getGateWayId(), getCheckoutId(), State.INITIATED);
    }

    public GateWayResponse getTransactionStatus(Transaction transaction) {
        if (transaction.getState() == State.INITIATED) {
            return new GateWayResponse(transaction.getPaymentGatewayId(),
                    transaction.getPaymentGatewayCheckoutId(), State.PENDING);
        }

        if (transaction.getState() == State.PENDING) {
            return new GateWayResponse(transaction.getPaymentGatewayId(),
                    transaction.getPaymentGatewayCheckoutId(), State.SUCCESSFUL);
        }

        return new GateWayResponse(transaction.getPaymentGatewayId(),
                transaction.getPaymentGatewayCheckoutId(), transaction.getState());
    }

    private String getGateWayId() {
        return "gateWay-" + UUID.randomUUID();
    }

    private String getCheckoutId() {
        return "checkoutId-" + UUID.randomUUID();
    }
}
