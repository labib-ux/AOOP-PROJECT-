package com.nagorikseba.observer;

import com.nagorikseba.entity.Complaint;
import com.nagorikseba.entity.StatusUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AuditLogObserver implements ComplaintStatusObserver {

    private static final Logger log = LoggerFactory.getLogger(AuditLogObserver.class);

    @Override
    public void onStatusChange(Complaint complaint, StatusUpdate update) {
        log.debug("Audit: complaint {} {} -> {}", complaint.getId(), update.getFromStatus(), update.getToStatus());
    }
}
