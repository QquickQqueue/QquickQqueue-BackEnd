package com.example.qquickqqueue.domain.schedule.entity;

import com.example.qquickqqueue.domain.actor.entity.Actor;
import com.example.qquickqqueue.domain.musical.entity.Musical;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {
    @Id
    @Column(name = "SCHEDULE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private boolean isDeleted;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MUSICAL_ID")
    private Musical musical;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACTOR_ID")
    private Actor actor;
}
