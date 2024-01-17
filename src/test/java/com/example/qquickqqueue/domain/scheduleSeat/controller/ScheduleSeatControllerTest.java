package com.example.qquickqqueue.domain.scheduleSeat.controller;

import com.example.qquickqqueue.domain.scheduleSeat.dto.ScheduleSeatResponseDto;
import com.example.qquickqqueue.domain.scheduleSeat.service.ScheduleSeatService;
import com.example.qquickqqueue.util.Message;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class ScheduleSeatControllerTest {
    @Mock
    private ScheduleSeatService scheduleSeatService;
    @InjectMocks
    private ScheduleSeatController scheduleSeatController;
    @Test
    @DisplayName("readScheduleSeat Method Test")
    void readScheduleSeatTest() {
        // given
        Long scheduleSeatId = 1L;
        ResponseEntity<Message> responseValue = new ResponseEntity<>(new Message("조회 성공", ScheduleSeatResponseDto.builder()
                .name("뮤지컬")
                .price(100000)
                .build()), HttpStatus.OK);
        when(scheduleSeatController.readScheduleSeat(scheduleSeatId)).thenReturn(responseValue);

        // when
        ResponseEntity<Message> response = scheduleSeatController.readScheduleSeat(scheduleSeatId);

        // then
        assertEquals(responseValue, response);
    }
}