package com.morning.morningpage.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "morning_pages")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MorningPage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    
    @Column(nullable = false)
    private LocalDate recordDate;  // 기록 날짜
    
    @Column(nullable = false, length = 100)
    private String activity;  // 활동명
    
    @Column(columnDefinition = "TEXT")
    private String content;  // 상세 내용
    
    @Column
    private Integer duration;  // 소요 시간 (분)
    
    @Column(nullable = false)
    private Boolean isPublic = false;  // 공개 여부
    
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
