package com.donation.controller;

import com.donation.model.DonationItem;
import com.donation.service.DonationItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/items")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class DonationItemController {

    @Autowired
    private DonationItemService donationItemService;

    @GetMapping
    public ResponseEntity<Page<DonationItem>> getAllItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category) {
        
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<DonationItem> items = donationItemService.findItems(name, category, pageRequest);
        return ResponseEntity.ok(items);
    }

    @PostMapping
    public ResponseEntity<DonationItem> createItem(@RequestBody DonationItem item) {
        return ResponseEntity.ok(donationItemService.createItem(item));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DonationItem> updateItem(
            @PathVariable Long id,
            @RequestBody DonationItem item) {
        return ResponseEntity.ok(donationItemService.updateItem(id, item));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        donationItemService.deleteItem(id);
        return ResponseEntity.ok().build();
    }
} 