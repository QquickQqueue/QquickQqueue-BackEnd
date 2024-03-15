package com.example.qquickqqueue.domain.ticket.dto;

import com.example.qquickqqueue.domain.enumPackage.Grade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TicketResponseDto {
    private Long ticketId;
    private String email;
    private String musicalTitle;
    private Long scheduleId;
    private String stadiumName;
    private Grade seatGrade;
    private long rowNum;
    private long columnNum;
    private boolean status;
}
