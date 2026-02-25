package com.morning.morningpage.service;

import com.morning.morningpage.entity.MorningPage;
import com.morning.morningpage.entity.User;
import com.morning.morningpage.repository.MorningPageRepository;
import com.morning.morningpage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;  // ⭐ 추가!
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    
    private final UserRepository userRepository;
    private final MorningPageRepository morningPageRepository;
    private final PasswordEncoder passwordEncoder;  // ⭐ 추가!
    
    /**
 * 회원가입
 * 
 * @param username 사용자명
 * @param email 이메일
 * @param password 평문 비밀번호
 * @return 생성된 사용자 엔티티
 * @throws RuntimeException 중복된 사용자명/이메일
 */
    @Transactional
    public User register(String username, String email, String password) {
        // 중복 체크
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("이미 존재하는 사용자명입니다.");
        }
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }
        
        User user = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))  // 비밀번호 암호화
                // 평문 "1234" → "$2a$10$xM8V..."
                .currentStreak(0)
                .totalRecords(0)
                .build();
        
        return userRepository.save(user);
    }
    
    /**
 * 로그인
 * 
 * @param username 사용자명
 * @param password 평문 비밀번호
 * @return 사용자 엔티티
 * @throws RuntimeException 사용자 없음 또는 비밀번호 불일치
 */
    public User login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        
        // 비밀번호 검증
    // passwordEncoder.matches(평문, 암호화된값)
    // 내부적으로 Salt를 추출하여 비교
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
        
        return user;
    }
    
    // 사용자 조회
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }
    
    // 추천곡 URL 저장
    @Transactional
    public void updateYoutubeUrl(Long userId, String youtubeUrl) {
        User user = getUserById(userId);
        user.setYoutubeUrl(youtubeUrl);
    }

    // 추천곡 등록한 모든 유저 조회
    public List<User> getUsersWithMusic() {
        return userRepository.findByYoutubeUrlIsNotNull();
    }

    // 사용자 통계 업데이트
    @Transactional
    public void updateUserStats(Long userId) {
        User user = getUserById(userId);
        
        // 총 기록 수 업데이트
        List<MorningPage> allRecords = morningPageRepository.findByUserOrderByRecordDateDesc(user);
        user.setTotalRecords(allRecords.size());
        
        // 연속 기록일 계산
        int streak = calculateStreak(allRecords);
        user.setCurrentStreak(streak);
    }
    
    // 연속 기록일 계산
    private int calculateStreak(List<MorningPage> records) {
        if (records.isEmpty()) {
            return 0;
        }
        
        // 날짜순 정렬 (최신순)
        records.sort((a, b) -> b.getRecordDate().compareTo(a.getRecordDate()));
        
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        
        // 오늘이나 어제 기록이 없으면 연속 끊김
        LocalDate latestDate = records.get(0).getRecordDate();
        if (!latestDate.equals(today) && !latestDate.equals(yesterday)) {
            return 0;
        }
        
        // 연속 일수 계산
        int streak = 1;
        LocalDate expectedDate = latestDate.minusDays(1);
        
        for (int i = 1; i < records.size(); i++) {
            LocalDate currentDate = records.get(i).getRecordDate();
            
            if (currentDate.equals(expectedDate)) {
                streak++;
                expectedDate = expectedDate.minusDays(1);
            } else {
                break;
            }
        }
        
        return streak;
    }
}