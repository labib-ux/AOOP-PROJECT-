package com.nagorikseba.service;

import com.nagorikseba.dto.ComplaintDTO;
import com.nagorikseba.entity.Complaint;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@Service
public class ComplaintService {

    public Complaint submit(ComplaintDTO dto, List<MultipartFile> files, String username) {
        throw new UnsupportedOperationException("Complaint submission is not implemented yet");
    }

    public List<Complaint> findMyComplaints(String username) {
        return Collections.emptyList();
    }

    public Complaint findById(Long id) {
        throw new UnsupportedOperationException("Complaint lookup is not implemented yet");
    }

    public Complaint rateAndClose(Long id, int rating, String feedback, String username) {
        throw new UnsupportedOperationException("Rating is not implemented yet");
    }

    public Complaint reopen(Long id, String reason, String username) {
        throw new UnsupportedOperationException("Reopen is not implemented yet");
    }

    public Complaint verify(Long id, String username) {
        throw new UnsupportedOperationException("Verify is not implemented yet");
    }

    public Complaint assign(Long id, Long departmentId, Long officerId, String username) {
        throw new UnsupportedOperationException("Assign is not implemented yet");
    }

    public Complaint startWork(Long id, String username) {
        throw new UnsupportedOperationException("Start work is not implemented yet");
    }

    public Complaint resolve(Long id, String note, String username) {
        throw new UnsupportedOperationException("Resolve is not implemented yet");
    }

    public Complaint uploadProof(Long id, MultipartFile file, String username) {
        throw new UnsupportedOperationException("Work proof upload is not implemented yet");
    }

    public List<Complaint> findForMap() {
        return Collections.emptyList();
    }

    public List<Complaint> findWardComplaints(String username) {
        return Collections.emptyList();
    }
}
