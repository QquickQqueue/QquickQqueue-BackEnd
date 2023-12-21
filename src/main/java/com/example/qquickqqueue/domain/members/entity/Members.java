package com.example.qquickqqueue.domain.members.entity;

import com.example.qquickqqueue.domain.enumPackage.Gender;
import com.example.qquickqqueue.util.Timestamped;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Members extends Timestamped {
    @Id
    @Column(name = "EMAIL", length = 50)
    private String email;

    @Column(nullable = false, length = 20)
    @JsonIgnore
    private String password;

    @Column(nullable = false, length = 10)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private Date birth;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private Date joinDate;

    @Column
    private Date outDate;
}
