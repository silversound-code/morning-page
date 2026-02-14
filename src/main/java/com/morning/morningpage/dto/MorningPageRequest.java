package com.morning.morningpage.dto;

import lombok.*;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class MorningPageRequest {
    private LocalDate recordDate;
    private Long categoryId;
    private String activity;
    private String content;
    private Integer duration;
    private Boolean isPublic;
}
