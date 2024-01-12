package com.example.qquickqqueue.domain.members.controller;

import com.example.qquickqqueue.domain.members.service.KakaoMembersService;
import com.example.qquickqqueue.security.userDetails.UserDetailsImpl;
import com.example.qquickqqueue.util.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KakaoMembersController {
    private final KakaoMembersService kakaoMembersService;

    @GetMapping("/api/kakao")
    public ResponseEntity<Message> kakaoLogin(@RequestParam("accessToken") String accessToken, HttpServletResponse response) throws JsonProcessingException {
        return kakaoMembersService.kakaoLogin(accessToken, response);
    }

    @GetMapping("/api/kakao/withdrawal")
    public ResponseEntity<Message> kakaoWithdrawal(@RequestParam("accessToken") String accessToken, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return kakaoMembersService.kakaoWithdrawal(accessToken, userDetails.getMember());
    }
}
