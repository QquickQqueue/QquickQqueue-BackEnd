package com.example.qquickqqueue.domain.musical.entity;

import com.example.qquickqqueue.domain.enumPackage.Rating;
import com.example.qquickqqueue.domain.stadium.entity.Stadium;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Musical {
    @Id
    @Column(name = "MUSICAL_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String title;

    @Column(nullable = false)
    private Rating rating;

    @Column
    private String thumbnailUrl;

    @Column
    private String description;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private LocalTime runningTime;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STADIUM_ID")
    private Stadium stadium;
}
