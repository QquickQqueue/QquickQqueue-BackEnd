package com.example.qquickqqueue.domain.members.service;

import com.example.qquickqqueue.domain.members.dto.request.LoginRequestDto;
import com.example.qquickqqueue.domain.members.dto.request.SignupRequestDto;
import com.example.qquickqqueue.domain.members.dto.request.WithdrawalDto;
import com.example.qquickqqueue.domain.members.dto.response.MemberInfoResponseDto;
import com.example.qquickqqueue.domain.members.entity.Members;
import com.example.qquickqqueue.domain.members.repository.MembersRepository;
import com.example.qquickqqueue.redis.util.RedisUtil;
import com.example.qquickqqueue.security.jwt.JwtUtil;
import com.example.qquickqqueue.security.jwt.TokenDto;
import com.example.qquickqqueue.util.Message;
import jakarta.persistence.EntityExistsException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.security.InvalidParameterException;
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
			Members pMember = findEmail.get();
			if (pMember.getOutDate() != null) {
				throw new IllegalStateException("이미 탈퇴한 이메일입니다. email : " + email);
			} else if (pMember.isKakaoEmail() && pMember.getCreateAt().equals(pMember.getModifiedDate())) {
				 pMember.setPassword(password);
				 membersRepository.save(pMember);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      
				 return new ResponseEntity<>(new Message("카카오 연동 성공", null), HttpStatus.OK);
			} else {
				throw new EntityExistsException("이미 존재하는 이메일입니다. email : " + email);
			}
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
		if (member.getOutDate() != null) {
			throw new IllegalStateException("이미 탈퇴한 이메일입니다. email : " + email);
		}
		// 비밀번호 틀렸을 때
		if (!passwordEncoder.matches(loginRequestDto.getPassword(), member.getPassword())) {
			throw new InvalidParameterException("비밀번호를 틀렸습니다.");
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

	// 회원탈퇴
	@Transactional
	public ResponseEntity<Message> withdrawal(WithdrawalDto withdrawalDto, Members member) {
		if (!passwordEncoder.matches(withdrawalDto.getPassword(), member.getPassword())) {
			throw new InvalidParameterException("비밀번호를 틀렸습니다.");
		} else {
			member.updateDate();
			membersRepository.save(member);
			return new ResponseEntity<>(new Message("회원탈퇴 성공", null), HttpStatus.OK);
		}
	}

	public ResponseEntity<Message> getMemberInfo(Members members) {
		Members memberInfo = membersRepository.findByEmail(members.getEmail())
				.orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾을 수 없습니다. email : " + members.getEmail()));

		return new ResponseEntity<>(new Message("회원정보 조회 성공", MemberInfoResponseDto.builder()
				.email(memberInfo.getEmail())
				.name(memberInfo.getName())
				.gender(memberInfo.getGender())
				.birth(memberInfo.getBirth())
				.phoneNumber(memberInfo.getPhoneNumber())
				.createAt(memberInfo.getCreateAt())
				.isKakaoEmail(memberInfo.isKakaoEmail())
				.build()), HttpStatus.OK);
	}
}
