package com.nagorikseba.dto;

import com.nagorikseba.entity.Attachment;
import com.nagorikseba.entity.StatusUpdate;
import com.nagorikseba.enums.ComplaintCategory;
import com.nagorikseba.enums.ComplaintStatus;
import com.nagorikseba.enums.Priority;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComplaintResponseDTO {

    private Long id;
    
    private String title;
    
    private String description;
    
    private ComplaintCategory category;
    
    private ComplaintStatus status;
    
    private Priority priority;
    
    private Double latitude;
    
    private Double longitude;
    
    private Long wardId;
    
    private String wardName;
    
    private LocalDateTime submittedAt;
    
    private LocalDateTime resolvedAt;
    
    private LocalDateTime deadlineAt;
    
    private Integer rating;
    
    private String ratingFeedback;
    
    private Integer reopenCount;
    
    private List<AttachmentDTO> attachments;
    
    private List<StatusUpdateDTO> statusUpdates;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AttachmentDTO {
        private Long id;
        private String fileUrl;
        private String fileType;
        private Boolean isWorkProof;
        private LocalDateTime uploadedAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StatusUpdateDTO {
        private Long id;
        private String fromStatus;
        private String toStatus;
        private String note;
        private LocalDateTime createdAt;
    }

    public static ComplaintResponseDTO fromEntity(com.nagorikseba.entity.Complaint complaint) {
        return ComplaintResponseDTO.builder()
                .id(complaint.getId())
                .title(complaint.getTitle())
                .description(complaint.getDescription())
                .category(complaint.getCategory())
                .status(complaint.getStatus())
                .priority(complaint.getPriority())
                .latitude(complaint.getLatitude())
                .longitude(complaint.getLongitude())
                .wardId(complaint.getWard() != null ? complaint.getWard().getId() : null)
                .wardName(complaint.getWard() != null ? complaint.getWard().getAreaName() : null)
                .submittedAt(complaint.getSubmittedAt())
                .resolvedAt(complaint.getResolvedAt())
                .deadlineAt(complaint.getDeadlineAt())
                .rating(complaint.getRating())
                .ratingFeedback(complaint.getRatingFeedback())
                .reopenCount(complaint.getReopenCount())
                .attachments(complaint.getAttachments().stream()
                        .map(a -> AttachmentDTO.builder()
                                .id(a.getId())
                                .fileUrl(a.getFileUrl())
                                .fileType(a.getFileType() != null ? a.getFileType().name() : null)
                                .isWorkProof(a.getIsWorkProof())
                                .uploadedAt(a.getUploadedAt())
                                .build())
                        .toList())
                .statusUpdates(complaint.getStatusUpdates().stream()
                        .map(s -> StatusUpdateDTO.builder()
                                .id(s.getId())
                                .fromStatus(s.getFromStatus())
                                .toStatus(s.getToStatus())
                                .note(s.getNote())
                                .createdAt(s.getCreatedAt())
                                .build())
                        .toList())
                .build();
    }
}
