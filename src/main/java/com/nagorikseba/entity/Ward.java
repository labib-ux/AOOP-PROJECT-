package com.nagorikseba.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "wards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer wardNumber;

    @Column(nullable = false, length = 100)
    private String areaName;

    @Column(nullable = false, length = 50)
    private String cityCorporation;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "councilor_id")
    @JsonIgnore
    private User councilor;

    @Builder.Default
    @OneToMany(mappedBy = "ward", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Department> departments = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "ward")
    @JsonIgnore
    private List<Complaint> complaints = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "ward")
    @JsonIgnore
    private List<WardPerformance> performances = new ArrayList<>();
}
