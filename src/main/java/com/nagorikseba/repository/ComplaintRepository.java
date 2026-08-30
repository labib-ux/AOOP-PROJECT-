package com.nagorikseba.repository;

import com.nagorikseba.entity.Complaint;
import com.nagorikseba.enums.ComplaintStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    List<Complaint> findByCitizenId(Long citizenId);
    List<Complaint> findByWardId(Long wardId);
    List<Complaint> findByAssignedOfficerId(Long officerId);
    List<Complaint> findByAssignedDepartmentId(Long departmentId);
    List<Complaint> findByStatus(ComplaintStatus status);
    List<Complaint> findByWardIdAndStatus(Long wardId, ComplaintStatus status);
    List<Complaint> findByStatusNotInAndDeadlineAtBefore(List<ComplaintStatus> statuses, LocalDateTime deadline);

    @Query("SELECT c FROM Complaint c WHERE c.status NOT IN :excludedStatuses AND c.deadlineAt < :now")
    List<Complaint> findBreachedSLA(@Param("excludedStatuses") List<ComplaintStatus> excludedStatuses,
                                    @Param("now") LocalDateTime now);
}
