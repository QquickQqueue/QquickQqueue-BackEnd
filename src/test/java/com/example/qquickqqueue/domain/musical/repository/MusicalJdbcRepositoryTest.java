package com.example.qquickqqueue.domain.musical.repository;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.example.qquickqqueue.domain.schedule.entity.Schedule;
import com.example.qquickqqueue.domain.scheduleSeat.entity.ScheduleSeat;
import com.example.qquickqqueue.domain.seat.entity.Seat;
import com.example.qquickqqueue.domain.seatGrade.entity.SeatGrade;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

@ExtendWith({MockitoExtension.class})
class MusicalJdbcRepositoryTest {
	@Mock
	private JdbcTemplate jdbcTemplate;
	@InjectMocks
	private MusicalJdbcRepository musicalJdbcRepository;

	@Test
	@DisplayName("jdbcRepository insert schedule Seat List")
	public void insertScheduleSeatListTest() throws SQLException {
		List<ScheduleSeat> scheduleSeatList = List.of(ScheduleSeat.builder().isReserved(false).schedule(Schedule.builder()
			.id(1L).build()).seat(Seat.builder().id(1L).build()).seatGrade(SeatGrade.builder().id(1L).build()).build());

		musicalJdbcRepository.insertScheduleSeatList(scheduleSeatList);

		ArgumentCaptor<BatchPreparedStatementSetter> argumentCaptor = ArgumentCaptor.forClass(BatchPreparedStatementSetter.class);
		verify(jdbcTemplate).batchUpdate(anyString(), argumentCaptor.capture());

		BatchPreparedStatementSetter capturedSetter = argumentCaptor.getValue();
		PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

		capturedSetter.setValues(mockPreparedStatement, 0);

		verify(mockPreparedStatement).setBoolean(1, false);
		verify(mockPreparedStatement).setLong(2, 1L);
		verify(mockPreparedStatement).setLong(3, 1L);
		verify(mockPreparedStatement).setLong(4, 1L);
		assert capturedSetter.getBatchSize() == scheduleSeatList.size();
	}
}