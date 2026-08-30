package com.nagorikseba.state;

public class SubmittedState implements ComplaintState {
    @Override
    public String getStatusName() {
        return "SUBMITTED";
    }
}
