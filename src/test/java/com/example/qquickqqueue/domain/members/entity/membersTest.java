package com.example.qquickqqueue.domain.members.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith({MockitoExtension.class})
public class membersTest {
    @Test
    @DisplayName("setIsKakaoEmail to True")
    void setIsKakaoEmailTrue() {
        // Given
        Members member = Members.builder()
                .isKakaoEmail(false)
                .build();

        // When
        member.setIsKakaoEmail();

        // Then
        assertTrue(member.isKakaoEmail());
    }

    @Test
    @DisplayName("setIsKakaoEmail to false")
    void setIsKakaoEmailfalse() {
        // Given
        Members member = Members.builder()
                .isKakaoEmail(true)
                .build();

        // When
        member.setIsKakaoEmail();

        // Then
        assertFalse(member.isKakaoEmail());
    }
}
