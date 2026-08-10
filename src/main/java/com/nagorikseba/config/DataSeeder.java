package com.nagorikseba.config;

import com.nagorikseba.entity.*;
import com.nagorikseba.enums.*;
import com.nagorikseba.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository,
                                   WardRepository wardRepository,
                                   DepartmentRepository departmentRepository,
                                   SLARuleRepository slaRuleRepository,
                                   PasswordEncoder passwordEncoder) {
        return args -> {
            // Create Wards
            Ward ward1 = Ward.builder()
                    .wardNumber(1)
                    .areaName("Mohammadpur")
                    .cityCorporation("Dhaka North")
                    .build();
            Ward ward2 = Ward.builder()
                    .wardNumber(2)
                    .areaName("Gulshan")
                    .cityCorporation("Dhaka North")
                    .build();
            Ward ward3 = Ward.builder()
                    .wardNumber(15)
                    .areaName("Mirpur")
                    .cityCorporation("Dhaka North")
                    .build();
            wardRepository.saveAll(List.of(ward1, ward2, ward3));

            // Create Admin User
            User admin = User.builder()
                    .fullName("System Administrator")
                    .email("admin@nagorikseba.com")
                    .phone("01700000000")
                    .password(passwordEncoder.encode("admin123"))
                    .role(UserRole.ADMIN)
                    .isActive(true)
                    .build();
            userRepository.save(admin);

            // Create Ward Councilors
            User councilor1 = User.builder()
                    .fullName("Abdul Rahman")
                    .email("councilor1@nagorikseba.com")
                    .phone("01711111111")
                    .password(passwordEncoder.encode("councilor123"))
                    .role(UserRole.WARD_COUNCILOR)
                    .ward(ward1)
                    .isActive(true)
                    .build();
            
            User councilor2 = User.builder()
                    .fullName("Fatema Begum")
                    .email("councilor2@nagorikseba.com")
                    .phone("01722222222")
                    .password(passwordEncoder.encode("councilor123"))
                    .role(UserRole.WARD_COUNCILOR)
                    .ward(ward2)
                    .isActive(true)
                    .build();
            
            ward1.setCouncilor(councilor1);
            ward2.setCouncilor(councilor2);
            wardRepository.saveAll(List.of(ward1, ward2));
            userRepository.saveAll(List.of(councilor1, councilor2));

            // Create Department Officers
            Department roadsDept = Department.builder()
                    .name("ROADS")
                    .ward(ward1)
                    .build();
            Department waterDept = Department.builder()
                    .name("WATER_SUPPLY")
                    .ward(ward1)
                    .build();
            Department electricityDept = Department.builder()
                    .name("ELECTRICITY")
                    .ward(ward2)
                    .build();
            departmentRepository.saveAll(List.of(roadsDept, waterDept, electricityDept));

            User officer1 = User.builder()
                    .fullName("Karim Ahmed")
                    .email("officer1@nagorikseba.com")
                    .phone("01733333333")
                    .password(passwordEncoder.encode("officer123"))
                    .role(UserRole.DEPT_OFFICER)
                    .ward(ward1)
                    .department(roadsDept)
                    .isActive(true)
                    .build();

            User officer2 = User.builder()
                    .fullName("Salma Khatun")
                    .email("officer2@nagorikseba.com")
                    .phone("01744444444")
                    .password(passwordEncoder.encode("officer123"))
                    .role(UserRole.DEPT_OFFICER)
                    .ward(ward1)
                    .department(waterDept)
                    .isActive(true)
                    .build();
            userRepository.saveAll(List.of(officer1, officer2));

            // Create Demo Citizen
            User citizen = User.builder()
                    .fullName("Rahim Uddin")
                    .email("citizen@nagorikseba.com")
                    .phone("01755555555")
                    .password(passwordEncoder.encode("citizen123"))
                    .role(UserRole.CITIZEN)
                    .ward(ward1)
                    .isActive(true)
                    .build();
            userRepository.save(citizen);

            // Create SLA Rules
            SLARule sla1 = SLARule.builder()
                    .category(ComplaintCategory.ROADS)
                    .priority(Priority.NORMAL)
                    .maxHours(72)
                    .escalationLevel(1)
                    .build();
            SLARule sla2 = SLARule.builder()
                    .category(ComplaintCategory.ROADS)
                    .priority(Priority.HIGH)
                    .maxHours(24)
                    .escalationLevel(2)
                    .build();
            SLARule sla3 = SLARule.builder()
                    .category(ComplaintCategory.WATER_SUPPLY)
                    .priority(Priority.NORMAL)
                    .maxHours(48)
                    .escalationLevel(1)
                    .build();
            SLARule sla4 = SLARule.builder()
                    .category(ComplaintCategory.ELECTRICITY)
                    .priority(Priority.CRITICAL)
                    .maxHours(6)
                    .escalationLevel(3)
                    .build();
            slaRuleRepository.saveAll(List.of(sla1, sla2, sla3, sla4));

            System.out.println("=== Database Seeded Successfully ===");
            System.out.println("Admin: admin@nagorikseba.com / admin123");
            System.out.println("Citizen: citizen@nagorikseba.com / citizen123");
            System.out.println("Councilor: councilor1@nagorikseba.com / councilor123");
            System.out.println("Officer: officer1@nagorikseba.com / officer123");
        };
    }
}
