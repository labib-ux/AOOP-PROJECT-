package com.nagorikseba.state;

public class ClosedState implements ComplaintState {
    @Override
    public String getStatusName() {
        return "CLOSED";
    }
}
