package com.example.qquickqqueue.domain.scheduleSeat.repository;

import com.example.qquickqqueue.domain.scheduleSeat.entity.ScheduleSeat;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScheduleSeatRepository extends JpaRepository<ScheduleSeat, Long>, ScheduleSeatCustomRepository {
    ScheduleSeat findByScheduleIdAndSeatId(Long scheduleId, Long seatId);

    @Override
//    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    Optional<ScheduleSeat> findById(Long id);
}
