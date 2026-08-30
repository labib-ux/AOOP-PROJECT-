package com.nagorikseba.controller;

import com.nagorikseba.entity.Complaint;
import com.nagorikseba.service.ComplaintService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/citizen")
public class CitizenController {

    private final ComplaintService complaintService;

    public CitizenController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<List<Complaint>> dashboard(Authentication authentication) {
        return ResponseEntity.ok(complaintService.findMyComplaints(authentication.getName()));
    }
}
