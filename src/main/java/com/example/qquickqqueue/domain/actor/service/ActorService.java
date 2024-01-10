package com.example.qquickqqueue.domain.actor.service;

import com.example.qquickqqueue.domain.actor.dto.ActorRequestDto;
import com.example.qquickqqueue.domain.actor.entity.Actor;
import com.example.qquickqqueue.domain.actor.repository.ActorRepository;
import com.example.qquickqqueue.util.Message;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActorService {
    private final ActorRepository actorRepository;

    @Transactional
    public ResponseEntity<Message> saveActor(ActorRequestDto actorRequestDto) {
        actorRepository.save(Actor.builder()
                .actorName(actorRequestDto.getActorName())
                .gender(actorRequestDto.getGender())
                .build());
        return new ResponseEntity<>(new Message("배우 등록 성공", null), HttpStatus.OK);
    }
}
