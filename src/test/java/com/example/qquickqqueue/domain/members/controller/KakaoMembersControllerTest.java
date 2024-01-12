package com.example.qquickqqueue.domain.members.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.example.qquickqqueue.domain.members.service.KakaoMembersService;
import com.example.qquickqqueue.util.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith({MockitoExtension.class})
public class KakaoMembersControllerTest {
	@Mock
	private KakaoMembersService kakaoMembersService;
	@Mock
	private HttpServletResponse httpServletResponse;
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
}
