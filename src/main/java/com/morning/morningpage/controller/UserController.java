package com.morning.morningpage.controller;

import com.morning.morningpage.dto.UserRequest;
import com.morning.morningpage.dto.UserResponse;
import com.morning.morningpage.entity.User;
import com.morning.morningpage.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody UserRequest request) {
        try {
            User user = userService.register(
                    request.getUsername(),
                    request.getEmail(),
                    request.getPassword()
            );
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(UserResponse.from(user));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // 로그인
    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody UserRequest request, HttpSession session) {
        try {
            User user = userService.login(request.getUsername(), request.getPassword());
            
            // 세션에 사용자 ID 저장
            session.setAttribute("userId", user.getId());
            
            return ResponseEntity.ok(UserResponse.from(user));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    
    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok().build();
    }
    
    // 현재 로그인 사용자 정보
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(UserResponse.from(user));
    }
    
    // 특정 사용자 조회
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(UserResponse.from(user));
    }
}
