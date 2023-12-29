package com.example.qquickqqueue.domain.members.dto.request;

import com.example.qquickqqueue.domain.enumPackage.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class KakaoMemberInfoDto {
    private String name;
    private String email;
    private Gender gender;
    private LocalDate birth;
    private String phoneNumber;
}
