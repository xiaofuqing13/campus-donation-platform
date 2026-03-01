package com.donation.service;

import com.donation.model.DonationItem;
import com.donation.repository.DonationItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DonationItemService {
    
    @Autowired
    private DonationItemRepository donationItemRepository;

    public Page<DonationItem> findItems(String name, String category, Pageable pageable) {
        if (name != null && !name.isEmpty() && category != null && !category.isEmpty()) {
            return donationItemRepository.findByNameContainingAndCategory(name, category, pageable);
        } else if (name != null && !name.isEmpty()) {
            return donationItemRepository.findByNameContaining(name, pageable);
        } else if (category != null && !category.isEmpty()) {
            return donationItemRepository.findByCategory(category, pageable);
        }
        return donationItemRepository.findAll(pageable);
    }

    public List<DonationItem> getAllItems() {
        return donationItemRepository.findAll();
    }

    public DonationItem createItem(DonationItem item) {
        item.setCreateTime(LocalDateTime.now());
        item.setStatus("可用");
        return donationItemRepository.save(item);
    }

    public DonationItem updateItem(Long id, DonationItem item) {
        DonationItem existingItem = donationItemRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("物品不存在"));
        
        existingItem.setName(item.getName());
        existingItem.setCategory(item.getCategory());
        existingItem.setDescription(item.getDescription());
        existingItem.setQuantity(item.getQuantity());
        existingItem.setStatus(item.getStatus());
        
        return donationItemRepository.save(existingItem);
    }

    public void deleteItem(Long id) {
        donationItemRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("物品不存在"));
        donationItemRepository.deleteById(id);
    }
} 