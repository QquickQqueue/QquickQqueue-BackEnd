package com.example.qquickqqueue.domain.stadium.entity;

import com.example.qquickqqueue.domain.seat.entity.Seat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Stadium {
    @Id
    @Column(name = "STADIUM_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String stadiumName;

    @Column(nullable = false, length = 50)
    private String address;

    @OneToMany(mappedBy = "stadium", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Seat> seats = new ArrayList<>();
}
