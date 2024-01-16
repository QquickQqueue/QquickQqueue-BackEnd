package com.example.qquickqqueue.security.userDetails;

import com.example.qquickqqueue.domain.members.entity.Members;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({MockitoExtension.class})
class UserDetailsImplTest {

    // given
    private Members testMember = Members.builder().build();
    private String testEmail = "email@email.com";
    private UserDetailsImpl userDetails = new UserDetailsImpl(testMember, testEmail);
    @Test
    @DisplayName("getMember Method Test")
    void getMember() {
        // when
        Members member = userDetails.getMember();

        // then
        assertEquals(member, testMember);
    }

    @Test
    @DisplayName("getEmail Method Test")
    void getEmail() {
        // when
        String email = userDetails.getEmail();

        // then
        assertEquals(email, testEmail);
    }

    @Test
    @DisplayName("getAuthorities Method Test")
    void getAuthorities() {
        // when & then
        assertNull(userDetails.getAuthorities());
    }

    @Test
    @DisplayName("getPassword Method Test")
    void getPassword() {
        // when & then
        assertNull(userDetails.getPassword());
    }

    @Test
    @DisplayName("getUsername Method Test")
    void getUsername() {
        // when & then
        assertNull(userDetails.getUsername());
    }

    @Test
    @DisplayName("isAccountNonExpired Method Test")
    void isAccountNonExpired() {
        // when & then
        assertFalse(userDetails.isAccountNonExpired());
    }

    @Test
    @DisplayName("isAccountNonLocked Method Test")
    void isAccountNonLocked() {
        // when & then
        assertFalse(userDetails.isAccountNonLocked());
    }

    @Test
    @DisplayName("isCredentialsNonExpired Method Test")
    void isCredentialsNonExpired() {
        // when & then
        assertFalse(userDetails.isCredentialsNonExpired());
    }

    @Test
    @DisplayName("isEnabled Method Test")
    void isEnabled() {
        // when & then
        assertFalse(userDetails.isEnabled());
    }
}