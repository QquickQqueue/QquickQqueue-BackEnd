package com.example.qquickqqueue.domain.musical.dto;

import com.example.qquickqqueue.domain.enumPackage.Rating;
import com.example.qquickqqueue.domain.schedule.dto.ScheduleRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class MusicalSaveRequestDto {
    private String title;
    private String description;
    private String thumbnailUrl;
    private Long stadiumId;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime runningTime;
    private Rating rating;
    private List<ScheduleRequestDto> scheduleList;
    private int priceOfVip, priceOfR, priceOfS, priceOfA;
}
