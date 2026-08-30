package com.nagorikseba.observer;

import com.nagorikseba.entity.Complaint;
import com.nagorikseba.entity.StatusUpdate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ComplaintStatusPublisher {

    private final List<ComplaintStatusObserver> observers;

    public ComplaintStatusPublisher(List<ComplaintStatusObserver> observers) {
        this.observers = observers;
    }

    public void notifyStatusChange(Complaint complaint, StatusUpdate update) {
        observers.forEach(observer -> observer.onStatusChange(complaint, update));
    }
}
