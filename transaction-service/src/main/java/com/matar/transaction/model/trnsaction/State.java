package com.matar.transaction.model.trnsaction;

import lombok.Getter;

import java.util.EnumSet;

@Getter
public enum State {
    INITIATED(0), PENDING(1), SUCCESSFUL(2), DECLINED(2);

    private final int rank;

    public static EnumSet<State> getTerminalState() {
        return EnumSet.of(SUCCESSFUL, DECLINED);
    }

    State(int rank) {
        this.rank = rank;
    }

    public static EnumSet<State> getHangingStates() {
        return EnumSet.of(INITIATED, PENDING);
    }

    public boolean isTerminalState() {
        return getTerminalState().contains(this);
    }

    public boolean isHangingState() {
        return getHangingStates().contains(this);
    }

}
