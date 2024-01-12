package com.example.qquickqqueue.domain.members.entity;

import com.example.qquickqqueue.domain.enumPackage.Gender;
import com.example.qquickqqueue.util.Timestamped;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Members extends Timestamped {
    @Id
    @Column(name = "EMAIL", length = 50)
    private String email;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(nullable = false, length = 10)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private LocalDate birth;

    @Column(nullable = false)
    @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", message = "휴대폰 번호를 확인해주세요.")
    private String phoneNumber;

    @Column
    private LocalDate outDate;

    @Column(nullable = false)
    private boolean isKakaoEmail;

    public void updateDate() {
        this.outDate = LocalDate.now();
    }

    public void setIsKakaoEmail() {
        this.isKakaoEmail = !isKakaoEmail;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
