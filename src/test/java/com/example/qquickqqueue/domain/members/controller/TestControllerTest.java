package com.example.qquickqqueue.domain.members.controller;

import com.example.qquickqqueue.util.Message;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({MockitoExtension.class})
class TestControllerTest {
    @InjectMocks
    private TestController TestController;
    @Test
    void test() {
        ResponseEntity<Message> response = TestController.test();
        assertEquals("test", Objects.requireNonNull(response.getBody()).getMessage());
    }
}