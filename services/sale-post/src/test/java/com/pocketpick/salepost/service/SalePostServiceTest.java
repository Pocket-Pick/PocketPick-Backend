package com.pocketpick.salepost.service;

import com.pocketpick.salepost.domain.domain.CardCondition;
import com.pocketpick.salepost.domain.domain.SalePost;
import com.pocketpick.salepost.domain.domain.SaleStatus;
import com.pocketpick.salepost.domain.domain.exception.ForbiddenException;
import com.pocketpick.salepost.domain.domain.exception.ReservedPostException;
import com.pocketpick.salepost.domain.domain.exception.SalePostNotFoundException;
import com.pocketpick.salepost.domain.dto.CreateSalePostRequest;
import com.pocketpick.salepost.domain.dto.SalePostItemRequest;
import com.pocketpick.salepost.domain.dto.SalePostResponse;
import com.pocketpick.salepost.domain.dto.UpdateSalePostRequest;
import com.pocketpick.salepost.domain.service.SalePostService;
import com.pocketpick.salepost.infrastructure.repository.SalePostImageRepository;
import com.pocketpick.salepost.infrastructure.repository.SalePostItemRepository;
import com.pocketpick.salepost.infrastructure.repository.SalePostRepository;
import com.pocketpick.salepost.infrastructure.s3.S3Uploader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("SalePostService")
@ExtendWith(MockitoExtension.class)
class SalePostServiceTest {

    @Mock
    private SalePostRepository salePostRepository;

    @Mock
    private SalePostImageRepository salePostImageRepository;

    @Mock
    private SalePostItemRepository salePostItemRepository;

    @Mock
    private S3Uploader s3Uploader;

    @InjectMocks
    private SalePostService salePostService;

    private static SalePost buildSalePost(Long userId) {
        return SalePost.builder()
                .userId(userId)
                .title("카드 팝니다")
                .description("상태 좋아요")
                .price(10000)
                .build();
    }

    private static List<SalePostItemRequest> singleItem() {
        return List.of(new SalePostItemRequest(1L, CardCondition.MINT, 1));
    }

    @Nested
    @DisplayName("판매글 작성")
    class Create {

        @Test
        @DisplayName("정상 요청이면 저장 후 응답을 반환한다")
        void create_validRequest_returnsResponse() {
            // given
            CreateSalePostRequest request = new CreateSalePostRequest(
                    "카드 팝니다", "상태 좋아요", 10000, singleItem(), List.of("images/temp/1/uuid.jpg")
            );
            SalePost saved = buildSalePost(1L);
            given(salePostRepository.save(any(SalePost.class))).willReturn(saved);
            given(salePostItemRepository.findBySalePostId(saved.getId())).willReturn(List.of());
            given(salePostImageRepository.findBySalePostIdOrderBySortOrder(saved.getId())).willReturn(List.of());

            // when
            SalePostResponse response = salePostService.create(1L, request);

            // then
            assertThat(response.title()).isEqualTo("카드 팝니다");
            assertThat(response.items()).isEmpty();
            assertThat(response.imageUrls()).isEmpty();
        }
    }

    @Nested
    @DisplayName("판매글 상세 조회")
    class GetOne {

        @Test
        @DisplayName("존재하지 않는 ID면 SalePostNotFoundException을 던진다")
        void getOne_notFound_throwsException() {
            // given
            given(salePostRepository.findById(999L)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> salePostService.getOne(999L))
                    .isInstanceOf(SalePostNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("판매글 수정")
    class Update {

        @Test
        @DisplayName("본인이 아니면 ForbiddenException을 던진다")
        void update_notOwner_throwsForbiddenException() {
            // given
            SalePost salePost = buildSalePost(1L);
            given(salePostRepository.findById(1L)).willReturn(Optional.of(salePost));

            UpdateSalePostRequest request = new UpdateSalePostRequest(
                    "수정 제목", "수정 설명", 6000, singleItem(), null
            );

            // when & then
            assertThatThrownBy(() -> salePostService.update(2L, 1L, request))
                    .isInstanceOf(ForbiddenException.class);
        }

        @Test
        @DisplayName("RESERVED 상태이면 ReservedPostException을 던진다")
        void update_reservedStatus_throwsReservedPostException() {
            // given
            SalePost salePost = buildSalePost(1L);
            salePost.updateStatus(SaleStatus.RESERVED);
            given(salePostRepository.findById(1L)).willReturn(Optional.of(salePost));

            UpdateSalePostRequest request = new UpdateSalePostRequest(
                    "수정 제목", "수정 설명", 6000, singleItem(), null
            );

            // when & then
            assertThatThrownBy(() -> salePostService.update(1L, 1L, request))
                    .isInstanceOf(ReservedPostException.class);
        }

        @Test
        @DisplayName("본인이면 판매글을 수정하고 응답을 반환한다")
        void update_owner_updatesAndReturnsResponse() {
            // given
            SalePost salePost = buildSalePost(1L);
            given(salePostRepository.findById(1L)).willReturn(Optional.of(salePost));
            given(salePostItemRepository.findBySalePostId(salePost.getId())).willReturn(List.of());
            given(salePostImageRepository.findBySalePostIdOrderBySortOrder(salePost.getId())).willReturn(List.of());

            UpdateSalePostRequest request = new UpdateSalePostRequest(
                    "수정 제목", "수정 설명", 8000, singleItem(), null
            );

            // when
            SalePostResponse response = salePostService.update(1L, 1L, request);

            // then
            assertThat(response.title()).isEqualTo("수정 제목");
            assertThat(response.price()).isEqualTo(8000);
        }
    }

    @Nested
    @DisplayName("판매글 삭제")
    class Delete {

        @BeforeEach
        void initTransactionSynchronization() {
            TransactionSynchronizationManager.initSynchronization();
        }

        @AfterEach
        void clearTransactionSynchronization() {
            TransactionSynchronizationManager.clearSynchronization();
        }

        @Test
        @DisplayName("본인이 아니면 ForbiddenException을 던진다")
        void delete_notOwner_throwsForbiddenException() {
            // given
            SalePost salePost = buildSalePost(1L);
            given(salePostRepository.findById(1L)).willReturn(Optional.of(salePost));

            // when & then
            assertThatThrownBy(() -> salePostService.delete(2L, 1L))
                    .isInstanceOf(ForbiddenException.class);
        }

        @Test
        @DisplayName("RESERVED 상태이면 ReservedPostException을 던진다")
        void delete_reservedStatus_throwsReservedPostException() {
            // given
            SalePost salePost = buildSalePost(1L);
            salePost.updateStatus(SaleStatus.RESERVED);
            given(salePostRepository.findById(1L)).willReturn(Optional.of(salePost));

            // when & then
            assertThatThrownBy(() -> salePostService.delete(1L, 1L))
                    .isInstanceOf(ReservedPostException.class);
        }

        @Test
        @DisplayName("본인이면 판매글을 삭제한다")
        void delete_owner_deletesSalePost() {
            // given
            SalePost salePost = buildSalePost(1L);
            given(salePostRepository.findById(1L)).willReturn(Optional.of(salePost));
            given(salePostImageRepository.findBySalePostIdOrderBySortOrder(1L)).willReturn(List.of());

            // when
            salePostService.delete(1L, 1L);

            // then
            then(salePostRepository).should().delete(salePost);
        }
    }
}
