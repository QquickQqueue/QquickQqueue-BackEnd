package com.example.qquickqqueue.domain.musical.repository;

import com.example.qquickqqueue.domain.scheduleSeat.entity.ScheduleSeat;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Generated
@Repository
@RequiredArgsConstructor
public class MusicalJdbcRepository {
    private final JdbcTemplate jdbcTemplate;

    public void insertScheduleSeatList(List<ScheduleSeat> scheduleSeatList) {
        jdbcTemplate.batchUpdate(
                "insert into schedule_seat (is_reserved, schedule_id, seat_id, seat_grade_id) values (?, ?, ?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ScheduleSeat scheduleSeat = scheduleSeatList.get(i);
                        ps.setBoolean(1, scheduleSeat.isReserved());
                        ps.setLong(2, scheduleSeat.getSchedule().getId());
                        ps.setLong(3, scheduleSeat.getSeat().getId());
                        ps.setLong(4, scheduleSeat.getSeatGrade().getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return scheduleSeatList.size();
                    }
                }
        );
    }
}
