package com.example.qquickqqueue.domain.scheduleSeat.repository;

import com.example.qquickqqueue.domain.scheduleSeat.entity.ScheduleSeat;
import java.util.List;
import org.springframework.data.repository.query.Param;

public interface ScheduleSeatCustomRepository {
	List<ScheduleSeat> findAllBySchedule_IdAndIsReserved(@Param("scheduleId") Long scheduleId, @Param("isReserved") boolean isReserved);
	List<ScheduleSeat> findAllBySchedule_Id(@Param("scheduleId") Long scheduleId);
}
