package com.example.qquickqqueue.domain.seat.repository;

import com.example.qquickqqueue.domain.seat.entity.Seat;
import com.example.qquickqqueue.domain.stadium.entity.Stadium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findAllByStadium(Stadium stadium);
}
