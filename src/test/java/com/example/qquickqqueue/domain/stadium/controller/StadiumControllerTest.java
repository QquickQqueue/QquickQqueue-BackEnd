package com.example.qquickqqueue.domain.stadium.controller;

import com.example.qquickqqueue.domain.stadium.dto.request.StadiumRequestDto;
import com.example.qquickqqueue.domain.stadium.service.StadiumService;
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
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class StadiumControllerTest {
    @Mock
    private StadiumService stadiumService;
    @InjectMocks
    private StadiumController stadiumController;

    @Nested
    @DisplayName("SaveStadium Method Test")
    class SaveStadium {
        @Test
        @DisplayName("SaveStadium Method Test")
        void saveStadium() {
            // given
            StadiumRequestDto stadiumRequestDto = StadiumRequestDto.builder().build();
            ResponseEntity<Message> responseEntity = new ResponseEntity<>(new Message("공연장 등록 성공", null), HttpStatus.OK);

            when(stadiumService.saveStadium(stadiumRequestDto)).thenReturn(responseEntity);

            // when
            ResponseEntity<Message> response = stadiumController.saveStadium(stadiumRequestDto);

            // then
            assertEquals(responseEntity, response);
        }
    }
}

