package com.example.qquickqqueue.domain.members.controller;

import com.example.qquickqqueue.domain.members.dto.request.LoginRequestDto;
import com.example.qquickqqueue.domain.members.dto.request.SignupRequestDto;
import com.example.qquickqqueue.domain.members.dto.request.WithdrawalDto;
import com.example.qquickqqueue.domain.members.service.MembersService;
import com.example.qquickqqueue.security.userDetails.UserDetailsImpl;
import com.example.qquickqqueue.util.Message;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MembersController {

	private final MembersService membersService;


	// 회원 가입
	@PostMapping(value = "/signup")
	public ResponseEntity<Message> signup(@Valid @RequestBody SignupRequestDto memberRequestDto) {
		return membersService.signup(memberRequestDto);
	}

	// 로그인
	@PostMapping(value = "/login")
	public ResponseEntity<Message> login(@RequestBody LoginRequestDto loginRequestDto,
		HttpServletResponse httpServletResponse) {
		return membersService.login(loginRequestDto, httpServletResponse);
	}

	// 로그 아웃
	@GetMapping("logout")
	public ResponseEntity<Message> logout(@AuthenticationPrincipal UserDetailsImpl userDetails,
		HttpServletRequest httpServletRequest) {
		return membersService.logout(userDetails.getMember(), httpServletRequest);
	}

	// 회원 탈퇴
	@PostMapping("/withdrawal")
	public ResponseEntity<Message> withdrawal(@RequestBody WithdrawalDto withdrawalDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return membersService.withdrawal(withdrawalDto, userDetails.getMember());
	}
}
