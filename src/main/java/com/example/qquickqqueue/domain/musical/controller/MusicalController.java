package com.example.qquickqqueue.domain.musical.controller;

import com.example.qquickqqueue.domain.musical.service.MusicalService;
import com.example.qquickqqueue.util.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MusicalController {

    private final MusicalService musicalService;

    @GetMapping("/musicals")
    public ResponseEntity<Message> readMusicals(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return musicalService.readMusicals(pageable);
    }

    @GetMapping("/musicals/{musical-id}")
    public ResponseEntity<Message> readMusical(@PathVariable(name = "musical-id") Long musicalId) {
        return musicalService.readMusical(musicalId);
    }
}
