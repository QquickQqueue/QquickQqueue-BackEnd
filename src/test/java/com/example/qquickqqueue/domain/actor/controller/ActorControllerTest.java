package com.example.qquickqqueue.domain.actor.controller;

import com.example.qquickqqueue.domain.actor.dto.ActorRequestDto;
import com.example.qquickqqueue.domain.actor.service.ActorService;
import com.example.qquickqqueue.util.Message;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class ActorControllerTest {

    @Mock
    private ActorService actorService;
    @Mock
    private ActorRequestDto actorRequestDto;

    @InjectMocks
    private ActorController actorController;

    @Test
    @DisplayName("saveActor test")
    void saveActorTest() {
        // given
        ResponseEntity<Message> responseEntity = new ResponseEntity<>(
                new Message("배우 등록 성공", null), HttpStatus.OK);

        when(actorService.saveActor(actorRequestDto)).thenReturn(responseEntity);

        // when
        ResponseEntity<Message> response = actorController.saveActor(actorRequestDto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("배우 등록 성공", Objects.requireNonNull(response.getBody()).getMessage());
    }
}