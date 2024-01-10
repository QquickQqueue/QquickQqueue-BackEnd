package com.example.qquickqqueue.domain.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ScheduleRequestDto {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<String> actorName;
}
