package com.nagorikseba.template;

import com.nagorikseba.dto.ComplaintDTO;
import com.nagorikseba.entity.Complaint;
import org.springframework.stereotype.Component;

@Component
public class AnonymousComplaintSubmission extends ComplaintSubmissionTemplate {

    @Override
    protected void validate(ComplaintDTO dto) {
        // TODO: anonymous submissions require a phone number
    }

    @Override
    protected void afterSubmit(Complaint complaint) {
        // TODO: queue for review without notifying admins
    }
}
