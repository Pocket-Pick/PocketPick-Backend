package com.pocketpick.salepost.service;

import com.pocketpick.salepost.domain.domain.SalePostImage;
import com.pocketpick.salepost.domain.dto.CreateSalePostRequest;
import com.pocketpick.salepost.domain.dto.SalePostResponse;
import com.pocketpick.salepost.domain.dto.UpdateSalePostRequest;
import com.pocketpick.salepost.domain.dto.UpdateSaleStatusRequest;
import com.pocketpick.salepost.domain.service.SalePostService;
import com.pocketpick.salepost.domain.domain.CardCondition;
import com.pocketpick.salepost.domain.domain.SalePost;
import com.pocketpick.salepost.domain.domain.SaleStatus;
import com.pocketpick.salepost.domain.domain.exception.ForbiddenException;
import com.pocketpick.salepost.domain.domain.exception.SalePostNotFoundException;
import com.pocketpick.salepost.infrastructure.repository.SalePostImageRepository;
import com.pocketpick.salepost.infrastructure.repository.SalePostRepository;
import com.pocketpick.salepost.infrastructure.s3.S3Uploader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

@DisplayName("SalePostService")
@ExtendWith(MockitoExtension.class)
class SalePostServiceTest {

    @Mock
    private SalePostRepository salePostRepository;

    @Mock
    private SalePostImageRepository salePostImageRepository;

    @Mock
    private S3Uploader s3Uploader;

    @InjectMocks
    private SalePostService salePostService;

    @Nested
    @DisplayName("판매글 작성")
    class Create {

        @Test
        @DisplayName("이미지 없이 작성하면 빈 imageUrls를 반환한다")
        void create_withoutImages_returnsEmptyImageUrls() {
            // given
            CreateSalePostRequest request = new CreateSalePostRequest(
                    1L, "카드 팝니다", "상태 좋아요", 10000, CardCondition.MINT, List.of()
            );
            SalePost savedPost = SalePost.builder()
                    .userId(1L).cardId(1L).title("카드 팝니다")
                    .description("상태 좋아요").price(10000)
                    .cardCondition(CardCondition.MINT)
                    .build();
            given(salePostRepository.save(any(SalePost.class))).willReturn(savedPost);
            given(salePostImageRepository.findBySalePostIdOrderBySortOrder(savedPost.getId())).willReturn(List.of());

            // when
            SalePostResponse response = salePostService.create(1L, request);

            // then
            assertThat(response.title()).isEqualTo("카드 팝니다");
            assertThat(response.imageUrls()).isEmpty();
            then(s3Uploader).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("이미지와 함께 작성하면 temp→posts 복사 후 imageUrls를 반환한다")
        void create_withImages_promotesToPostsAndReturnsUrls() {
            // given
            CreateSalePostRequest request = new CreateSalePostRequest(
                    1L, "카드 팝니다", "상태 좋아요", 10000, CardCondition.MINT,
                    List.of("images/temp/1/uuid1.jpg", "images/temp/1/uuid2.jpg")
            );
            SalePost savedPost = SalePost.builder()
                    .userId(1L).cardId(1L).title("카드 팝니다")
                    .description("상태 좋아요").price(10000)
                    .cardCondition(CardCondition.MINT)
                    .build();
            SalePostImage image1 = SalePostImage.of(savedPost.getId(), "images/posts/1/uuid1.jpg", 0);
            SalePostImage image2 = SalePostImage.of(savedPost.getId(), "images/posts/1/uuid2.jpg", 1);

            given(salePostRepository.save(any(SalePost.class))).willReturn(savedPost);
            given(s3Uploader.promoteToPostsPath("images/temp/1/uuid1.jpg")).willReturn("images/posts/1/uuid1.jpg");
            given(s3Uploader.promoteToPostsPath("images/temp/1/uuid2.jpg")).willReturn("images/posts/1/uuid2.jpg");
            given(salePostImageRepository.saveAll(anyList())).willReturn(List.of(image1, image2));
            given(salePostImageRepository.findBySalePostIdOrderBySortOrder(savedPost.getId()))
                    .willReturn(List.of(image1, image2));
            given(s3Uploader.buildImageUrl("images/posts/1/uuid1.jpg")).willReturn("https://s3/images/posts/1/uuid1.jpg");
            given(s3Uploader.buildImageUrl("images/posts/1/uuid2.jpg")).willReturn("https://s3/images/posts/1/uuid2.jpg");

            // when
            SalePostResponse response = salePostService.create(1L, request);

            // then
            assertThat(response.imageUrls()).hasSize(2);
            assertThat(response.imageUrls().get(0)).isEqualTo("https://s3/images/posts/1/uuid1.jpg");
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
        @DisplayName("본인이면 기존 이미지 삭제 후 새 이미지로 교체한다")
        void update_owner_replacesImagesAndReturnsResponse() {
            // given
            SalePost salePost = SalePost.builder()
                    .userId(1L).cardId(1L).title("제목").description("설명")
                    .price(5000).cardCondition(CardCondition.GOOD).build();
            // update(userId, id, request)에서 id=1L을 직접 사용; salePost.getId()=null(DB 저장 전)
            given(salePostRepository.findById(1L)).willReturn(Optional.of(salePost));

            UpdateSalePostRequest request = new UpdateSalePostRequest(
                    "수정 제목", "수정 설명", 8000, CardCondition.MINT,
                    List.of("images/temp/1/new.jpg")
            );
            // toResponse에서 salePost.getId()=null로 조회
            SalePostImage newImage = SalePostImage.of(salePost.getId(), "images/posts/1/new.jpg", 0);

            given(s3Uploader.promoteToPostsPath("images/temp/1/new.jpg")).willReturn("images/posts/1/new.jpg");
            given(salePostImageRepository.saveAll(anyList())).willReturn(List.of(newImage));
            // toResponse: salePost.getId()=null
            given(salePostImageRepository.findBySalePostIdOrderBySortOrder(salePost.getId()))
                    .willReturn(List.of(newImage));
            given(s3Uploader.buildImageUrl("images/posts/1/new.jpg")).willReturn("https://s3/images/posts/1/new.jpg");

            // when
            SalePostResponse response = salePostService.update(1L, 1L, request);

            // then
            assertThat(response.title()).isEqualTo("수정 제목");
            assertThat(response.price()).isEqualTo(8000);
            assertThat(response.imageUrls()).containsExactly("https://s3/images/posts/1/new.jpg");
            then(salePostImageRepository).should().deleteBySalePostId(1L); // update에서 id=1L 직접 사용
        }
    }

    @Nested
    @DisplayName("판매 상태 변경")
    class UpdateStatus {

        @Test
        @DisplayName("SOLD로 변경 시 S3 이미지를 삭제한다")
        void updateStatus_toSold_deletesS3Images() {
            // given
            SalePost salePost = SalePost.builder()
                    .userId(1L).cardId(1L).title("제목").description("설명")
                    .price(5000).cardCondition(CardCondition.GOOD).build();
            // updateStatus(userId, id, request)에서 id=1L 직접 사용, toResponse에서 salePost.getId()=null 사용
            SalePostImage image = SalePostImage.of(1L, "images/posts/1/uuid.jpg", 0);

            given(salePostRepository.findById(1L)).willReturn(Optional.of(salePost));
            // id=1L로 이미지 목록 조회 (updateStatus 내부)
            given(salePostImageRepository.findBySalePostIdOrderBySortOrder(1L))
                    .willReturn(List.of(image));
            // salePost.getId()=null로 toResponse 조회
            given(salePostImageRepository.findBySalePostIdOrderBySortOrder(salePost.getId()))
                    .willReturn(List.of());

            UpdateSaleStatusRequest request = new UpdateSaleStatusRequest(SaleStatus.SOLD);

            // when
            SalePostResponse response = salePostService.updateStatus(1L, 1L, request);

            // then
            assertThat(response.status()).isEqualTo(SaleStatus.SOLD);
            then(s3Uploader).should().deleteObject("images/posts/1/uuid.jpg");
            then(salePostImageRepository).should().deleteBySalePostId(1L);
        }

        @Test
        @DisplayName("본인이 아니면 ForbiddenException을 던진다")
        void updateStatus_notOwner_throwsForbiddenException() {
            // given
            SalePost salePost = SalePost.builder()
                    .userId(1L).cardId(1L).title("제목").description("설명")
                    .price(5000).cardCondition(CardCondition.GOOD).build();
            given(salePostRepository.findById(1L)).willReturn(Optional.of(salePost));

            UpdateSaleStatusRequest request = new UpdateSaleStatusRequest(SaleStatus.SOLD);

            // when & then
            assertThatThrownBy(() -> salePostService.updateStatus(2L, 1L, request))
                    .isInstanceOf(ForbiddenException.class);
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
        @DisplayName("본인이면 S3 이미지 삭제 후 판매글을 삭제한다")
        void delete_owner_deletesS3ImagesAndSalePost() {
            // given
            SalePost salePost = SalePost.builder()
                    .userId(1L).cardId(1L).title("제목").description("설명")
                    .price(5000).cardCondition(CardCondition.GOOD).build();
            // delete(userId, id)에서 id=1L 직접 사용
            SalePostImage image = SalePostImage.of(1L, "images/posts/1/uuid.jpg", 0);

            given(salePostRepository.findById(1L)).willReturn(Optional.of(salePost));
            given(salePostImageRepository.findBySalePostIdOrderBySortOrder(1L))
                    .willReturn(List.of(image));

            // when
            salePostService.delete(1L, 1L);

            // then
            then(s3Uploader).should().deleteObject("images/posts/1/uuid.jpg");
            then(salePostImageRepository).should().deleteBySalePostId(1L);
            then(salePostRepository).should().delete(salePost);
        }
    }
}
