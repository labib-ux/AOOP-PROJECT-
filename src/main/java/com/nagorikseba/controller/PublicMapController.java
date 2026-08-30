package com.nagorikseba.controller;

import com.nagorikseba.entity.Complaint;
import com.nagorikseba.entity.Ward;
import com.nagorikseba.service.ComplaintService;
import com.nagorikseba.service.WardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/public")
public class PublicMapController {

    private final ComplaintService complaintService;
    private final WardService wardService;

    public PublicMapController(ComplaintService complaintService, WardService wardService) {
        this.complaintService = complaintService;
        this.wardService = wardService;
    }

    @GetMapping("/complaints/map")
    public ResponseEntity<List<Complaint>> mapComplaints() {
        return ResponseEntity.ok(complaintService.findForMap());
    }

    @GetMapping("/wards")
    public ResponseEntity<List<WardSummary>> wards() {
        List<WardSummary> wards = wardService.findAll().stream()
                .map(w -> new WardSummary(w.getId(), w.getWardNumber(), w.getAreaName(), w.getCityCorporation()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(wards);
    }

    @GetMapping("/wards/{id}/performance")
    public ResponseEntity<?> wardPerformance(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    public record WardSummary(Long id, Integer wardNumber, String areaName, String cityCorporation) {
    }
}
