package com.donation.controller;

import com.donation.model.DonationRecord;
import com.donation.service.DonationRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/records")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
@ResponseBody
public class DonationRecordController {

    @Autowired
    private DonationRecordService donationRecordService;

    @GetMapping
    public ResponseEntity<Page<DonationRecord>> getRecords(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String donorName,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime endDate) {
        
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return ResponseEntity.ok(donationRecordService.findRecords(donorName, startDate, endDate, pageRequest));
    }

    @PostMapping
    public ResponseEntity<?> createRecord(@RequestBody DonationRecord record) {
        try {
            System.out.println("Received donation record: " + record);
            DonationRecord savedRecord = donationRecordService.createRecord(record);
            return ResponseEntity.ok(savedRecord);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
} 