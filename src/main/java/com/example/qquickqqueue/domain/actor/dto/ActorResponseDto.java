package com.example.qquickqqueue.domain.actor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ActorResponseDto {
    private String actorName;
}
