package com.nagorikseba.state;

public class ResolvedState implements ComplaintState {
    @Override
    public String getStatusName() {
        return "RESOLVED";
    }
}
