package com.example.qquickqqueue.domain.stadium.dto.request;

import com.example.qquickqqueue.domain.seat.dto.SeatRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class StadiumRequestDto {
    private String stadiumName;
    private String stadiumAddress;
    private List<SeatRequestDto> seatList;
}
