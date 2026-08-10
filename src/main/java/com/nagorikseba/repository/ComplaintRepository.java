package com.nagorikseba.repository;

import com.nagorikseba.entity.Complaint;
import com.nagorikseba.enums.ComplaintStatus;
import com.nagorikseba.enums.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    org.springframework.data.domain.Page<Complaint> findByCitizenId(Long citizenId, org.springframework.data.domain.Pageable pageable);
    List<Complaint> findByWardId(Long wardId);
    List<Complaint> findByAssignedOfficerId(Long officerId);
    List<Complaint> findByAssignedDepartmentId(Long departmentId);
    List<Complaint> findByStatus(ComplaintStatus status);
    List<Complaint> findByWardIdAndStatus(Long wardId, ComplaintStatus status);
    
    @Query("SELECT c FROM Complaint c WHERE c.status NOT IN :excludedStatuses AND c.deadlineAt < :now")
    List<Complaint> findBreachedSLA(@Param("excludedStatuses") List<ComplaintStatus> excludedStatuses, 
                                     @Param("now") LocalDateTime now);
    
    @Query("SELECT c FROM Complaint c WHERE c.latitude BETWEEN :minLat AND :maxLat " +
           "AND c.longitude BETWEEN :minLng AND :maxLng")
    List<Complaint> findComplaintsInBoundingBox(@Param("minLat") Double minLat,
                                                 @Param("maxLat") Double maxLat,
                                                 @Param("minLng") Double minLng,
                                                 @Param("maxLng") Double maxLng);
    
    @Query("SELECT COUNT(c) FROM Complaint c WHERE c.ward.id = :wardId AND c.status = :status")
    long countByWardIdAndStatus(@Param("wardId") Long wardId, @Param("status") ComplaintStatus status);
    
    @Query("SELECT AVG(FUNCTION('EPOCH', c.resolvedAt) - FUNCTION('EPOCH', c.submittedAt)) / 3600.0 " +
           "FROM Complaint c WHERE c.ward.id = :wardId AND c.resolvedAt IS NOT NULL")
    Double getAverageResolutionHoursByWard(@Param("wardId") Long wardId);
    
    @Query("SELECT AVG(c.rating) FROM Complaint c WHERE c.ward.id = :wardId AND c.rating IS NOT NULL")
    Double getAverageRatingByWard(@Param("wardId") Long wardId);
}
