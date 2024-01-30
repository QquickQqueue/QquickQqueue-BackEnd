package com.example.qquickqqueue.domain.scheduleSeat.repository;

import com.example.qquickqqueue.domain.scheduleSeat.entity.ScheduleSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleSeatRepository extends JpaRepository<ScheduleSeat, Long>, ScheduleSeatCustomRepository {
    ScheduleSeat findByScheduleIdAndSeatId(Long scheduleId, Long seatId);
}
