package com.example.qquickqqueue.domain.seatGrade.repository;

import com.example.qquickqqueue.domain.seatGrade.entity.SeatGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatGradeRepository extends JpaRepository<SeatGrade, Long> {
}
