package com.pocketpick.salepost.service;

import com.pocketpick.salepost.application.dto.CreateSalePostRequest;
import com.pocketpick.salepost.application.dto.SalePostResponse;
import com.pocketpick.salepost.application.dto.UpdateSalePostRequest;
import com.pocketpick.salepost.application.service.SalePostService;
import com.pocketpick.salepost.domain.entity.CardCondition;
import com.pocketpick.salepost.domain.entity.SalePost;
import com.pocketpick.salepost.domain.entity.SaleStatus;
import com.pocketpick.salepost.domain.exception.ForbiddenException;
import com.pocketpick.salepost.domain.exception.SalePostNotFoundException;
import com.pocketpick.salepost.infrastructure.repository.SalePostRepository;
import com.pocketpick.salepost.infrastructure.s3.S3Uploader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("SalePostService")
@ExtendWith(MockitoExtension.class)
class SalePostServiceTest {

    @Mock
    private SalePostRepository salePostRepository;

    @Mock
    private S3Uploader s3Uploader;

    @InjectMocks
    private SalePostService salePostService;

    @Nested
    @DisplayName("판매글 작성")
    class Create {

        @Test
        @DisplayName("정상 요청이면 저장 후 응답을 반환한다")
        void create_validRequest_returnsResponse() {
            // given
            CreateSalePostRequest request = new CreateSalePostRequest(
                    1L, "카드 팝니다", "상태 좋아요", 10000, CardCondition.MINT, "images/1/uuid.jpg"
            );
            SalePost savedPost = SalePost.builder()
                    .userId(1L).cardId(1L).title("카드 팝니다")
                    .description("상태 좋아요").price(10000)
                    .cardCondition(CardCondition.MINT).imageObjectKey("images/1/uuid.jpg")
                    .build();
            given(salePostRepository.save(any(SalePost.class))).willReturn(savedPost);
            given(s3Uploader.buildImageUrl("images/1/uuid.jpg")).willReturn("https://bucket.s3.amazonaws.com/images/1/uuid.jpg");

            // when
            SalePostResponse response = salePostService.create(1L, request);

            // then
            assertThat(response.title()).isEqualTo("카드 팝니다");
            assertThat(response.imageUrl()).isEqualTo("https://bucket.s3.amazonaws.com/images/1/uuid.jpg");
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
            SalePost salePost = SalePost.builder()
                    .userId(1L).cardId(1L).title("제목").description("설명")
                    .price(5000).cardCondition(CardCondition.GOOD).build();
            given(salePostRepository.findById(1L)).willReturn(Optional.of(salePost));

            UpdateSalePostRequest request = new UpdateSalePostRequest(
                    "수정 제목", "수정 설명", 6000, CardCondition.MINT, null
            );

            // when & then
            assertThatThrownBy(() -> salePostService.update(2L, 1L, request))
                    .isInstanceOf(ForbiddenException.class);
        }

        @Test
        @DisplayName("본인이면 판매글을 수정하고 응답을 반환한다")
        void update_owner_updatesAndReturnsResponse() {
            // given
            SalePost salePost = SalePost.builder()
                    .userId(1L).cardId(1L).title("제목").description("설명")
                    .price(5000).cardCondition(CardCondition.GOOD).build();
            given(salePostRepository.findById(1L)).willReturn(Optional.of(salePost));

            UpdateSalePostRequest request = new UpdateSalePostRequest(
                    "수정 제목", "수정 설명", 8000, CardCondition.MINT, null
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

        @Test
        @DisplayName("본인이 아니면 ForbiddenException을 던진다")
        void delete_notOwner_throwsForbiddenException() {
            // given
            SalePost salePost = SalePost.builder()
                    .userId(1L).cardId(1L).title("제목").description("설명")
                    .price(5000).cardCondition(CardCondition.GOOD).build();
            given(salePostRepository.findById(1L)).willReturn(Optional.of(salePost));

            // when & then
            assertThatThrownBy(() -> salePostService.delete(2L, 1L))
                    .isInstanceOf(ForbiddenException.class);
        }

        @Test
        @DisplayName("본인이면 판매글을 삭제한다")
        void delete_owner_deletesSalePost() {
            // given
            SalePost salePost = SalePost.builder()
                    .userId(1L).cardId(1L).title("제목").description("설명")
                    .price(5000).cardCondition(CardCondition.GOOD).build();
            given(salePostRepository.findById(1L)).willReturn(Optional.of(salePost));

            // when
            salePostService.delete(1L, 1L);

            // then
            then(salePostRepository).should().delete(salePost);
        }
    }
}
