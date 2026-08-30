package com.nagorikseba.exception;

public class ComplaintNotFoundException extends RuntimeException {
    public ComplaintNotFoundException(Long id) {
        super("Complaint not found: " + id);
    }
}
