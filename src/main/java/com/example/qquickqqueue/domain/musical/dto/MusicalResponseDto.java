package com.example.qquickqqueue.domain.musical.dto;

import com.example.qquickqqueue.domain.enumPackage.Rating;
import com.example.qquickqqueue.domain.schedule.dto.ScheduleResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class MusicalResponseDto {
    private Long id;
    private String title;
    private Rating rating;
    private String thumbnailUrl;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Time runningTime;
    private List<ScheduleResponseDto> scheduleList;
}
