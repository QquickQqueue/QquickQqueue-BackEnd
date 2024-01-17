package com.example.qquickqqueue.domain.members.controller;

import com.example.qquickqqueue.domain.members.service.KakaoMembersService;
import com.example.qquickqqueue.security.userDetails.UserDetailsImpl;
import com.example.qquickqqueue.util.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
public class KakaoMembersControllerTest {
	@Mock
	private KakaoMembersService kakaoMembersService;
	@Mock
	private HttpServletResponse httpServletResponse;
	@Mock
	private UserDetailsImpl userDetails;
	@InjectMocks
	private KakaoMembersController kakaoMembersController;

	@Nested
	@DisplayName("카카오 로그인")
	class kakaoLogin {
		@Test
		@DisplayName("카카오 로그인 컨트롤러 테스트")
		void kakaoLoginControllerTest() throws JsonProcessingException {
			// given
			String code = "testCode";

			ResponseEntity<Message> responseEntity = new ResponseEntity<>(new Message("로그인 성공", null), HttpStatus.OK);
			when(kakaoMembersService.kakaoLogin(code, httpServletResponse)).thenReturn(responseEntity);

			// when
			ResponseEntity<Message> response = kakaoMembersController.kakaoLogin(code, httpServletResponse);

			// then
			assertEquals(HttpStatus.OK, response.getStatusCode());
			assertEquals("로그인 성공", Objects.requireNonNull(response.getBody()).getMessage());
		}
	}

	@Nested
	@DisplayName("카카오 탈퇴")
	class kakaoWithdrawal {
		@Test
		@DisplayName("카카오 탈퇴 컨트롤러 테스트")
		void kakaoWithdrawalTest() {
			// Given
			String accessToken = "access_token";

			ResponseEntity<Message> response = new ResponseEntity<>(new Message("카카오 탈퇴 성공", null), HttpStatus.OK);
			when(kakaoMembersService.kakaoWithdrawal(accessToken, userDetails.getMember())).thenReturn(response);

			// When
			ResponseEntity<Message> responseEntity = kakaoMembersController.kakaoWithdrawal(accessToken, userDetails);

			// Then
			assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
			assertEquals("카카오 탈퇴 성공", Objects.requireNonNull(responseEntity.getBody()).getMessage());
		}
	}
}
