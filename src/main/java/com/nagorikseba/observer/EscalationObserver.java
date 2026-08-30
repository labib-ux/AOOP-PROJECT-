package com.nagorikseba.observer;

import com.nagorikseba.entity.Complaint;
import com.nagorikseba.entity.StatusUpdate;
import org.springframework.stereotype.Component;

@Component
public class EscalationObserver implements ComplaintStatusObserver {

    @Override
    public void onStatusChange(Complaint complaint, StatusUpdate update) {
        // TODO: escalate to councilor when status is REOPENED
    }
}
