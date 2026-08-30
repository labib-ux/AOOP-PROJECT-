package com.nagorikseba.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WardPerformanceDTO {
    private Long wardId;
    private String areaName;
    private Integer month;
    private Integer year;
    private Integer totalComplaints;
    private Integer resolvedComplaints;
    private BigDecimal avgResolutionHours;
    private BigDecimal avgRating;
    private Integer slaBreachCount;
}
