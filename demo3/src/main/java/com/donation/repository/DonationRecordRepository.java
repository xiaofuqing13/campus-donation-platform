package com.donation.repository;

import com.donation.model.DonationRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DonationRecordRepository extends JpaRepository<DonationRecord, Long> {
    Page<DonationRecord> findByDonorNameContaining(String donorName, Pageable pageable);
    Page<DonationRecord> findByDonationTimeBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    Page<DonationRecord> findByDonorNameContainingAndDonationTimeBetween(
        String donorName, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    List<DonationRecord> findByDonationTimeBetween(LocalDateTime startDate, LocalDateTime endDate);
} 