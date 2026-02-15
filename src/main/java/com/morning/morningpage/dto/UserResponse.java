package com.morning.morningpage.dto;

import com.morning.morningpage.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private Integer currentStreak;
    private Integer totalRecords;
    private LocalDateTime createdAt;
    
    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .currentStreak(user.getCurrentStreak())
                .totalRecords(user.getTotalRecords())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
