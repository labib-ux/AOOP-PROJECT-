package com.nagorikseba.controller;

import com.nagorikseba.dto.ComplaintDTO;
import com.nagorikseba.entity.Complaint;
import com.nagorikseba.service.ComplaintService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

    private final ComplaintService complaintService;

    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    @PostMapping
    public ResponseEntity<Complaint> submit(@Valid @RequestBody ComplaintDTO dto, Authentication authentication) {
        return notImplemented();
    }

    @GetMapping("/my")
    public ResponseEntity<List<Complaint>> myComplaints(Authentication authentication) {
        return ResponseEntity.ok(complaintService.findMyComplaints(authentication.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Complaint> getById(@PathVariable Long id) {
        return notImplemented();
    }

    @PostMapping("/{id}/rate")
    public ResponseEntity<Complaint> rate(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        return notImplemented();
    }

    @PostMapping("/{id}/reopen")
    public ResponseEntity<Complaint> reopen(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return notImplemented();
    }

    @PostMapping("/{id}/verify")
    public ResponseEntity<Complaint> verify(@PathVariable Long id) {
        return notImplemented();
    }

    @PostMapping("/{id}/assign")
    public ResponseEntity<Complaint> assign(@PathVariable Long id, @RequestBody Map<String, Long> body) {
        return notImplemented();
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<Complaint> start(@PathVariable Long id) {
        return notImplemented();
    }

    @PostMapping("/{id}/resolve")
    public ResponseEntity<Complaint> resolve(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return notImplemented();
    }

    @PostMapping("/{id}/upload-proof")
    public ResponseEntity<Complaint> uploadProof(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        return notImplemented();
    }

    private <T> ResponseEntity<T> notImplemented() {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
