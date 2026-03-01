package com.donation.service;

import com.donation.model.DonationItem;
import com.donation.model.DonationRecord;
import com.donation.repository.DonationItemRepository;
import com.donation.repository.DonationRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    @Autowired
    private DonationItemRepository donationItemRepository;

    @Autowired
    private DonationRecordRepository donationRecordRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM-dd");

    public List<Map<String, Object>> getCategoryStatistics() {
        // 获取所有物品
        List<DonationItem> items = donationItemRepository.findAll();
        
        // 按类别统计
        Map<String, Long> categoryStats = items.stream()
            .collect(Collectors.groupingBy(
                DonationItem::getCategory,
                Collectors.counting()
            ));
        
        // 转换为前端需要的格式
        return categoryStats.entrySet().stream()
            .map(entry -> {
                Map<String, Object> stat = new HashMap<>();
                stat.put("name", entry.getKey());
                stat.put("value", entry.getValue());
                return stat;
            })
            .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getDonationTrend() {
        // 获取最近7天的数据
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(6);
        
        List<DonationRecord> records = donationRecordRepository.findByDonationTimeBetween(
            startDate, endDate);
        
        // 按日期分组统计
        Map<LocalDate, Integer> dailyStats = records.stream()
            .collect(Collectors.groupingBy(
                record -> record.getDonationTime().toLocalDate(),
                Collectors.summingInt(DonationRecord::getQuantity)
            ));
        
        // 生成最近7天的数据
        List<Map<String, Object>> trend = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            LocalDate date = endDate.minusDays(i).toLocalDate();
            Map<String, Object> point = new HashMap<>();
            point.put("date", date.format(DATE_FORMATTER));
            point.put("value", dailyStats.getOrDefault(date, 0));
            trend.add(point);
        }
        
        return trend;
    }
} 