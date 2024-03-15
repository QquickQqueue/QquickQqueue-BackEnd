package com.example.qquickqqueue.domain.stadium.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class StadiumRequestDto {
    private String stadiumName;
    private String stadiumAddress;
    private int rowNum;
    private int columnNum;
}
