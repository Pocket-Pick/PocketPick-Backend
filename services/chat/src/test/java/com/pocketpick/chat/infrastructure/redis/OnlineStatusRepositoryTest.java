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

import java.util.concurrent.TimeUnit;

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
        @DisplayName("온라인으로 표시하면 서버 IP를 Redis에 TTL 30분으로 저장한다")
        void markOnline_savesServerIpToRedis() {
            // given
            given(redisTemplate.opsForValue()).willReturn(valueOperations);

            // when
            onlineStatusRepository.markOnline(1L, "192.168.0.1");

            // then
            verify(valueOperations).set("user:server:1", "192.168.0.1", 30L, TimeUnit.MINUTES);
        }

        @Test
        @DisplayName("오프라인으로 표시하면 Redis에서 삭제된다")
        void markOffline_deletesFromRedis() {
            // when
            onlineStatusRepository.markOffline(1L);

            // then
            verify(redisTemplate).delete("user:server:1");
        }

        @Test
        @DisplayName("키가 존재하면 온라인 상태를 반환한다")
        void isOnline_keyExists_returnsTrue() {
            // given
            given(redisTemplate.hasKey("user:server:1")).willReturn(true);

            // when
            boolean result = onlineStatusRepository.isOnline(1L);

            // then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("키가 없으면 오프라인 상태를 반환한다")
        void isOnline_keyNotExists_returnsFalse() {
            // given
            given(redisTemplate.hasKey("user:server:1")).willReturn(false);

            // when
            boolean result = onlineStatusRepository.isOnline(1L);

            // then
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("서버 IP 조회 시 저장된 값을 반환한다")
        void getServerIp_returnsStoredIp() {
            // given
            given(redisTemplate.opsForValue()).willReturn(valueOperations);
            given(valueOperations.get("user:server:1")).willReturn("10.0.0.1");

            // when
            String result = onlineStatusRepository.getServerIp(1L);

            // then
            assertThat(result).isEqualTo("10.0.0.1");
        }
    }
}
