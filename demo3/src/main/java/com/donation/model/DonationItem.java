package com.donation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "donation_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonationItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;        // 物品名称
    
    @Column(nullable = false)
    private String category;    // 物品类别
    
    private String description; // 物品描述
    
    @Column(nullable = false)
    private Integer quantity;   // 数量
    
    @Column(nullable = false)
    private String status;      // 状态（可用/已捐出）
    
    @Column(nullable = false)
    private LocalDateTime createTime; // 创建时间
} 