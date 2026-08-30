package com.nagorikseba.dto;

import com.nagorikseba.enums.ComplaintCategory;
import com.nagorikseba.enums.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintDTO {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotNull
    private ComplaintCategory category;

    private Priority priority;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private Long wardId;
}
