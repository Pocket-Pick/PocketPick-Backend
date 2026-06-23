package com.pocketpick.user.service;

import com.pocketpick.user.domain.service.UserService;
import com.pocketpick.user.domain.domain.exception.InvalidNicknameException;
import com.pocketpick.user.domain.domain.exception.UserNotFoundException;
import com.pocketpick.user.domain.dto.RegisterRequest;
import com.pocketpick.user.domain.dto.UpdateProfileRequest;
import com.pocketpick.user.domain.dto.UserResponse;
import com.pocketpick.user.domain.repository.OutboxEventRepository;
import com.pocketpick.user.domain.repository.UserRepository;
import com.pocketpick.user.infrastructure.auth.AuthServiceClient;
import com.pocketpick.user.support.fixture.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@DisplayName("UserService")
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OutboxEventRepository outboxEventRepository;

    @Mock
    private AuthServiceClient authServiceClient;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Nested
    @DisplayName("회원가입")
    class Register {

        @Test
        @DisplayName("정상 요청이면 사용자를 저장하고 응답을 반환한다")
        void register_validRequest_returnsUserResponse() {
            // given
            RegisterRequest request = new RegisterRequest(UserFixture.EMAIL, UserFixture.RAW_PASSWORD, UserFixture.NICKNAME);
            given(userRepository.save(any())).willReturn(UserFixture.user());
            given(outboxEventRepository.save(any())).willReturn(null);
            given(passwordEncoder.encode(anyString())).willReturn(UserFixture.ENCODED_PASSWORD);

            // when
            UserResponse response = userService.register(request);

            // then
            assertThat(response.nickname()).isEqualTo(UserFixture.NICKNAME);
        }

        @Test
        @DisplayName("닉네임이 2자 미만이면 InvalidNicknameException을 던진다")
        void register_shortNickname_throwsInvalidNicknameException() {
            // given
            RegisterRequest request = new RegisterRequest(UserFixture.EMAIL, UserFixture.RAW_PASSWORD, "a");

            // when & then
            assertThatThrownBy(() -> userService.register(request))
                    .isInstanceOf(InvalidNicknameException.class);
        }

        @Test
        @DisplayName("닉네임이 20자 초과이면 InvalidNicknameException을 던진다")
        void register_longNickname_throwsInvalidNicknameException() {
            // given
            RegisterRequest request = new RegisterRequest(UserFixture.EMAIL, UserFixture.RAW_PASSWORD, "a".repeat(21));

            // when & then
            assertThatThrownBy(() -> userService.register(request))
                    .isInstanceOf(InvalidNicknameException.class);
        }
    }

    @Nested
    @DisplayName("사용자 조회")
    class GetUser {

        @Test
        @DisplayName("존재하는 id면 사용자 응답을 반환한다")
        void getUser_existingId_returnsUserResponse() {
            // given
            given(userRepository.findById(UserFixture.ID)).willReturn(Optional.of(UserFixture.user()));

            // when
            UserResponse response = userService.getUser(UserFixture.ID);

            // then
            assertThat(response.nickname()).isEqualTo(UserFixture.NICKNAME);
        }

        @Test
        @DisplayName("존재하지 않는 id면 UserNotFoundException을 던진다")
        void getUser_notExistingId_throwsUserNotFoundException() {
            // given
            given(userRepository.findById(UserFixture.ID)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> userService.getUser(UserFixture.ID))
                    .isInstanceOf(UserNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("프로필 수정")
    class UpdateProfile {

        @Test
        @DisplayName("정상 요청이면 프로필을 수정하고 응답을 반환한다")
        void updateProfile_validRequest_returnsUpdatedUserResponse() {
            // given
            UpdateProfileRequest request = new UpdateProfileRequest("새닉네임", null, "서울");
            given(userRepository.findById(UserFixture.ID)).willReturn(Optional.of(UserFixture.user()));

            // when
            UserResponse response = userService.updateProfile(UserFixture.ID, request);

            // then
            assertThat(response.nickname()).isEqualTo("새닉네임");
            assertThat(response.region()).isEqualTo("서울");
        }

        @Test
        @DisplayName("닉네임이 2자 미만이면 InvalidNicknameException을 던진다")
        void updateProfile_shortNickname_throwsInvalidNicknameException() {
            // given
            UpdateProfileRequest request = new UpdateProfileRequest("a", null, null);
            given(userRepository.findById(UserFixture.ID)).willReturn(Optional.of(UserFixture.user()));

            // when & then
            assertThatThrownBy(() -> userService.updateProfile(UserFixture.ID, request))
                    .isInstanceOf(InvalidNicknameException.class);
        }

        @Test
        @DisplayName("존재하지 않는 id면 UserNotFoundException을 던진다")
        void updateProfile_notExistingId_throwsUserNotFoundException() {
            // given
            UpdateProfileRequest request = new UpdateProfileRequest("새닉네임", null, null);
            given(userRepository.findById(UserFixture.ID)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> userService.updateProfile(UserFixture.ID, request))
                    .isInstanceOf(UserNotFoundException.class);
        }
    }
}
