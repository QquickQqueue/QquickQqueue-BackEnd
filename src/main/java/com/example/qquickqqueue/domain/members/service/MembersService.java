package com.example.qquickqqueue.domain.members.service;

import com.example.qquickqqueue.domain.members.dto.request.LoginRequestDto;
import com.example.qquickqqueue.domain.members.dto.request.SignupRequestDto;
import com.example.qquickqqueue.domain.members.entity.Members;
import com.example.qquickqqueue.domain.members.repository.MembersRepository;
import com.example.qquickqqueue.redis.util.RedisUtil;
import com.example.qquickqqueue.security.jwt.JwtUtil;
import com.example.qquickqqueue.security.jwt.TokenDto;
import com.example.qquickqqueue.util.Message;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MembersService {

	private final PasswordEncoder passwordEncoder;
	private final MembersRepository membersRepository;

	private final JwtUtil jwtUtil;
	private final RedisUtil redisUtil;

	@Transactional
	public ResponseEntity<Message> signup(SignupRequestDto signupRequestDto) {
		String email = signupRequestDto.getEmail();
		String password = passwordEncoder.encode(signupRequestDto.getPassword());
		Optional<Members> findEmail = membersRepository.findByEmail(email);

		if (findEmail.isPresent()) {
			if (findEmail.get().getOutDate() != null) {
				throw new IllegalArgumentException("이미 탈퇴한 이메일입니다. email : " + email);
			}
			throw new IllegalArgumentException("이미 존재하는 이메일입니다. email : " + email);
		}

		Members member = Members.builder()
			.email(email)
			.password(password)
			.name(signupRequestDto.getName())
			.gender(signupRequestDto.getGender())
			.birth(signupRequestDto.getBirth())
			.phoneNumber(signupRequestDto.getPhoneNumber())
			.build();

		membersRepository.save(member);

		return new ResponseEntity<>(new Message("회원가입 성공", null), HttpStatus.OK);
	}

	// 로그인
	public ResponseEntity<Message> login(LoginRequestDto loginRequestDto, HttpServletResponse httpServletResponse) {
		String email = loginRequestDto.getEmail();
		Members member = membersRepository.findByEmail(email).orElseThrow(
			() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다. email : " + email)
		);

		// 비밀번호 틀렸을 때
		if (!passwordEncoder.matches(loginRequestDto.getPassword(), member.getPassword())) {
			throw new IllegalArgumentException("비밀번호를 틀렸습니다.");
		}

		TokenDto tokenDto = jwtUtil.createAllToken(member);
		String refreshToken = tokenDto.getRefreshToken();
		redisUtil.set(email, refreshToken, Duration.ofDays(7).toMillis());
		httpServletResponse.addHeader(JwtUtil.ACCESS_KEY, tokenDto.getAccessToken());
		httpServletResponse.addHeader(JwtUtil.REFRESH_KEY, tokenDto.getRefreshToken());
		return new ResponseEntity<>(new Message("로그인 성공", null), HttpStatus.OK);
	}

	// 로그아웃
	@Transactional
	public ResponseEntity<Message> logout(Members member,
		HttpServletRequest httpServletRequest) {
		String accessToken = httpServletRequest.getHeader("ACCESS-TOKEN").substring(7);
		String refreshToken = redisUtil.get(member.getEmail());

		if (!refreshToken.isEmpty()) {
			long tokenTime = jwtUtil.getExpirationTime(accessToken);
			redisUtil.setBlackList(accessToken, "ACCESS-TOKEN", tokenTime);
			redisUtil.delValues(member.getEmail());
			return new ResponseEntity<>(new Message("로그아웃 성공", null), HttpStatus.OK);
		} else {
			throw new UsernameNotFoundException("유저를 찾지 못했습니다. email : " + member.getEmail());
		}
	}
}
