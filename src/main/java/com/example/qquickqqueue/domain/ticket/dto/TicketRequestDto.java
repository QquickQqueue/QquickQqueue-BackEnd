package com.example.qquickqqueue.domain.ticket.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class TicketRequestDto {
    private Long scheduleSeatId;

    @JsonCreator
    public TicketRequestDto(@JsonProperty("scheduleSeatId") Long scheduleSeatId) {
        this.scheduleSeatId = scheduleSeatId;
    }
}
