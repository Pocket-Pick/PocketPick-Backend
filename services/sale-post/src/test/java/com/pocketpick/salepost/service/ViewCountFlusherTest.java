package com.pocketpick.salepost.service;

import com.pocketpick.salepost.domain.service.SalePostUseCase;
import com.pocketpick.salepost.infrastructure.redis.ViewCountFlusher;
import com.pocketpick.salepost.infrastructure.redis.ViewCountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("ViewCountFlusher")
@ExtendWith(MockitoExtension.class)
class ViewCountFlusherTest {

    @Mock
    private ViewCountRepository viewCountRepository;

    @Mock
    private SalePostUseCase salePostUseCase;

    @InjectMocks
    private ViewCountFlusher viewCountFlusher;

    @Test
    @DisplayName("Redis에 조회수가 있으면 DB에 반영하고 Redis 키를 삭제한다")
    void flush_withPendingCounts_updatesDbAndDeletesRedisKeys() {
        // given
        given(viewCountRepository.getAllKeys()).willReturn(Set.of("sale-post:view:1", "sale-post:view:2"));
        given(viewCountRepository.extractSalePostId("sale-post:view:1")).willReturn(1L);
        given(viewCountRepository.extractSalePostId("sale-post:view:2")).willReturn(2L);
        given(viewCountRepository.getAndDelete(1L)).willReturn(5L);
        given(viewCountRepository.getAndDelete(2L)).willReturn(3L);

        // when
        viewCountFlusher.flush();

        // then
        then(salePostUseCase).should().applyViewCount(1L, 5);
        then(viewCountRepository).shouldHaveNoMoreInteractions();
        then(salePostUseCase).should().applyViewCount(2L, 3);
    }

    @Test
    @DisplayName("Redis에 조회수가 없으면 DB 업데이트를 하지 않는다")
    void flush_withNoKeys_doesNothing() {
        // given
        given(viewCountRepository.getAllKeys()).willReturn(Set.of());

        // when
        viewCountFlusher.flush();

        // then
        then(salePostUseCase).shouldHaveNoInteractions();
    }
}
