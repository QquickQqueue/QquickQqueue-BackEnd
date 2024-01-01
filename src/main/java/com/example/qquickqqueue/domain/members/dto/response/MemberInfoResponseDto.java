package com.example.qquickqqueue.domain.members.dto.response;

import com.example.qquickqqueue.domain.enumPackage.Gender;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class MemberInfoResponseDto {
    private String email;
    private String name;
    private Gender gender;
    private LocalDate birth;
    private String phoneNumber;
    private LocalDateTime createAt;
    private boolean isKakaoEmail;
}
