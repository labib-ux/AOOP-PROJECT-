package com.nagorikseba.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nagorikseba.enums.ComplaintCategory;
import com.nagorikseba.enums.ComplaintStatus;
import com.nagorikseba.enums.Priority;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "complaints")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ComplaintCategory category;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ComplaintStatus status = ComplaintStatus.SUBMITTED;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Priority priority = Priority.NORMAL;

    @Column(precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(precision = 11, scale = 8)
    private BigDecimal longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ward_id")
    @JsonIgnore
    private Ward ward;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "citizen_id")
    @JsonIgnore
    private User citizen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_department_id")
    @JsonIgnore
    private Department assignedDepartment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_officer_id")
    @JsonIgnore
    private User assignedOfficer;

    @Column(nullable = false, updatable = false)
    private LocalDateTime submittedAt;

    private LocalDateTime resolvedAt;

    private LocalDateTime deadlineAt;

    @Column(columnDefinition = "INTEGER CHECK (rating BETWEEN 1 AND 5)")
    private Integer rating;

    @Column(columnDefinition = "TEXT")
    private String ratingFeedback;

    @Column(columnDefinition = "TEXT")
    private String reopenReason;

    @Builder.Default
    @Column(nullable = false)
    private Integer reopenCount = 0;

    @Builder.Default
    @OneToMany(mappedBy = "complaint", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<StatusUpdate> statusUpdates = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "complaint", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Attachment> attachments = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (submittedAt == null) {
            submittedAt = LocalDateTime.now();
        }
        if (status == null) {
            status = ComplaintStatus.SUBMITTED;
        }
        if (priority == null) {
            priority = Priority.NORMAL;
        }
        if (reopenCount == null) {
            reopenCount = 0;
        }
    }

    public void addStatusUpdate(StatusUpdate update) {
        statusUpdates.add(update);
        update.setComplaint(this);
    }

    public void addAttachment(Attachment attachment) {
        attachments.add(attachment);
        attachment.setComplaint(this);
    }
}
