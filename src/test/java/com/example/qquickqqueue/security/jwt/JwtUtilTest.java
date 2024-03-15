package com.example.qquickqqueue.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.qquickqqueue.domain.enumPackage.Gender;
import com.example.qquickqqueue.domain.members.entity.Members;
import com.example.qquickqqueue.domain.members.repository.MembersRepository;
import com.example.qquickqqueue.redis.util.RedisUtil;
import com.example.qquickqqueue.security.userDetails.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.security.Key;
import java.time.Duration;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;

@ExtendWith({MockitoExtension.class})
public class JwtUtilTest {

	@Mock
	private Key key;
	@Mock
	private SignatureAlgorithm signatureAlgorithm;
	@Mock
	private HttpServletRequest httpServletRequest;
	@Mock
	private HttpServletResponse httpServletResponse;
	@Mock
	private RedisUtil redisUtil;
	@Mock
	private UserDetailsServiceImpl userDetailsService;
	@Mock
	private UserDetails userDetails;
	@Mock
	private MembersRepository membersRepository;
	@InjectMocks
	JwtUtil jwtUtil;

	private static final String BEARER_PREFIX = "Bearer ";
	public static final String ACCESS_KEY = "ACCESS_KEY";
	public static final String REFRESH_KEY = "REFRESH_KEY";
	private static final long ACCESS_TIME = Duration.ofMinutes(30).toMillis();
	private static final long REFRESH_TIME = Duration.ofDays(7).toMillis();

	Members member = Members.builder()
	.email("test@test.com")
	.password("test1234")
	.name("tester")
	.gender(Gender.FEMALE)
	.birth(LocalDate.now())
	.phoneNumber("010-123-1234")
	.outDate(LocalDate.now())
	.build();

	@Nested
	@DisplayName("resolveToken Method 테스트")
	class ResolveToken {

		@Test
		@DisplayName("resolveToken test success")
		void resolveToken_success() {
			// given
			String tokenName = "ACCESS-TOKEN";
			String bearerToken = "Bearer accessToken";

			when(httpServletRequest.getHeader(tokenName)).thenReturn(bearerToken);

			// when
			String resolveToken = jwtUtil.resolveToken(httpServletRequest, tokenName);

			// then
			assertEquals("accessToken", resolveToken);
		}

		@Test
		@DisplayName("resolveToken test success with Refresh")
		void resolveToken_success_with_Refresh() {
			// given
			String tokenName = "REFRESH-TOKEN";
			String bearerToken = "Bearer refreshToken";

			when(httpServletRequest.getHeader(tokenName)).thenReturn(bearerToken);

			// when
			String resolveToken = jwtUtil.resolveToken(httpServletRequest, tokenName);

			// then
			assertEquals("refreshToken", resolveToken);
		}

		@Test
		@DisplayName("resolveToken test null with StringUtils.hasText(bearerToken) is False")
		void resolveToken_null_with_bearerToken_is_false() {
			String tokenName = "ACCESS-TOKEN";

			when(httpServletRequest.getHeader(tokenName)).thenReturn(null);
			
			String token = jwtUtil.resolveToken(httpServletRequest, tokenName);

			assertNull(token);
		}

		@Test
		@DisplayName("resolveToken test null with bearerToken.startsWith(BEARER_PREFIX) is False")
		void resolveToken_null_with_BEARER_PREFIX_is_false() {
			String tokenName = "ACCESS-TOKEN";
			String bearerToken = "accessToken";

			when(httpServletRequest.getHeader(tokenName)).thenReturn(bearerToken);

			String token = jwtUtil.resolveToken(httpServletRequest, tokenName);

			assertNull(token);
		}
	}

	@Nested
	@DisplayName("setHeaderToken method 테스트")
	class setHeaderToken {

		@Test
		@DisplayName("setHeaderToken success 테스트")
		void setHeaderToken_success () {
			String accessToken = "accessToken";
			String refreshToken = "refreshToken";

			jwtUtil.setHeaderToken(httpServletResponse, accessToken, refreshToken);

			verify(httpServletResponse).setHeader(JwtUtil.ACCESS_KEY, accessToken);
			verify(httpServletResponse).setHeader(JwtUtil.REFRESH_KEY, refreshToken);
		}
	}

	@Nested
	@DisplayName("createAuthenticationTest method 테스트")
	class createAuthenticationTest {

		@Test
		@DisplayName("createAuthentication success 테스트")
		void createAuthenticationTest_success () {
			String email = "test@test.com";
			when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);

			Authentication response = jwtUtil.createAuthentication(email);

			assertEquals(userDetails, response.getPrincipal());
			assertNull(response.getCredentials());
			assertEquals(userDetails.getAuthorities(), response.getAuthorities());
		}
	}

	@Nested
	@DisplayName("existsRefreshToken method 테스트")
	class existsRefreshTokenTest {

		@Test
		@DisplayName("existsRefreshToken success 테스트")
		void existsRefreshTokenTest_success () {
			String email = "test@test.com";
			when(redisUtil.get(email)).thenReturn("refreshToken");

			boolean exists = jwtUtil.existsRefreshToken(email);

			assertTrue(exists);
		}

		@Test
		@DisplayName("existsRefreshToken return false 테스트")
		void existsRefreshTokenTest_return_false () {
			String email = "test@test.com";
			when(redisUtil.get(email)).thenReturn(null);

			boolean exists = jwtUtil.existsRefreshToken(email);

			assertFalse(exists);
		}
	}
}
