package com.donation.controller;

import com.donation.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/statistics")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("categoryStats", statisticsService.getCategoryStatistics());
        statistics.put("trendStats", statisticsService.getDonationTrend());
        return ResponseEntity.ok(statistics);
    }
} 