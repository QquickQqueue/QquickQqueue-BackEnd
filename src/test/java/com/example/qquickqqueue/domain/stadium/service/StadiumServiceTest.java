package com.example.qquickqqueue.domain.stadium.service;

import com.example.qquickqqueue.domain.seat.entity.Seat;
import com.example.qquickqqueue.domain.seat.repository.SeatRepository;
import com.example.qquickqqueue.domain.stadium.dto.request.StadiumRequestDto;
import com.example.qquickqqueue.domain.stadium.entity.Stadium;
import com.example.qquickqqueue.domain.stadium.repository.StadiumRepository;
import com.example.qquickqqueue.util.Message;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class StadiumServiceTest {
    @Mock
    private StadiumRepository stadiumRepository;
    @Mock
    private SeatRepository seatRepository;
    @InjectMocks
    private StadiumService stadiumService;

    @Nested
    @DisplayName("saveStadium Method Test")
    class SaveStadium {
        @Test
        @DisplayName("saveStadium Method Test")
        void saveStadium() {
            // given
            StadiumRequestDto stadiumRequestDto = StadiumRequestDto.builder()
                    .stadiumName("예술의 전당")
                    .stadiumAddress("")
                    .maxRowNum(20)
                    .maxColumnNum(30)
                    .build();
            Stadium stadium = Stadium.builder()
                    .id(1L)
                    .stadiumName(stadiumRequestDto.getStadiumName())
                    .address(stadiumRequestDto.getStadiumAddress())
                    .build();
            when(stadiumRepository.save(any())).thenReturn(stadium);
            when(seatRepository.save(any())).thenReturn(Seat.builder()
                    .stadium(stadium)
                    .rowNum(2)
                    .columnNum(3)
                    .build());

            // when
            ResponseEntity<Message> response = stadiumService.saveStadium(stadiumRequestDto);

            // then
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("공연장 등록 성공", response.getBody().getMessage());
        }
    }
}