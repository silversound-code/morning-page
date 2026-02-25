package com.morning.morningpage.controller;

import com.morning.morningpage.dto.LoginRequest;
import com.morning.morningpage.dto.UserRequest;
import com.morning.morningpage.dto.UserResponse;
import com.morning.morningpage.entity.User;
import com.morning.morningpage.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRequest request, 
        BindingResult bindingResult) {
        // Validation 에러 체크
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return ResponseEntity.badRequest().body(errorMessage);
        }
        
        try {
            User user = userService.register(
                    request.getUsername(),
                    request.getEmail(),
                    request.getPassword()
            );
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(UserResponse.from(user));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request,
                                   BindingResult bindingResult,
                                   HttpSession session) {
        // Validation 에러 체크
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return ResponseEntity.badRequest().body(errorMessage);
        }

        try {
            User user = userService.login(request.getUsername(), request.getPassword());
            
            // 세션에 사용자 ID 저장
            session.setAttribute("userId", user.getId());
            
            return ResponseEntity.ok(UserResponse.from(user));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
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
    public ResponseEntity<?> getCurrentUser(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("로그인이 필요합니다.");
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

    // 내 추천곡 저장
    @PatchMapping("/me/music")
    public ResponseEntity<Void> updateMusic(@RequestBody java.util.Map<String, String> body,
                                            HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        userService.updateYoutubeUrl(userId, body.get("youtubeUrl"));
        return ResponseEntity.ok().build();
    }

    // 모두의 추천곡 조회
    @GetMapping("/music/public")
    public ResponseEntity<java.util.List<UserResponse>> getPublicMusic() {
        java.util.List<UserResponse> list = userService.getUsersWithMusic()
                .stream()
                .map(UserResponse::from)
                .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(list);
    }
}