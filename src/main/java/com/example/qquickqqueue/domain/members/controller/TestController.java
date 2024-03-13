package com.example.qquickqqueue.domain.members.controller;

import com.example.qquickqqueue.util.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/")
    public ResponseEntity<Message> test() {
        return new ResponseEntity<>(new Message("test", "test"), HttpStatus.OK);
    }
}
