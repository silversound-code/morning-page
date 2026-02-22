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

    // FETCH JOIN으로 N+1 해결
    @Query("SELECT m FROM MorningPage m " +
    "JOIN FETCH m.user " +
    "JOIN FETCH m.category " +
    "WHERE m.user = :user " +
    "ORDER BY m.recordDate DESC")
    List<MorningPage> findByUserOrderByRecordDateDesc(@Param("user") User user);

    @Query("SELECT m FROM MorningPage m " +
    "JOIN FETCH m.user " +
    "JOIN FETCH m.category " +
    "WHERE m.user = :user AND m.recordDate = :date")
    Optional<MorningPage> findByUserAndRecordDate(@Param("user") User user,
    @Param("date") LocalDate date);

    @Query("SELECT m FROM MorningPage m " +
    "JOIN FETCH m.user " +
    "JOIN FETCH m.category " +
    "WHERE m.user = :user " +
    "AND m.recordDate BETWEEN :startDate AND :endDate")
    List<MorningPage> findByUserAndRecordDateBetween(@Param("user") User user,
    @Param("startDate") LocalDate startDate,
    @Param("endDate") LocalDate endDate);

    @Query("SELECT m FROM MorningPage m " +
    "JOIN FETCH m.user " +
    "JOIN FETCH m.category " +
    "WHERE m.isPublic = true " +
    "ORDER BY m.createdAt DESC " +
    "LIMIT 10")
    List<MorningPage> findTop10ByIsPublicTrueOrderByCreatedAtDesc();

    @Query("SELECT COUNT(m) FROM MorningPage m " +
    "WHERE m.user = :user " +
    "AND YEAR(m.recordDate) = :year " +
    "AND MONTH(m.recordDate) = :month")
    Long countByUserAndYearMonth(@Param("user") User user,
    @Param("year") int year,
    @Param("month") int month);
}
