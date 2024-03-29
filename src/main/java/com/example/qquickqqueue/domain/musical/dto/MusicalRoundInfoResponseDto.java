package com.example.qquickqqueue.domain.musical.dto;

import com.example.qquickqqueue.domain.actor.dto.ActorResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class MusicalRoundInfoResponseDto {
    private int sumVIP;
    private int sumR;
    private int sumS;
    private int sumA;
    private List<ActorResponseDto> actors;
}
