package com.nagorikseba.state;

public class VerifiedState implements ComplaintState {
    @Override
    public String getStatusName() {
        return "VERIFIED";
    }
}
