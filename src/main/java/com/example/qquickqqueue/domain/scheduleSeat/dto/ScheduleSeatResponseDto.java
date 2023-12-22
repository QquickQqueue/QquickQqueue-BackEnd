package com.example.qquickqqueue.domain.scheduleSeat.dto;

import com.example.qquickqqueue.domain.enumPackage.Grade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ScheduleSeatResponseDto {
    private Long id;
    private boolean isReserved;
    private long rowNum;
    private long columnNum;
    private Grade grade;
    private int price;
}
