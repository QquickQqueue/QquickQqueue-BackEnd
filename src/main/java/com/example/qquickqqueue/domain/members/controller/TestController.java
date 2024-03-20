package com.example.qquickqqueue.domain.members.controller;

import com.example.qquickqqueue.util.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;

@Slf4j
@RestController
public class TestController {
    @GetMapping("/")
    public ResponseEntity<Message> test() {
        log.info("time ============= {}", LocalTime.now());
        return new ResponseEntity<>(new Message("ci/cd..", "해치웠나..?"), HttpStatus.OK);
    }
}
