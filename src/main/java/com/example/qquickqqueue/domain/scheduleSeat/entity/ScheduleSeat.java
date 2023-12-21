package com.example.qquickqqueue.domain.scheduleSeat.entity;

import com.example.qquickqqueue.domain.schedule.entity.Schedule;
import com.example.qquickqqueue.domain.seat.entity.Seat;
import com.example.qquickqqueue.domain.seatGrade.entity.SeatGrade;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleSeat {
    @Id
    @Column(name = "SCHEDULE_SEAT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "boolean default false", nullable = false)
    private boolean isReserved;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCHEDULE_ID")
    private Schedule schedule;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SEAT_ID")
    private Seat seat;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SEAT_GRADE_ID")
    private SeatGrade seatGrade;
}
