package com.nagorikseba.service;

import com.nagorikseba.entity.Complaint;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SLAService {

    public LocalDateTime calculateDeadline(Complaint complaint) {
        throw new UnsupportedOperationException("SLA deadline calculation is not implemented yet");
    }

    @Scheduled(fixedRate = 3600000)
    public void checkSLABreaches() {
        // TODO: escalate complaints past deadline
    }
}
