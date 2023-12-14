package com.example.qquickqqueue.domain.members.entity;

import com.example.qquickqqueue.domain.enumPackage.Gender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Members {
    @Id
    private String email;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(nullable = false)
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

    @Column
    private String profileImage;
}
