package com.example.qquickqqueue.redis.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class RedisUtilTest {
    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private RedisUtil redisUtil;

    private String key = "key";
    private String val = "val";
    private long time = 30L;

    @Nested
    @DisplayName("set Method Test")
    class Set {
        @Test
        void set() {
            // given
            when(redisTemplate.opsForValue()).thenReturn(valueOperations);

            // when
            redisUtil.set(key, val, time);

            // then
            verify(redisTemplate, times(1)).setValueSerializer(any(Jackson2JsonRedisSerializer.class));
            verify(valueOperations, times(1)).set(eq(key), eq(val), eq(time), eq(TimeUnit.MINUTES));
        }
    }


    @Nested
    @DisplayName("get Method Test")
    class Get {
        @Test
        void get() {
            // given
            when(redisTemplate.opsForValue()).thenReturn(valueOperations);
            when(valueOperations.get(any())).thenReturn(val);

            // when
            String result = redisUtil.get(key);

            // then
            assertEquals(val, result);
        }
    }

    @Nested
    @DisplayName("delValues Method Test")
    class DelValues {
        @Test
        void delValues() {
            // given

            // when
            redisUtil.delValues(key);

            // then
            verify(redisTemplate, times(1)).delete(eq(key));
        }
    }

    @Nested
    @DisplayName("setBlackList Method Test")
    class SetBlackList {
        @Test
        void setBlackList() {
            // given
            given(redisTemplate.opsForValue()).willReturn(valueOperations);

            // when
            redisUtil.setBlackList(key, val, time);

            // then
            verify(redisTemplate, times(1)).setValueSerializer(any(Jackson2JsonRedisSerializer.class));
            verify(valueOperations, times(1)).set(eq(key), eq(val), eq(time), eq(TimeUnit.MINUTES));
        }
    }

    @Nested
    @DisplayName("hasKeyBlackList Method Test")
    class HasKeyBlackList {
        @Test
        void hasKeyBlackList_true() {
            // given
//            String key = "hasKey";
            when(redisTemplate.hasKey(any())).thenReturn(true);

            // when
            boolean result = redisUtil.hasKeyBlackList(key);

            // then
            assertTrue(result);
        }

        @Test
        void hasKeyBlackList_false() {
            // given
            given(redisTemplate.hasKey(key)).willReturn(false);

            // when
            boolean result = redisUtil.hasKeyBlackList(key);

            // then
            assertFalse(result);
        }
    }
}