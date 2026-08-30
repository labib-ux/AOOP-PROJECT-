package com.nagorikseba.observer;

import com.nagorikseba.entity.Complaint;
import com.nagorikseba.entity.StatusUpdate;
import com.nagorikseba.service.NotificationService;
import org.springframework.stereotype.Component;

@Component
public class NotificationObserver implements ComplaintStatusObserver {

    private final NotificationService notificationService;

    public NotificationObserver(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void onStatusChange(Complaint complaint, StatusUpdate update) {
        if (complaint.getCitizen() != null) {
            notificationService.send(
                    complaint.getCitizen(),
                    "Complaint update",
                    "Your complaint #" + complaint.getId() + " is now " + update.getToStatus()
            );
        }
    }
}
