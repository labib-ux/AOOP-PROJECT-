package com.nagorikseba.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ward_id")
    @JsonIgnore
    private Ward ward;

    private Double officeLatitude;

    private Double officeLongitude;

    @Builder.Default
    @OneToMany(mappedBy = "assignedDepartment")
    @JsonIgnore
    private List<Complaint> complaints = new ArrayList<>();

    public int getActiveComplaintCount() {
        if (complaints == null) {
            return 0;
        }
        return (int) complaints.stream()
                .filter(c -> c.getStatus() != null && !c.getStatus().name().equals("CLOSED"))
                .count();
    }
}
