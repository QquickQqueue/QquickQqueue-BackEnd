package com.example.qquickqqueue.domain.scheduleSeat.controller;

import com.example.qquickqqueue.domain.scheduleSeat.service.ScheduleSeatService;
import com.example.qquickqqueue.util.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ScheduleSeatController {
    private final ScheduleSeatService scheduleSeatService;

    @GetMapping("/schedule-seat/{schedule-seat-id}")
    public ResponseEntity<Message> readScheduleSeat(@PathVariable("schedule-seat-id") Long scheduleSeatId) {
        return scheduleSeatService.readScheduleSeat(scheduleSeatId);
    }
}
