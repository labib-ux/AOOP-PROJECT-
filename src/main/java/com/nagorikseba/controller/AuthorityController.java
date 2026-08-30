package com.nagorikseba.controller;

import com.nagorikseba.entity.Complaint;
import com.nagorikseba.service.ComplaintService;
import com.nagorikseba.service.WardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/authority")
public class AuthorityController {

    private final WardService wardService;
    private final ComplaintService complaintService;

    public AuthorityController(WardService wardService, ComplaintService complaintService) {
        this.wardService = wardService;
        this.complaintService = complaintService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> dashboard(Authentication authentication) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @GetMapping("/complaints")
    public ResponseEntity<List<Complaint>> complaints(Authentication authentication) {
        return ResponseEntity.ok(complaintService.findWardComplaints(authentication.getName()));
    }
}
