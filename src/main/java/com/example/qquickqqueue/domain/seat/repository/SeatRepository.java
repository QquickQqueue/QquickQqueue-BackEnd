package com.example.qquickqqueue.domain.seat.repository;

import com.example.qquickqqueue.domain.seat.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
}
