package com.morning.morningpage.repository;

import com.morning.morningpage.entity.MorningPage;
import com.morning.morningpage.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MorningPageRepository extends JpaRepository<MorningPage, Long> {
    
    // 특정 사용자의 모든 기록
    List<MorningPage> findByUserOrderByRecordDateDesc(User user);
    
    // 특정 날짜의 기록
    Optional<MorningPage> findByUserAndRecordDate(User user, LocalDate date);
    
    // 특정 기간의 기록
    List<MorningPage> findByUserAndRecordDateBetween(User user, LocalDate startDate, LocalDate endDate);
    
    // 카테고리별 기록
    List<MorningPage> findByUserAndCategory_Id(User user, Long categoryId);
    
    // 공개된 최신 기록들
    List<MorningPage> findTop10ByIsPublicTrueOrderByCreatedAtDesc();
    
    // 특정 월의 기록 개수
    @Query("SELECT COUNT(m) FROM MorningPage m WHERE m.user = :user " +
    "AND YEAR(m.recordDate) = :year AND MONTH(m.recordDate) = :month")
    Long countByUserAndYearMonth(@Param("user") User user, @Param("year") int year, @Param("month") int month);
}