package com.matar.transaction.gateway;

import com.matar.transaction.model.trnsaction.State;
import lombok.Data;
import lombok.NonNull;

@Data
public class GateWayResponse {

    @NonNull
    private final String id;

    @NonNull
    private final String paymentGatewayCheckoutId;

    @NonNull
    private final State state;
}
