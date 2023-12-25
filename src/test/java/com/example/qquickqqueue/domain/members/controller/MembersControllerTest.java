package com.example.qquickqqueue.domain.members.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.qquickqqueue.domain.enumPackage.Gender;
import com.example.qquickqqueue.domain.members.dto.request.LoginRequestDto;
import com.example.qquickqqueue.domain.members.dto.request.SignupRequestDto;
import com.example.qquickqqueue.domain.members.dto.request.WithdrawalDto;
import com.example.qquickqqueue.domain.members.entity.Members;
import com.example.qquickqqueue.domain.members.service.MembersService;
import com.example.qquickqqueue.security.userDetails.UserDetailsImpl;
import com.example.qquickqqueue.util.Message;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
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
class MembersControllerTest {

	@Mock
	private MembersService membersService;
	@Mock
	private SignupRequestDto signupRequestDto;
	@Mock
	private LoginRequestDto loginRequestDto;

	@Mock
	private HttpServletResponse httpServletResponse;
	@Mock
	private HttpServletRequest httpServletRequest;
	@Mock
	private UserDetailsImpl userDetails;

	@InjectMocks
	private MembersController membersController;

	Members member = Members.builder()
		.email("test@test.com")
		.password("test1234")
		.name("tester")
		.gender(Gender.FEMALE)
		.birth(LocalDate.now())
		.phoneNumber("010-123-1234")
		.build();

	@Nested
	@DisplayName("회원가입")
	class signup {

		@Test
		@DisplayName("회원가입 컨트롤러 테스트")
		void singupControllerTest() {
			// given
			ResponseEntity<Message> responseEntity = new ResponseEntity<>(
				new Message("회원가입 성공", null), HttpStatus.OK);

			when(membersService.signup(signupRequestDto)).thenReturn(responseEntity);

			// when
			ResponseEntity<Message> response = membersController.signup(signupRequestDto);

			// then
			assertEquals(HttpStatus.OK, response.getStatusCode());
			assertEquals("회원가입 성공", response.getBody().getMessage());
		}
	}

	@Nested
	@DisplayName("로그인")
	class login{

		@Test
		@DisplayName("로그인 컨트롤러 테스트")
		void loginControllerTest() {
			//given
			ResponseEntity<Message> responseEntity = new ResponseEntity<>(
				new Message("로그인 성공", null), HttpStatus.OK);
			when(membersService.login(loginRequestDto, httpServletResponse)).thenReturn(
				responseEntity);

			//when
			ResponseEntity<Message> response = membersController.login(loginRequestDto,
				httpServletResponse);

			//then
			assertEquals(HttpStatus.OK, response.getStatusCode());
			assertEquals("로그인 성공", response.getBody().getMessage());
		}
	}

	@Nested
	@DisplayName("로그아웃")
	class logout{
		@Test
		@DisplayName("로그아웃 컨트롤러 테스트")
		void logoutControllerTest() {
			//given
			ResponseEntity<Message> responseEntity = new ResponseEntity<>(new Message("로그아웃 성공", null),
				HttpStatus.OK);
			when(membersService.logout(any(), any(httpServletRequest.getClass()))).thenReturn(responseEntity);

			//when
			ResponseEntity<Message> response = membersController.logout(userDetails, httpServletRequest);

			//then
			assertEquals(HttpStatus.OK, response.getStatusCode());
			assertEquals("로그아웃 성공", response.getBody().getMessage());
		}
	}

	@Nested
	@DisplayName("회원탈퇴")
	class Withdrawal {
		@Test
		@DisplayName("회원탈퇴")
		void withdrawalControllerTest() {
			//given
			WithdrawalDto withdrawalDto = WithdrawalDto.builder()
				.password("password").build();

			ResponseEntity<Message> responseEntity = new ResponseEntity<>(new Message("회원탈퇴 성공", null), HttpStatus.OK);
			when(membersService.withdrawal(withdrawalDto, userDetails.getMember())).thenReturn(responseEntity);

			//when
			ResponseEntity<Message> response = membersController.withdrawal(withdrawalDto, userDetails);

			//then
			assertEquals(HttpStatus.OK, response.getStatusCode());
			assertEquals("회원탈퇴 성공", response.getBody().getMessage());
		}
	}
}