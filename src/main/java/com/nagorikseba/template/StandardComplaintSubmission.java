package com.nagorikseba.template;

import com.nagorikseba.dto.ComplaintDTO;
import com.nagorikseba.entity.Complaint;
import com.nagorikseba.service.NotificationService;
import org.springframework.stereotype.Component;

@Component
public class StandardComplaintSubmission extends ComplaintSubmissionTemplate {

    private final NotificationService notificationService;

    public StandardComplaintSubmission(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    protected void validate(ComplaintDTO dto) {
        // TODO: standard validation
    }

    @Override
    protected void afterSubmit(Complaint complaint) {
        if (complaint.getWard() != null) {
            notificationService.notifyAdmins(complaint.getWard());
        }
    }
}
