package com.nagorikseba.service;

import com.nagorikseba.dto.ComplaintDTO;
import com.nagorikseba.entity.*;
import com.nagorikseba.enums.ComplaintStatus;
import com.nagorikseba.enums.FileType;
import com.nagorikseba.exception.InvalidStatusTransitionException;
import com.nagorikseba.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final WardRepository wardRepository;
    private final SLARuleRepository slaRuleRepository;
    private final FileStorageService fileStorageService;
    private final AttachmentRepository attachmentRepository;
    private final StatusUpdateRepository statusUpdateRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;

    // Define allowed status transitions
    private static final Set<String> ALLOWED_TRANSITIONS = Set.of(
            "SUBMITTED:VERIFIED",
            "SUBMITTED:REOPENED",
            "VERIFIED:ASSIGNED",
            "VERIFIED:RESOLVED",
            "ASSIGNED:IN_PROGRESS",
            "IN_PROGRESS:RESOLVED",
            "RESOLVED:CLOSED",
            "RESOLVED:REOPENED",
            "CLOSED:REOPENED",
            "REOPENED:VERIFIED",
            "REOPENED:ASSIGNED"
    );

    public ComplaintService(ComplaintRepository complaintRepository,
                           WardRepository wardRepository,
                           SLARuleRepository slaRuleRepository,
                           FileStorageService fileStorageService,
                           AttachmentRepository attachmentRepository,
                           StatusUpdateRepository statusUpdateRepository,
                           UserRepository userRepository,
                           DepartmentRepository departmentRepository) {
        this.complaintRepository = complaintRepository;
        this.wardRepository = wardRepository;
        this.slaRuleRepository = slaRuleRepository;
        this.fileStorageService = fileStorageService;
        this.attachmentRepository = attachmentRepository;
        this.statusUpdateRepository = statusUpdateRepository;
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
    }

    /**
     * Submit a new complaint with photos
     */
    public Complaint submitComplaint(ComplaintDTO dto, User citizen) throws IOException {
        // Get or determine ward
        Ward ward;
        if (dto.getWardId() != null) {
            ward = wardRepository.findById(dto.getWardId())
                    .orElseThrow(() -> new IllegalArgumentException("Ward not found: " + dto.getWardId()));
        } else if (dto.getLatitude() != null && dto.getLongitude() != null) {
            // Simple hardcoded ward detection based on lat/lng bounding box
            ward = detectWardFromLocation(dto.getLatitude(), dto.getLongitude());
        } else {
            throw new IllegalArgumentException("Either wardId or latitude/longitude must be provided");
        }

        // Create complaint entity
        Complaint complaint = Complaint.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .category(dto.getCategory())
                .priority(dto.getPriority() != null ? dto.getPriority() : com.nagorikseba.enums.Priority.NORMAL)
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .ward(ward)
                .citizen(citizen)
                .status(ComplaintStatus.SUBMITTED)
                .reopenCount(0)
                .build();

        // Save complaint first to get ID
        complaint = complaintRepository.save(complaint);

        // Calculate and set deadline based on SLA rules
        calculateAndSetDeadline(complaint);

        // Save attachments
        if (dto.getPhotos() != null && !dto.getPhotos().isEmpty()) {
            for (MultipartFile photo : dto.getPhotos()) {
                if (!photo.isEmpty()) {
                    String filePath = fileStorageService.saveFile(photo);
                    
                    Attachment attachment = Attachment.builder()
                            .complaint(complaint)
                            .fileUrl(filePath)
                            .fileType(determineFileType(photo.getContentType()))
                            .uploadedBy(citizen)
                            .isWorkProof(false)
                            .build();
                    
                    attachmentRepository.save(attachment);
                    complaint.addAttachment(attachment);
                }
            }
        }

        // Create initial status update
        StatusUpdate initialUpdate = StatusUpdate.builder()
                .complaint(complaint)
                .fromStatus(null)
                .toStatus(ComplaintStatus.SUBMITTED.name())
                .note("Complaint received.")
                .updatedBy(citizen)
                .build();
        
        statusUpdateRepository.save(initialUpdate);
        complaint.addStatusUpdate(initialUpdate);

        return complaintRepository.save(complaint);
    }

    /**
     * Get complaints for a specific citizen (paginated)
     */
    @Transactional(readOnly = true)
    public Page<Complaint> getCitizenComplaints(Long citizenId, Pageable pageable) {
        return complaintRepository.findByCitizenId(citizenId, pageable);
    }

    /**
     * Get a single complaint by ID with all details
     */
    @Transactional(readOnly = true)
    public Complaint getComplaintById(Long id) {
        return complaintRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Complaint not found: " + id));
    }

    /**
     * Validate and perform status transition
     */
    protected void validateStatusTransition(ComplaintStatus currentStatus, ComplaintStatus newStatus) {
        String transitionKey = currentStatus.name() + ":" + newStatus.name();
        if (!ALLOWED_TRANSITIONS.contains(transitionKey)) {
            throw new InvalidStatusTransitionException(currentStatus.name(), newStatus.name());
        }
    }

    /**
     * Update complaint status with validation
     */
    protected void updateComplaintStatus(Complaint complaint, ComplaintStatus newStatus, 
                                         User updatedBy, String note) {
        ComplaintStatus oldStatus = complaint.getStatus();
        validateStatusTransition(oldStatus, newStatus);
        
        complaint.setStatus(newStatus);
        
        if (newStatus == ComplaintStatus.RESOLVED) {
            complaint.setResolvedAt(LocalDateTime.now());
        }
        
        StatusUpdate statusUpdate = StatusUpdate.builder()
                .complaint(complaint)
                .fromStatus(oldStatus.name())
                .toStatus(newStatus.name())
                .note(note)
                .updatedBy(updatedBy)
                .build();
        
        statusUpdateRepository.save(statusUpdate);
        complaint.addStatusUpdate(statusUpdate);
        
        complaintRepository.save(complaint);
    }

    /**
     * Verify a complaint (WARD_COUNCILOR action)
     */
    public Complaint verifyComplaint(Long complaintId, User councilor) {
        Complaint complaint = getComplaintById(complaintId);
        updateComplaintStatus(complaint, ComplaintStatus.VERIFIED, councilor, "Complaint verified by ward councilor.");
        return complaint;
    }

    /**
     * Assign complaint to department and officer
     */
    public Complaint assignComplaint(Long complaintId, Long departmentId, Long officerId, User assignedBy) {
        Complaint complaint = getComplaintById(complaintId);
        
        Department department = null;
        if (departmentId != null) {
            department = departmentRepository.findById(departmentId)
                    .orElseThrow(() -> new IllegalArgumentException("Department not found: " + departmentId));
        }
        
        User officer = null;
        if (officerId != null) {
            officer = userRepository.findById(officerId)
                    .orElseThrow(() -> new IllegalArgumentException("Officer not found: " + officerId));
        }
        
        complaint.setAssignedDepartment(department);
        complaint.setAssignedOfficer(officer);
        
        String note = "Assigned to " + (department != null ? department.getName() : "unknown department");
        if (officer != null) {
            note += " and officer " + officer.getFullName();
        }
        
        updateComplaintStatus(complaint, ComplaintStatus.ASSIGNED, assignedBy, note);
        return complaint;
    }

    /**
     * Start working on a complaint (DEPT_OFFICER action)
     */
    public Complaint startComplaint(Long complaintId, User officer) {
        Complaint complaint = getComplaintById(complaintId);
        updateComplaintStatus(complaint, ComplaintStatus.IN_PROGRESS, officer, "Work started on complaint.");
        return complaint;
    }

    /**
     * Resolve a complaint with work-proof photos
     */
    public Complaint resolveComplaint(Long complaintId, User officer, List<MultipartFile> workProofPhotos) throws IOException {
        Complaint complaint = getComplaintById(complaintId);
        
        // Save work-proof photos
        if (workProofPhotos != null && !workProofPhotos.isEmpty()) {
            for (MultipartFile photo : workProofPhotos) {
                if (!photo.isEmpty()) {
                    String filePath = fileStorageService.saveFile(photo);
                    
                    Attachment attachment = Attachment.builder()
                            .complaint(complaint)
                            .fileUrl(filePath)
                            .fileType(determineFileType(photo.getContentType()))
                            .uploadedBy(officer)
                            .isWorkProof(true)
                            .build();
                    
                    attachmentRepository.save(attachment);
                    complaint.addAttachment(attachment);
                }
            }
        }
        
        updateComplaintStatus(complaint, ComplaintStatus.RESOLVED, officer, "Complaint resolved. Work proof uploaded.");
        return complaint;
    }

    /**
     * Close a complaint with citizen rating
     */
    public Complaint closeComplaint(Long complaintId, Integer rating, String feedback) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        
        Complaint complaint = getComplaintById(complaintId);
        complaint.setRating(rating);
        complaint.setRatingFeedback(feedback);
        complaint.setStatus(ComplaintStatus.CLOSED);
        
        StatusUpdate statusUpdate = StatusUpdate.builder()
                .complaint(complaint)
                .fromStatus(ComplaintStatus.RESOLVED.name())
                .toStatus(ComplaintStatus.CLOSED.name())
                .note("Complaint closed by citizen with rating: " + rating)
                .build();
        
        statusUpdateRepository.save(statusUpdate);
        complaint.addStatusUpdate(statusUpdate);
        
        return complaintRepository.save(complaint);
    }

    /**
     * Reopen a complaint
     */
    public Complaint reopenComplaint(Long complaintId, String reason, User citizen) {
        Complaint complaint = getComplaintById(complaintId);
        
        complaint.setReopenReason(reason);
        complaint.setReopenCount(complaint.getReopenCount() + 1);
        complaint.setPriority(com.nagorikseba.enums.Priority.HIGH);
        
        StatusUpdate statusUpdate = StatusUpdate.builder()
                .complaint(complaint)
                .fromStatus(complaint.getStatus().name())
                .toStatus(ComplaintStatus.REOPENED.name())
                .note("Complaint reopened by citizen. Reason: " + reason)
                .updatedBy(citizen)
                .build();
        
        statusUpdateRepository.save(statusUpdate);
        complaint.addStatusUpdate(statusUpdate);
        complaint.setStatus(ComplaintStatus.REOPENED);
        
        return complaintRepository.save(complaint);
    }

    /**
     * Get complaints for authority (filtered by ward/department)
     */
    @Transactional(readOnly = true)
    public List<Complaint> getAuthorityComplaints(User user) {
        switch (user.getRole()) {
            case WARD_COUNCILOR:
                if (user.getWard() != null) {
                    return complaintRepository.findByWardId(user.getWard().getId());
                }
                break;
            case DEPT_OFFICER:
                if (user.getDepartment() != null) {
                    return complaintRepository.findByAssignedDepartmentId(user.getDepartment().getId());
                }
                break;
            default:
                // For admins or other roles, return all
                return complaintRepository.findAll();
        }
        return List.of();
    }

    /**
     * Detect ward from latitude/longitude using simple bounding box
     * This is a placeholder - in production, use proper geospatial queries
     */
    private Ward detectWardFromLocation(Double latitude, Double longitude) {
        // Simple hardcoded example - adjust based on your city's actual ward boundaries
        // Example: Dhaka City Corporation wards
        
        // Default to first ward if no match
        return wardRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No ward found for location"));
    }

    /**
     * Calculate deadline based on SLA rules
     */
    private void calculateAndSetDeadline(Complaint complaint) {
        slaRuleRepository.findByCategoryAndPriority(complaint.getCategory(), complaint.getPriority())
                .ifPresentOrElse(
                        slaRule -> {
                            LocalDateTime deadline = complaint.getSubmittedAt().plusHours(slaRule.getMaxHours());
                            complaint.setDeadlineAt(deadline);
                        },
                        () -> {
                            // Default to 72 hours if no SLA rule found
                            complaint.setDeadlineAt(complaint.getSubmittedAt().plusHours(72));
                        }
                );
    }

    /**
     * Determine file type from content type
     */
    private FileType determineFileType(String contentType) {
        if (contentType == null) {
            return FileType.OTHER;
        }
        
        if (contentType.startsWith("image/")) {
            return FileType.IMAGE;
        } else if (contentType.startsWith("video/")) {
            return FileType.VIDEO;
        } else if (contentType.startsWith("audio/")) {
            return FileType.AUDIO;
        } else if (contentType.contains("pdf")) {
            return FileType.DOCUMENT;
        }
        
        return FileType.OTHER;
    }
}
