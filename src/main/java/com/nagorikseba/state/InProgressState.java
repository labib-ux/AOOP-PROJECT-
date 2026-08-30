package com.nagorikseba.state;

public class InProgressState implements ComplaintState {
    @Override
    public String getStatusName() {
        return "IN_PROGRESS";
    }
}
