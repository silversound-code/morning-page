package com.morning.morningpage.controller;

import com.morning.morningpage.dto.MorningPageRequest;
import com.morning.morningpage.dto.MorningPageResponse;
import com.morning.morningpage.service.MorningPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/records")
@RequiredArgsConstructor
public class MorningPageController {
    
    private final MorningPageService morningPageService;
    
    // 기록 작성
    @PostMapping
    public ResponseEntity<?> createRecord(
            @Valid @RequestBody MorningPageRequest request,
            BindingResult bindingResult,
            HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("로그인이 필요합니다.");
        }

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return ResponseEntity.badRequest().body(errorMessage);
        }

        try {
            MorningPageResponse response = morningPageService.createRecord(userId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // 내 모든 기록 조회
    @GetMapping("/my")
    public ResponseEntity<List<MorningPageResponse>> getMyRecords(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        List<MorningPageResponse> records = morningPageService.getMyRecords(userId);
        return ResponseEntity.ok(records);
    }
    
    // 특정 기록 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<MorningPageResponse> getRecord(@PathVariable Long id) {
        try {
            MorningPageResponse response = morningPageService.getRecord(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // 특정 날짜의 기록 조회
    @GetMapping("/date/{date}")
    public ResponseEntity<MorningPageResponse> getRecordByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            HttpSession session) {
        
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        try {
            MorningPageResponse response = morningPageService.getRecordByDate(userId, date);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // 특정 월의 기록 조회
    @GetMapping("/month")
    public ResponseEntity<List<MorningPageResponse>> getRecordsByMonth(
            @RequestParam int year,
            @RequestParam int month,
            HttpSession session) {
        
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        List<MorningPageResponse> records = morningPageService.getRecordsByMonth(userId, year, month);
        return ResponseEntity.ok(records);
    }
    
    // 특정 월의 기록 개수
    @GetMapping("/month/count")
    public ResponseEntity<Long> getRecordCountByMonth(
            @RequestParam int year,
            @RequestParam int month,
            HttpSession session) {
        
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        Long count = morningPageService.getRecordCountByMonth(userId, year, month);
        return ResponseEntity.ok(count);
    }
    
    // 기록 수정
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRecord(
            @PathVariable Long id,
            @Valid @RequestBody MorningPageRequest request,
            BindingResult bindingResult,
            HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("로그인이 필요합니다.");
        }

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return ResponseEntity.badRequest().body(errorMessage);
        }

        try {
            MorningPageResponse response = morningPageService.updateRecord(userId, id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
    
    // 기록 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        try {
            morningPageService.deleteRecord(userId, id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    // 공개된 최신 기록들 조회
    @GetMapping("/public")
    public ResponseEntity<List<MorningPageResponse>> getPublicRecords() {
        List<MorningPageResponse> records = morningPageService.getPublicRecords();
        return ResponseEntity.ok(records);
    }
}