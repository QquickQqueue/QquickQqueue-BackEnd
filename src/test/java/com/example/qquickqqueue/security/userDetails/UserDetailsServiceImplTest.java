package com.example.qquickqqueue.security.userDetails;

import com.example.qquickqqueue.domain.enumPackage.Gender;
import com.example.qquickqqueue.domain.members.entity.Members;
import com.example.qquickqqueue.domain.members.repository.MembersRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class UserDetailsServiceImplTest {
    @Mock
    private MembersRepository membersRepository;
    @InjectMocks
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Nested
    @DisplayName("loadUserByUsername Method Test")
    class LoadUserByUsername {
        @Test
        @DisplayName("loadUserByUsername Method Success Test")
        void loadUserByUsernameSuccessTest() {
            // given
            String email = "email@email.com";

            Members member = Members.builder()
                    .email(email)
                    .password("test1234")
                    .name("tester")
                    .gender(Gender.FEMALE)
                    .birth(LocalDate.now())
                    .phoneNumber("010-123-1234")
                    .build();

            UserDetailsImpl userDetailsImpl = new UserDetailsImpl(member, member.getEmail());

            when(membersRepository.findByEmail(email)).thenReturn(Optional.of(member));

            // when
            UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsServiceImpl.loadUserByUsername(email);

            // then
            assertEquals(userDetailsImpl.getMember(), userDetails.getMember());
        }

        @Test
        @DisplayName("loadUserByUsername Method Fail Test")
        void loadUserByUsernameFailTest() {
            // given
            String email = "email@email.com";

            when(membersRepository.findByEmail(email)).thenReturn(Optional.empty());

            // when
            UsernameNotFoundException usernameNotFoundException = assertThrows(UsernameNotFoundException.class,
                    () -> userDetailsServiceImpl.loadUserByUsername(email));

            // then
            assertEquals("사용자를 찾을 수 없습니다.", usernameNotFoundException.getMessage());
        }
    }
}