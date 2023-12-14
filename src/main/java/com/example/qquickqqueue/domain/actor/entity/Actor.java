package com.example.qquickqqueue.domain.actor.entity;

import com.example.qquickqqueue.domain.enumPackage.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Actor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String actorName;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Gender gender;
}
