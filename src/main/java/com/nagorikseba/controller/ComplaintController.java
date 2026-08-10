package com.nagorikseba.controller;

import com.nagorikseba.dto.ComplaintDTO;
import com.nagorikseba.dto.ComplaintResponseDTO;
import com.nagorikseba.entity.Complaint;
import com.nagorikseba.entity.User;
import com.nagorikseba.repository.UserRepository;
import com.nagorikseba.service.ComplaintService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

    private final ComplaintService complaintService;
    private final UserRepository userRepository;

    public ComplaintController(ComplaintService complaintService, UserRepository userRepository) {
        this.complaintService = complaintService;
        this.userRepository = userRepository;
    }

    /**
     * Submit a new complaint (Citizen only)
     * POST /api/complaints
     */
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ComplaintResponseDTO> submitComplaint(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("category") String category,
            @RequestParam(value = "priority", required = false, defaultValue = "NORMAL") String priority,
            @RequestParam(value = "latitude", required = false) Double latitude,
            @RequestParam(value = "longitude", required = false) Double longitude,
            @RequestParam(value = "wardId", required = false) Long wardId,
            @RequestParam(value = "photos", required = false) List<MultipartFile> photos,
            @AuthenticationPrincipal UserDetails userDetails) throws IOException {

        // Get the logged-in citizen user
        User citizen = getUserFromUserDetails(userDetails);

        // Build DTO
        ComplaintDTO dto = ComplaintDTO.builder()
                .title(title)
                .description(description)
                .category(com.nagorikseba.enums.ComplaintCategory.valueOf(category.toUpperCase()))
                .priority(priority != null ? com.nagorikseba.enums.Priority.valueOf(priority.toUpperCase()) : com.nagorikseba.enums.Priority.NORMAL)
                .latitude(latitude)
                .longitude(longitude)
                .wardId(wardId)
                .photos(photos != null ? photos : List.of())
                .build();

        Complaint complaint = complaintService.submitComplaint(dto, citizen);
        return ResponseEntity.ok(ComplaintResponseDTO.fromEntity(complaint));
    }

    /**
     * Get logged-in citizen's complaints (paginated)
     * GET /api/complaints/my
     */
    @GetMapping("/my")
    public ResponseEntity<Page<ComplaintResponseDTO>> getMyComplaints(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        User citizen = getUserFromUserDetails(userDetails);
        Pageable pageable = PageRequest.of(page, size);
        
        Page<Complaint> complaints = complaintService.getCitizenComplaints(citizen.getId(), pageable);
        
        return ResponseEntity.ok(complaints.map(ComplaintResponseDTO::fromEntity));
    }

    /**
     * Get single complaint by ID with status timeline and attachments
     * GET /api/complaints/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ComplaintResponseDTO> getComplaint(@PathVariable Long id) {
        Complaint complaint = complaintService.getComplaintById(id);
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
