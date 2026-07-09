package com.pocketpick.chat.infrastructure.redis;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@DisplayName("OnlineStatusRepository")
@ExtendWith(MockitoExtension.class)
class OnlineStatusRepositoryTest {

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private OnlineStatusRepository onlineStatusRepository;

    @Nested
    @DisplayName("온라인 상태 관리")
    class OnlineStatus {

        @Test
        @DisplayName("온라인으로 표시하면 Redis에 저장된다")
        void markOnline_savesToRedis() {
            // given
            given(redisTemplate.opsForValue()).willReturn(valueOperations);

            // when
            onlineStatusRepository.markOnline(1L);

            // then
            verify(valueOperations).set("user:online:1", "1");
        }

        @Test
        @DisplayName("오프라인으로 표시하면 Redis에서 삭제된다")
        void markOffline_deletesFromRedis() {
            // when
            onlineStatusRepository.markOffline(1L);

            // then
            verify(redisTemplate).delete("user:online:1");
        }

        @Test
        @DisplayName("키가 존재하면 온라인 상태를 반환한다")
        void isOnline_keyExists_returnsTrue() {
            // given
            given(redisTemplate.hasKey("user:online:1")).willReturn(true);

            // when
            boolean result = onlineStatusRepository.isOnline(1L);

            // then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("키가 없으면 오프라인 상태를 반환한다")
        void isOnline_keyNotExists_returnsFalse() {
            // given
            given(redisTemplate.hasKey("user:online:1")).willReturn(false);

            // when
            boolean result = onlineStatusRepository.isOnline(1L);

            // then
            assertThat(result).isFalse();
        }
    }
}
