package com.nagorikseba.service;

import com.nagorikseba.dto.WardPerformanceDTO;
import com.nagorikseba.entity.Ward;
import com.nagorikseba.repository.WardRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class WardService {

    private final WardRepository wardRepository;

    public WardService(WardRepository wardRepository) {
        this.wardRepository = wardRepository;
    }

    public List<Ward> findAll() {
        return wardRepository.findAll();
    }

    public WardPerformanceDTO getPerformance(Long wardId) {
        throw new UnsupportedOperationException("Ward performance is not implemented yet");
    }

    public Map<String, Object> authorityDashboard(String username) {
        throw new UnsupportedOperationException("Authority dashboard is not implemented yet");
    }
}
