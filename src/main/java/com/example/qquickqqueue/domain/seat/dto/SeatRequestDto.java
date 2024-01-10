package com.example.qquickqqueue.domain.seat.dto;

import com.example.qquickqqueue.domain.enumPackage.Grade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SeatRequestDto {
    private int rowNum;
    private int columnNum;
    private Grade grade;
}
