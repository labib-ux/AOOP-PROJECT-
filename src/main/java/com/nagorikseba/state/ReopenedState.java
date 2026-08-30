package com.nagorikseba.state;

public class ReopenedState implements ComplaintState {
    @Override
    public String getStatusName() {
        return "REOPENED";
    }
}
