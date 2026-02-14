package com.morning.morningpage.dto;

import com.morning.morningpage.entity.MorningPage;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MorningPageResponse {
    private Long id;
    private String username;
    private String categoryName;
    private String categoryIcon;
    private LocalDate recordDate;
    private String activity;
    private String content;
    private Integer duration;
    private Boolean isPublic;
    private LocalDateTime createdAt;
    
    public static MorningPageResponse from(MorningPage page) {
        return MorningPageResponse.builder()
                .id(page.getId())
                .username(page.getUser().getUsername())
                .categoryName(page.getCategory().getName())
                .categoryIcon(page.getCategory().getIcon())
                .recordDate(page.getRecordDate())
                .activity(page.getActivity())
                .content(page.getContent())
                .duration(page.getDuration())
                .isPublic(page.getIsPublic())
                .createdAt(page.getCreatedAt())
                .build();
    }
}