package com.nagorikseba.template;

import com.nagorikseba.dto.ComplaintDTO;
import com.nagorikseba.entity.Complaint;
import com.nagorikseba.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public abstract class ComplaintSubmissionTemplate {

    public final Complaint submit(ComplaintDTO dto, User citizen, List<MultipartFile> files) {
        validate(dto);
        Complaint complaint = createComplaint(dto, citizen);
        saveAttachments(complaint, files);
        assignWard(complaint);
        afterSubmit(complaint);
        return complaint;
    }

    protected abstract void validate(ComplaintDTO dto);

    protected abstract void afterSubmit(Complaint complaint);

    protected Complaint createComplaint(ComplaintDTO dto, User citizen) {
        throw new UnsupportedOperationException("Complaint creation is not implemented yet");
    }

    protected void saveAttachments(Complaint complaint, List<MultipartFile> files) {
        // TODO: persist uploaded photos
    }

    protected void assignWard(Complaint complaint) {
        // TODO: auto-detect ward from lat/lng
    }
}
