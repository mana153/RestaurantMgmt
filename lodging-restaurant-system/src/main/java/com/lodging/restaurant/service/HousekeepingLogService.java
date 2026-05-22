package com.lodging.restaurant.service;

import com.lodging.restaurant.model.HousekeepingLog;
import com.lodging.restaurant.repository.HousekeepingLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HousekeepingLogService {

    @Autowired
    private HousekeepingLogRepository housekeepingLogRepository;

    public List<HousekeepingLog> getAllHousekeepingLogs() {
        return housekeepingLogRepository.findAll();
    }

    public HousekeepingLog getHousekeepingLogById(Long id) {
        return housekeepingLogRepository.findById(id).orElse(null);
    }

    public HousekeepingLog saveHousekeepingLog(HousekeepingLog housekeepingLog) {
        return housekeepingLogRepository.save(housekeepingLog);
    }

    public void deleteHousekeepingLog(Long id) {
        housekeepingLogRepository.deleteById(id);
    }
}
