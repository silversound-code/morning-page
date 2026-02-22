package com.morning.morningpage.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class MorningPageRequest {
    @NotNull(message = "날짜는 필수입니다.")
    private LocalDate recordDate;

    @NotNull(message = "카테고리는 필수입니다.")
    private Long categoryId;

    @NotBlank(message = "활동명은 필수입니다.")
    private String activity;

    private String content;

    @Positive(message = "소요 시간은 양수여야 합니다.")
    private Integer duration;
    
    private Boolean isPublic;
}
