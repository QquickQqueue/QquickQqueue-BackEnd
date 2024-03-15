package com.example.qquickqqueue.domain.seatGrade.entity;

import com.example.qquickqqueue.domain.enumPackage.Grade;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatGrade {
    @Id
    @Column(name = "SEAT_GRADE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Grade grade;

    @Column(nullable = false)
    private int price;
}
