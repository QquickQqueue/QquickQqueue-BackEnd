package com.example.qquickqqueue.domain.actor.controller;

import com.example.qquickqqueue.domain.actor.dto.ActorRequestDto;
import com.example.qquickqqueue.domain.actor.service.ActorService;
import com.example.qquickqqueue.util.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ActorController {
    private final ActorService actorService;

    @PostMapping("/actor")
    public ResponseEntity<Message> saveActor(@RequestBody ActorRequestDto actorRequestDto) {
        return actorService.saveActor(actorRequestDto);
    }
}
