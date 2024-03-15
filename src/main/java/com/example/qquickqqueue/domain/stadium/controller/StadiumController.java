package com.example.qquickqqueue.domain.stadium.controller;

import com.example.qquickqqueue.domain.stadium.dto.request.StadiumRequestDto;
import com.example.qquickqqueue.domain.stadium.service.StadiumService;
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
public class StadiumController {
    private final StadiumService stadiumService;

    @PostMapping("/stadium")
    public ResponseEntity<Message> saveStadium(@RequestBody StadiumRequestDto stadiumRequestDto) {
        return stadiumService.saveStadium(stadiumRequestDto);
    }
}
