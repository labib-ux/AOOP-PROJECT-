package com.nagorikseba.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "ward_performance")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WardPerformance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ward_id")
    @JsonIgnore
    private Ward ward;

    @Column(name = "`month`")
    private Integer month;

    @Column(name = "`year`")
    private Integer year;

    @Builder.Default
    private Integer totalComplaints = 0;

    @Builder.Default
    private Integer resolvedComplaints = 0;

    @Column(precision = 10, scale = 2)
    private BigDecimal avgResolutionHours;

    @Column(precision = 3, scale = 2)
    private BigDecimal avgRating;

    @Builder.Default
    private Integer slaBreachCount = 0;
}
