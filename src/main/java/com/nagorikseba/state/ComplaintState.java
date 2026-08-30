package com.nagorikseba.state;

import com.nagorikseba.entity.Complaint;
import com.nagorikseba.entity.Department;
import com.nagorikseba.entity.User;
import com.nagorikseba.exception.InvalidStateTransitionException;

public interface ComplaintState {

    default void submit(Complaint complaint) {
        throw invalid();
    }

    default void verify(Complaint complaint, User officer) {
        throw invalid();
    }

    default void assign(Complaint complaint, Department dept, User officer) {
        throw invalid();
    }

    default void startWork(Complaint complaint, User officer) {
        throw invalid();
    }

    default void resolve(Complaint complaint, User officer, String note) {
        throw invalid();
    }

    default void close(Complaint complaint, User citizen, int rating) {
        throw invalid();
    }

    default void reopen(Complaint complaint, User citizen, String reason) {
        throw invalid();
    }

    String getStatusName();

    private static InvalidStateTransitionException invalid() {
        return new InvalidStateTransitionException("This status transition is not allowed");
    }
}
