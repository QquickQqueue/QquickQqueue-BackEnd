package com.example.qquickqqueue.domain.seatGrade.entity;

import com.example.qquickqqueue.domain.enumPackage.Grade;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class SeatGrade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Grade grade;

    @Column(nullable = false)
    private int price;
}
