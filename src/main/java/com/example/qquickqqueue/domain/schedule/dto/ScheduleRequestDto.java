package com.example.qquickqqueue.domain.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ScheduleRequestDto {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
