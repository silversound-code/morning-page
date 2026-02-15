package com.morning.morningpage.service;

import com.morning.morningpage.dto.MorningPageRequest;
import com.morning.morningpage.dto.MorningPageResponse;
import com.morning.morningpage.entity.Category;
import com.morning.morningpage.entity.MorningPage;
import com.morning.morningpage.entity.User;
import com.morning.morningpage.repository.MorningPageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MorningPageService {
    
    private final MorningPageRepository morningPageRepository;
    private final CategoryService categoryService;
    private final UserService userService;
    
    // 기록 작성
    @Transactional
    public MorningPageResponse createRecord(Long userId, MorningPageRequest request) {
        User user = userService.getUserById(userId);
        Category category = categoryService.getCategoryById(request.getCategoryId());
        
        // 같은 날짜에 이미 기록이 있는지 확인
        morningPageRepository.findByUserAndRecordDate(user, request.getRecordDate())
                .ifPresent(existing -> {
                    throw new RuntimeException("이미 오늘 기록을 작성했습니다.");
                });
        
        MorningPage morningPage = MorningPage.builder()
                .user(user)
                .category(category)
                .recordDate(request.getRecordDate())
                .activity(request.getActivity())
                .content(request.getContent())
                .duration(request.getDuration())
                .isPublic(request.getIsPublic() != null ? request.getIsPublic() : false)
                .build();
        
        MorningPage saved = morningPageRepository.save(morningPage);
        
        // 사용자 통계 업데이트
        userService.updateUserStats(userId);
        
        return MorningPageResponse.from(saved);
    }
    
    // 내 모든 기록 조회
    public List<MorningPageResponse> getMyRecords(Long userId) {
        User user = userService.getUserById(userId);
        return morningPageRepository.findByUserOrderByRecordDateDesc(user)
                .stream()
                .map(MorningPageResponse::from)
                .collect(Collectors.toList());
    }
    
    // 특정 기록 상세 조회
    public MorningPageResponse getRecord(Long recordId) {
        MorningPage record = morningPageRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("기록을 찾을 수 없습니다."));
        return MorningPageResponse.from(record);
    }
    
    // 특정 날짜의 기록 조회
    public MorningPageResponse getRecordByDate(Long userId, LocalDate date) {
        User user = userService.getUserById(userId);
        MorningPage record = morningPageRepository.findByUserAndRecordDate(user, date)
                .orElseThrow(() -> new RuntimeException("해당 날짜의 기록이 없습니다."));
        return MorningPageResponse.from(record);
    }
    
    // 특정 월의 기록 조회
    public List<MorningPageResponse> getRecordsByMonth(Long userId, int year, int month) {
        User user = userService.getUserById(userId);
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        
        return morningPageRepository.findByUserAndRecordDateBetween(user, startDate, endDate)
                .stream()
                .map(MorningPageResponse::from)
                .collect(Collectors.toList());
    }
    
    // 기록 수정
    @Transactional
    public MorningPageResponse updateRecord(Long userId, Long recordId, MorningPageRequest request) {
        MorningPage record = morningPageRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("기록을 찾을 수 없습니다."));
        
        // 본인 기록인지 확인
        if (!record.getUser().getId().equals(userId)) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }
        
        // 카테고리 변경
        if (request.getCategoryId() != null) {
            Category category = categoryService.getCategoryById(request.getCategoryId());
            record.setCategory(category);
        }
        
        // 다른 필드 업데이트
        if (request.getActivity() != null) {
            record.setActivity(request.getActivity());
        }
        if (request.getContent() != null) {
            record.setContent(request.getContent());
        }
        if (request.getDuration() != null) {
            record.setDuration(request.getDuration());
        }
        if (request.getIsPublic() != null) {
            record.setIsPublic(request.getIsPublic());
        }
        
        return MorningPageResponse.from(record);
    }
    
    // 기록 삭제
    @Transactional
    public void deleteRecord(Long userId, Long recordId) {
        MorningPage record = morningPageRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("기록을 찾을 수 없습니다."));
        
        // 본인 기록인지 확인
        if (!record.getUser().getId().equals(userId)) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }
        
        morningPageRepository.delete(record);
        
        // 사용자 통계 업데이트
        userService.updateUserStats(userId);
    }
    
    // 공개된 최신 기록들 조회
    public List<MorningPageResponse> getPublicRecords() {
        return morningPageRepository.findTop10ByIsPublicTrueOrderByCreatedAtDesc()
                .stream()
                .map(MorningPageResponse::from)
                .collect(Collectors.toList());
    }
    
    // 특정 월의 기록 개수
    public Long getRecordCountByMonth(Long userId, int year, int month) {
        User user = userService.getUserById(userId);
        return morningPageRepository.countByUserAndYearMonth(user, year, month);
    }
}