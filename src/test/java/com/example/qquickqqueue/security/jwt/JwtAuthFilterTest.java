package com.example.qquickqqueue.security.jwt;

import com.example.qquickqqueue.domain.members.entity.Members;
import com.example.qquickqqueue.domain.members.repository.MembersRepository;
import com.example.qquickqqueue.redis.util.RedisUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthFilterTest {
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private RedisUtil redisUtil;
    @Mock
    private MembersRepository membersRepository;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    @Nested
    @DisplayName("doFilterInternal Test")
    class DoFilterInternal {
        @Test
        @DisplayName("Valid Access Token")
        void validAccessTokenTest() throws ServletException, IOException {
            // Given
            when(jwtUtil.resolveToken(any(), any())).thenReturn("access_token");
            when(jwtUtil.validateToken("access_token")).thenReturn(true);

            // When
            jwtAuthFilter.doFilterInternal(request, response, filterChain);

            // Then
            verify(jwtUtil).resolveToken(request, JwtUtil.ACCESS_KEY);
            verify(jwtUtil).validateToken("access_token");
            verify(filterChain).doFilter(request, response);
        }

        @Test
        @DisplayName("No Access Token")
        void noAccessTokenTest() throws ServletException, IOException {
            // Given
            when(jwtUtil.resolveToken(request, JwtUtil.ACCESS_KEY)).thenReturn(null);

            // When
            jwtAuthFilter.doFilterInternal(request, response, filterChain);

            // Then
            verify(jwtUtil).resolveToken(request, JwtUtil.ACCESS_KEY);
            verify(filterChain).doFilter(request, response);
        }

        @Test
        @DisplayName("No Refresh Token")
        void noRefreshTokenTest() throws ServletException, IOException {
            // Given
            when(jwtUtil.resolveToken(request, JwtUtil.ACCESS_KEY)).thenReturn("access_token");
            when(jwtUtil.validateToken("access_token")).thenReturn(false);
            when(jwtUtil.resolveToken(request, JwtUtil.REFRESH_KEY)).thenReturn(null);

            // When
            jwtAuthFilter.doFilterInternal(request, response, filterChain);

            // Then
            verify(jwtUtil).resolveToken(request, JwtUtil.REFRESH_KEY);
            verify(filterChain, never()).doFilter(request, response);
        }

        @Test
        @DisplayName("Valid Refresh Token")
        void validRefreshTokenTest() throws ServletException, IOException {
            // Given
            when(jwtUtil.resolveToken(request, JwtUtil.ACCESS_KEY)).thenReturn("access_token");
            when(jwtUtil.validateToken("access_token")).thenReturn(false);

            when(jwtUtil.resolveToken(request, JwtUtil.REFRESH_KEY)).thenReturn("refresh_token");
            when(jwtUtil.getMemberInfoFromToken("refresh_token")).thenReturn("test@gmail.com");
            when(jwtUtil.existsRefreshToken("test@gmail.com")).thenReturn(true);

            Members mockMember = mock(Members.class);

            when(membersRepository.findByEmail(ArgumentMatchers.anyString())).thenAnswer((Answer<Optional<Members>>) invocation -> {
                String email = invocation.getArgument(0);
                if ("test@gmail.com".equals(email)) {
                    return Optional.of(mockMember);
                } else {
                    return Optional.empty();
                }
            });

            when(jwtUtil.createToken(mockMember, "Access")).thenReturn("new_access_token");
            when(jwtUtil.getExpirationTime("refresh_token")).thenReturn(1000L);
            when(jwtUtil.createNewRefreshToken("test@gmail.com", 1000L)).thenReturn("new_refresh_token");

            // When
            jwtAuthFilter.doFilterInternal(request, response, filterChain);

            // Then
            verify(jwtUtil).resolveToken(request, JwtUtil.ACCESS_KEY);
            verify(jwtUtil).validateToken("access_token");
            verify(jwtUtil).resolveToken(request, JwtUtil.REFRESH_KEY);
            verify(membersRepository).findByEmail("test@gmail.com");
            verify(jwtUtil).createToken(mockMember, "Access");
            verify(jwtUtil).getExpirationTime("refresh_token");
            verify(jwtUtil).createNewRefreshToken("test@gmail.com", 1000L);
            verify(redisUtil).set("test@gmail.com", "new_refresh_token", 1000L);
            verify(jwtUtil).setHeaderToken(response, "new_access_token", "new_refresh_token");
            verify(filterChain).doFilter(request, response);
        }

        @Test
        @DisplayName("Email Not Found")
        void emailNotFoundTest() {
            // Given
            when(jwtUtil.resolveToken(any(), any())).thenReturn("access_token");
            when(jwtUtil.validateToken("access_token")).thenReturn(false);
            when(jwtUtil.resolveToken(request, JwtUtil.REFRESH_KEY)).thenReturn("refresh_token");
            when(jwtUtil.getMemberInfoFromToken("refresh_token")).thenReturn("unknown@gmail.com");
            when(jwtUtil.existsRefreshToken("unknown@gmail.com")).thenReturn(true);

            when(membersRepository.findByEmail(eq("unknown@gmail.com"))).thenReturn(Optional.empty());

            // When & Then
            assertThrows(UsernameNotFoundException.class, () -> {
                jwtAuthFilter.doFilterInternal(request, response, filterChain);
                jwtAuthFilter.jwtExceptionHandler(response, "해당 유저를 찾을 수 없습니다. 이메일 : unknown@gmail.com");
            });
        }

        @Test
        @DisplayName("Invalid Refresh Token")
        void invalidRefreshTokenTest() throws ServletException, IOException {
            // Given
            JwtAuthFilter jwtAuthFilterSpy = spy(jwtAuthFilter);

            when(jwtUtil.resolveToken(any(), any())).thenReturn("access_token");
            when(jwtUtil.validateToken("access_token")).thenReturn(false);
            when(jwtUtil.resolveToken(request, JwtUtil.REFRESH_KEY)).thenReturn("refresh_token");
            when(jwtUtil.getMemberInfoFromToken("refresh_token")).thenReturn("test@gmail.com");
            when(jwtUtil.existsRefreshToken("test@gmail.com")).thenReturn(false);

            // When
            jwtAuthFilterSpy.doFilterInternal(request, response, filterChain);

            // Then
            verify(jwtAuthFilterSpy).jwtExceptionHandler(response, "Invalid Refresh Token");
            verify(filterChain, never()).doFilter(request, response);
        }
    }

    @Test
    @DisplayName("jwtExceptionHandler Test")
    void jwtExceptionHandlerTest() throws Exception {
        // Given
        String errorMessage = "Test Error";
        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));

        // When
        jwtAuthFilter.jwtExceptionHandler(response, errorMessage);

        // Then
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("application/json");
    }
}
