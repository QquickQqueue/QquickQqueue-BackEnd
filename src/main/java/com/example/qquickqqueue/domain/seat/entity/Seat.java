package com.example.qquickqqueue.domain.seat.entity;

import com.example.qquickqqueue.domain.enumPackage.Grade;
import com.example.qquickqqueue.domain.stadium.entity.Stadium;
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
public class Seat {
    @Id
    @Column(name = "SEAT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private long rowNum;

    @Column(nullable = false)
    private long columnNum;

    @Column(nullable = false)
    private Grade grade;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STADIUM_ID")
    private Stadium stadium;
}
