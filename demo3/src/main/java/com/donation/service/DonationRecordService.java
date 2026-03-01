package com.donation.service;

import com.donation.model.DonationItem;
import com.donation.model.DonationRecord;
import com.donation.repository.DonationItemRepository;
import com.donation.repository.DonationRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class DonationRecordService {
    
    @Autowired
    private DonationRecordRepository donationRecordRepository;
    
    @Autowired
    private DonationItemRepository donationItemRepository;

    public Page<DonationRecord> findRecords(String donorName, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        if (donorName != null && !donorName.isEmpty()) {
            if (startDate != null && endDate != null) {
                return donationRecordRepository.findByDonorNameContainingAndDonationTimeBetween(
                    donorName, startDate, endDate, pageable);
            }
            return donationRecordRepository.findByDonorNameContaining(donorName, pageable);
        } else if (startDate != null && endDate != null) {
            return donationRecordRepository.findByDonationTimeBetween(startDate, endDate, pageable);
        }
        return donationRecordRepository.findAll(pageable);
    }

    @Transactional(rollbackFor = Exception.class)
    public DonationRecord createRecord(DonationRecord record) {
        try {
            // 获取物品信息
            DonationItem item = donationItemRepository.findById(record.getDonationItem().getId())
                .orElseThrow(() -> new RuntimeException("物品不存在"));
                
            // 检查数量是否足够
            if (item.getQuantity() < record.getQuantity()) {
                throw new RuntimeException("物品数量不足");
            }
            
            // 更新物品数量
            item.setQuantity(item.getQuantity() - record.getQuantity());
            if (item.getQuantity() == 0) {
                item.setStatus("已捐出");
            }
            donationItemRepository.save(item);
            
            // 设置关联的物品和其他信息
            record.setDonationItem(item);
            record.setDonationTime(LocalDateTime.now()); // 设置当前时间
            record.setStatus("已登记");
            
            // 保存并返回记录
            DonationRecord savedRecord = donationRecordRepository.save(record);
            
            // 确保时间被正确设置
            if (savedRecord.getDonationTime() == null) {
                savedRecord.setDonationTime(LocalDateTime.now());
                savedRecord = donationRecordRepository.save(savedRecord);
            }
            
            return savedRecord;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
} 