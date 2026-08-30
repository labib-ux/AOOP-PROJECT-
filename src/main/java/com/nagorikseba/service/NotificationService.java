package com.nagorikseba.service;

import com.nagorikseba.entity.User;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public void send(User user, String title, String message) {
        // TODO: SMS/email/in-app delivery
    }

    public void notifyAdmins(com.nagorikseba.entity.Ward ward) {
        // TODO: notify ward admins after submission
    }
}
