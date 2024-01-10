package com.example.qquickqqueue.domain.actor.dto;

import com.example.qquickqqueue.domain.enumPackage.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ActorRequestDto {
    private String actorName;
    private Gender gender;
}
