package com.example.qquickqqueue.domain.scheduleSeat.repository.impl;

import com.example.qquickqqueue.domain.scheduleSeat.entity.QScheduleSeat;
import com.example.qquickqqueue.domain.scheduleSeat.entity.ScheduleSeat;
import com.example.qquickqqueue.domain.scheduleSeat.repository.ScheduleSeatCustomRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ScheduleSeatCustomRepositoryImpl implements ScheduleSeatCustomRepository {
	private final JPAQueryFactory query;
	private final QScheduleSeat qScheduleSeat = QScheduleSeat.scheduleSeat;

	@Override
	public List<ScheduleSeat> findAllBySchedule_IdAndIsReserved(Long scheduleId, boolean isReserved) {
		return query.selectFrom(qScheduleSeat)
			.join(qScheduleSeat.schedule).fetchJoin()
			.join(qScheduleSeat.seat).fetchJoin()
			.join(qScheduleSeat.seatGrade).fetchJoin()
			.where(qScheduleSeat.schedule.id.eq(scheduleId)
				.and(qScheduleSeat.isReserved.eq(isReserved)))
			.fetch();
	}

	@Override
	public List<ScheduleSeat> findAllBySchedule_Id(Long scheduleId) {
		return query.selectFrom(qScheduleSeat)
			.join(qScheduleSeat.seatGrade).fetchJoin()
			.join(qScheduleSeat.schedule).fetchJoin()
			.join(qScheduleSeat.seat).fetchJoin()
			.where(qScheduleSeat.schedule.id.eq(scheduleId))
			.fetch();
	}

	@Override
	public Optional<ScheduleSeat> findById(Long id) {
		return Optional.ofNullable(query.selectFrom(qScheduleSeat)
				.where(qScheduleSeat.id.eq(id))
				.fetchOne());
	}
}
