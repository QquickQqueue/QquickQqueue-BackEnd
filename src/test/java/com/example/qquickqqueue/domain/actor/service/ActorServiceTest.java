package com.example.qquickqqueue.domain.actor.service;

import com.example.qquickqqueue.domain.actor.dto.ActorRequestDto;
import com.example.qquickqqueue.domain.actor.entity.Actor;
import com.example.qquickqqueue.domain.actor.repository.ActorRepository;
import com.example.qquickqqueue.domain.enumPackage.Gender;
import com.example.qquickqqueue.util.Message;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class ActorServiceTest {
    @Mock
    private ActorRepository actorRepository;
    @InjectMocks
    private ActorService actorService;

    @Test
    @DisplayName("배우 등록 성공")
    void saveActorSuccess() {
        // given
        ActorRequestDto actorRequestDto = ActorRequestDto.builder()
                .actorName("이창섭이히히").gender(Gender.MALE).build();

        Actor actor = Actor.builder().actorName(actorRequestDto.getActorName())
                .gender(actorRequestDto.getGender()).build();

        when(actorRepository.save(any())).thenReturn(actor);

        ResponseEntity<Message> response = actorService.saveActor(actorRequestDto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("배우 등록 성공", response.getBody().getMessage());
    }
}