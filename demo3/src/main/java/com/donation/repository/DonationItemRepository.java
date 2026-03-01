package com.donation.repository;

import com.donation.model.DonationItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DonationItemRepository extends JpaRepository<DonationItem, Long> {
    Page<DonationItem> findByNameContaining(String name, Pageable pageable);
    Page<DonationItem> findByCategory(String category, Pageable pageable);
    Page<DonationItem> findByNameContainingAndCategory(String name, String category, Pageable pageable);
} 