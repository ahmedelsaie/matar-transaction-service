package com.matar.transaction.model.trnsaction;

import com.matar.transaction.gateway.GateWayResponse;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@ToString
public class Transaction {

    @Id
    private String id;

    @Enumerated(EnumType.STRING)
    private State state = State.INITIATED;

    @Column
    private Integer userId;

    private String domainId;

    private BigDecimal money;

    private String paymentGatewayCheckoutId;

    private String paymentGatewayId;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

    public Transaction() {
        this.id = UUID.randomUUID().toString();
    }

    public void start(GateWayResponse gateWayResponse) {
        this.setPaymentGatewayCheckoutId(gateWayResponse.getPaymentGatewayCheckoutId());
        this.setPaymentGatewayId(gateWayResponse.getId());
        this.setState(gateWayResponse.getState());
    }

    public void updateWithGateWay(GateWayResponse gateWayResponse) {
        if (gateWayResponse.getState() == this.getState())
            throw new RuntimeException("No Update in state needed");
        this.setState(gateWayResponse.getState());
    }
}
