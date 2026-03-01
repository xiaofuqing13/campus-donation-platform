package com.donation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "donation_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonationRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id", nullable = false)
    private DonationItem donationItem;
    
    @Column(nullable = false)
    private String donorName;    // 捐赠人姓名
    
    @Column(nullable = false)
    private String donorContact; // 捐赠人联系方式
    
    @Column(nullable = false)
    private Integer quantity;    // 捐赠数量
    
    @Column(nullable = false)
    private LocalDateTime donationTime; // 捐赠时间
    
    @Column(nullable = false)
    private String status;      // 记录状态
} 