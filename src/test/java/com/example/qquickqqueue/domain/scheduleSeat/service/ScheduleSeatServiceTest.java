package com.example.qquickqqueue.domain.scheduleSeat.service;

import com.example.qquickqqueue.domain.musical.entity.Musical;
import com.example.qquickqqueue.domain.schedule.entity.Schedule;
import com.example.qquickqqueue.domain.scheduleSeat.dto.ScheduleSeatResponseDto;
import com.example.qquickqqueue.domain.scheduleSeat.entity.ScheduleSeat;
import com.example.qquickqqueue.domain.scheduleSeat.repository.ScheduleSeatRepository;
import com.example.qquickqqueue.domain.seatGrade.entity.SeatGrade;
import com.example.qquickqqueue.util.Message;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class ScheduleSeatServiceTest {
    @Mock
    private ScheduleSeatRepository scheduleSeatRepository;
    @InjectMocks
    private ScheduleSeatService scheduleSeatService;

    @Nested
    @DisplayName("readScheduleSeat Method Test")
    class ReadScheduleSeat {
        @Test
        @DisplayName("readScheduleSeat Method Success Test")
        void readScheduleSeatSuccess() {
            // given
            Musical musical = Musical.builder()
                    .title("뮤지컬")
                    .build();
            Schedule schedule = Schedule.builder()
                    .musical(musical)
                    .build();
            SeatGrade seatGrade = SeatGrade.builder()
                    .price(100000)
                    .build();
            ScheduleSeat scheduleSeat = ScheduleSeat.builder()
                    .schedule(schedule)
                    .seatGrade(seatGrade)
                    .build();
            Long scheduleSeatId = 1L;
            when(scheduleSeatRepository.findById(scheduleSeatId)).thenReturn(Optional.of(scheduleSeat));

            // when
            ResponseEntity<Message> response = scheduleSeatService.readScheduleSeat(scheduleSeatId);
            ScheduleSeatResponseDto scheduleSeatResponseDto = (ScheduleSeatResponseDto) response.getBody().getData();
            // then
            assertEquals("조회 성공", response.getBody().getMessage());
            assertEquals("뮤지컬", scheduleSeatResponseDto.getName());
            assertEquals(100000, scheduleSeatResponseDto.getPrice());
        }

        @Test
        @DisplayName("readScheduleSeat Method fail Test")
        void readScheduleSeatFail() {
            // given
            Long scheduleSeatId = 1L;
            when(scheduleSeatRepository.findById(scheduleSeatId)).thenReturn(Optional.empty());

            // when
            EntityNotFoundException response = assertThrows(EntityNotFoundException.class,
                    () -> scheduleSeatService.readScheduleSeat(scheduleSeatId));

            // then
            assertEquals("존재하지 않는 스케줄 좌석입니다. ScheduleSeatId : " + scheduleSeatId, response.getMessage());
        }
    }
}