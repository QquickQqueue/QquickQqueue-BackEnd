package com.example.qquickqqueue.domain.scheduleSeat.repository;

import com.example.qquickqqueue.domain.scheduleSeat.entity.ScheduleSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleSeatRepository extends JpaRepository<ScheduleSeat, Long> {
    List<ScheduleSeat> findAllBySchedule_IdAndIsReserved(Long scheduleId, boolean isReserved);
    List<ScheduleSeat> findAllBySchedule_Id(Long scheduleId);
    ScheduleSeat findByScheduleIdAndSeatId(Long scheduleId, Long seatId);
}
