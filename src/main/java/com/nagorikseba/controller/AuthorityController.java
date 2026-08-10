package com.nagorikseba.controller;

import com.nagorikseba.dto.ComplaintResponseDTO;
import com.nagorikseba.entity.Complaint;
import com.nagorikseba.entity.User;
import com.nagorikseba.repository.UserRepository;
import com.nagorikseba.service.ComplaintService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/authority")
public class AuthorityController {

    private final ComplaintService complaintService;
    private final UserRepository userRepository;

    public AuthorityController(ComplaintService complaintService, UserRepository userRepository) {
        this.complaintService = complaintService;
        this.userRepository = userRepository;
    }

    /**
     * Get complaints for authority (filtered by ward/department)
     * GET /api/authority/complaints
     * Secured for WARD_COUNCILOR and DEPT_OFFICER
     */
    @GetMapping("/complaints")
    @PreAuthorize("hasAnyRole('WARD_COUNCILOR', 'DEPT_OFFICER', 'ADMIN')")
    public ResponseEntity<List<ComplaintResponseDTO>> getAuthorityComplaints(
            @AuthenticationPrincipal UserDetails userDetails) {
        
        User user = getUserFromUserDetails(userDetails);
        List<Complaint> complaints = complaintService.getAuthorityComplaints(user);
        return ResponseEntity.ok(complaints.stream()
                .map(ComplaintResponseDTO::fromEntity)
                .toList());
    }

    /**
     * Verify a complaint (WARD_COUNCILOR action)
     * POST /api/complaints/{id}/verify
     */
    @PostMapping("/complaints/{id}/verify")
    @PreAuthorize("hasRole('WARD_COUNCILOR') or hasRole('ADMIN')")
    public ResponseEntity<ComplaintResponseDTO> verifyComplaint(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        User councilor = getUserFromUserDetails(userDetails);
        Complaint complaint = complaintService.verifyComplaint(id, councilor);
        return ResponseEntity.ok(ComplaintResponseDTO.fromEntity(complaint));
    }

    /**
     * Assign complaint to department and officer (WARD_COUNCILOR action)
     * POST /api/complaints/{id}/assign
     */
    @PostMapping("/complaints/{id}/assign")
    @PreAuthorize("hasRole('WARD_COUNCILOR') or hasRole('ADMIN')")
    public ResponseEntity<ComplaintResponseDTO> assignComplaint(
            @PathVariable Long id,
            @RequestParam(value = "departmentId", required = false) Long departmentId,
            @RequestParam(value = "officerId", required = false) Long officerId,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        User assignedBy = getUserFromUserDetails(userDetails);
        Complaint complaint = complaintService.assignComplaint(id, departmentId, officerId, assignedBy);
        return ResponseEntity.ok(ComplaintResponseDTO.fromEntity(complaint));
    }

    /**
     * Start working on a complaint (DEPT_OFFICER action)
     * POST /api/complaints/{id}/start
     */
    @PostMapping("/complaints/{id}/start")
    @PreAuthorize("hasRole('DEPT_OFFICER') or hasRole('ADMIN')")
    public ResponseEntity<ComplaintResponseDTO> startComplaint(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        User officer = getUserFromUserDetails(userDetails);
        Complaint complaint = complaintService.startComplaint(id, officer);
        return ResponseEntity.ok(ComplaintResponseDTO.fromEntity(complaint));
    }

    /**
     * Resolve a complaint with work-proof photos (DEPT_OFFICER action)
     * POST /api/complaints/{id}/resolve
     */
    @PostMapping("/complaints/{id}/resolve")
    @PreAuthorize("hasRole('DEPT_OFFICER') or hasRole('ADMIN')")
    public ResponseEntity<ComplaintResponseDTO> resolveComplaint(
            @PathVariable Long id,
            @RequestParam(value = "workProofPhotos", required = false) List<MultipartFile> workProofPhotos,
            @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        
        User officer = getUserFromUserDetails(userDetails);
        Complaint complaint = complaintService.resolveComplaint(id, officer, workProofPhotos);
        return ResponseEntity.ok(ComplaintResponseDTO.fromEntity(complaint));
    }

    /**
     * Helper method to get User entity from UserDetails
     */
    private User getUserFromUserDetails(UserDetails userDetails) {
        return userRepository.findByEmailOrPhone(userDetails.getUsername(), userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userDetails.getUsername()));
    }
}
