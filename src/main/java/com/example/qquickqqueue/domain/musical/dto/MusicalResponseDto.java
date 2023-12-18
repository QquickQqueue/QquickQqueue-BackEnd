package com.example.qquickqqueue.domain.musical.dto;

import com.example.qquickqqueue.domain.enumPackage.Rating;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.sql.Time;
import java.util.Date;

@Getter
@Builder
@AllArgsConstructor
public class MusicalResponseDto {
    private String title;
    private Rating rating;
    private String thumbnailUrl;
    private String description;
    private Date startDate;
    private Date endDate;
    private Time runningTime;
}
