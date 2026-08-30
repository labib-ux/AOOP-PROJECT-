package com.nagorikseba.dto;

import com.nagorikseba.enums.ComplaintCategory;
import com.nagorikseba.enums.Priority;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComplaintDTO {

    private String title;
    
    private String description;
    
    private ComplaintCategory category;
    
    private Priority priority;
    
    private Double latitude;
    
    private Double longitude;
    
    private Long wardId;
    
    @Builder.Default
    private List<MultipartFile> photos = List.of();
}
