package com.pocketpick.user.controller;

import tools.jackson.databind.ObjectMapper;
import com.pocketpick.user.domain.controller.UserController;
import com.pocketpick.user.domain.domain.exception.UserNotFoundException;
import com.pocketpick.user.domain.dto.RegisterRequest;
import com.pocketpick.user.domain.dto.UpdateNotificationRequest;
import com.pocketpick.user.domain.dto.UpdateProfileRequest;
import com.pocketpick.user.domain.dto.UserResponse;
import com.pocketpick.user.global.exception.GlobalExceptionHandler;
import com.pocketpick.user.domain.service.UserUseCase;
import com.pocketpick.user.support.fixture.UserFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("UserController")
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private UserUseCase userUseCase;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Nested
    @DisplayName("POST /users")
    class Register {

        @Test
        @DisplayName("정상 요청이면 201을 반환한다")
        void register_validRequest_returns201() throws Exception {
            // given
            RegisterRequest request = new RegisterRequest(UserFixture.EMAIL, "password123", UserFixture.NICKNAME);
            UserResponse response = new UserResponse(UserFixture.ID, UserFixture.NICKNAME, null, null, true);
            given(userUseCase.register(any())).willReturn(response);

            // when & then
            mockMvc.perform(post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.nickname").value(UserFixture.NICKNAME));
        }

        @Test
        @DisplayName("이메일이 비어있으면 400을 반환한다")
        void register_blankEmail_returns400() throws Exception {
            // given
            String invalidBody = "{\"email\": \"\", \"password\": \"password123\", \"nickname\": \"닉네임\"}";

            // when & then
            mockMvc.perform(post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(invalidBody))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorCode").value("INVALID_INPUT"));
        }
    }

    @Nested
    @DisplayName("GET /users/{id}")
    class GetUser {

        @Test
        @DisplayName("존재하는 id면 200을 반환한다")
        void getUser_existingId_returns200() throws Exception {
            // given
            UserResponse response = new UserResponse(UserFixture.ID, UserFixture.NICKNAME, null, null, true);
            given(userUseCase.getUser(UserFixture.ID)).willReturn(response);

            // when & then
            mockMvc.perform(get("/users/{id}", UserFixture.ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(UserFixture.ID))
                    .andExpect(jsonPath("$.nickname").value(UserFixture.NICKNAME));
        }

        @Test
        @DisplayName("존재하지 않는 id면 404를 반환한다")
        void getUser_notExistingId_returns404() throws Exception {
            // given
            given(userUseCase.getUser(UserFixture.ID)).willThrow(new UserNotFoundException());

            // when & then
            mockMvc.perform(get("/users/{id}", UserFixture.ID))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.errorCode").value("USER_NOT_FOUND"));
        }
    }

    @Nested
    @DisplayName("PATCH /users/{id}")
    class UpdateProfile {

        @Test
        @DisplayName("정상 요청이면 200을 반환한다")
        void updateProfile_validRequest_returns200() throws Exception {
            // given
            UpdateProfileRequest request = new UpdateProfileRequest("새닉네임", null, "서울");
            UserResponse response = new UserResponse(UserFixture.ID, "새닉네임", null, "서울", true);
            given(userUseCase.updateProfile(eq(UserFixture.ID), any())).willReturn(response);

            // when & then
            mockMvc.perform(patch("/users/{id}", UserFixture.ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nickname").value("새닉네임"))
                    .andExpect(jsonPath("$.region").value("서울"));
        }

        @Test
        @DisplayName("닉네임이 비어있으면 400을 반환한다")
        void updateProfile_blankNickname_returns400() throws Exception {
            // given
            String invalidBody = "{\"nickname\": \"\", \"profileImageUrl\": null, \"region\": null}";

            // when & then
            mockMvc.perform(patch("/users/{id}", UserFixture.ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(invalidBody))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorCode").value("INVALID_INPUT"));
        }

        @Test
        @DisplayName("존재하지 않는 id면 404를 반환한다")
        void updateProfile_notExistingId_returns404() throws Exception {
            // given
            UpdateProfileRequest request = new UpdateProfileRequest("새닉네임", null, null);
            given(userUseCase.updateProfile(eq(UserFixture.ID), any())).willThrow(new UserNotFoundException());

            // when & then
            mockMvc.perform(patch("/users/{id}", UserFixture.ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.errorCode").value("USER_NOT_FOUND"));
        }
    }

    @Nested
    @DisplayName("PATCH /users/{id}/notification")
    class UpdateNotification {

        @Test
        @DisplayName("정상 요청이면 200을 반환한다")
        void updateNotification_validRequest_returns200() throws Exception {
            // given
            UpdateNotificationRequest request = new UpdateNotificationRequest(false);
            UserResponse response = new UserResponse(UserFixture.ID, UserFixture.NICKNAME, null, null, false);
            given(userUseCase.updateNotification(eq(UserFixture.ID), any())).willReturn(response);

            // when & then
            mockMvc.perform(patch("/users/{id}/notification", UserFixture.ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.notificationEnabled").value(false));
        }

        @Test
        @DisplayName("notificationEnabled가 null이면 400을 반환한다")
        void updateNotification_nullValue_returns400() throws Exception {
            // given
            String invalidBody = "{\"notificationEnabled\": null}";

            // when & then
            mockMvc.perform(patch("/users/{id}/notification", UserFixture.ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(invalidBody))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorCode").value("INVALID_INPUT"));
        }

        @Test
        @DisplayName("존재하지 않는 id면 404를 반환한다")
        void updateNotification_notExistingId_returns404() throws Exception {
            // given
            UpdateNotificationRequest request = new UpdateNotificationRequest(false);
            given(userUseCase.updateNotification(eq(UserFixture.ID), any())).willThrow(new UserNotFoundException());

            // when & then
            mockMvc.perform(patch("/users/{id}/notification", UserFixture.ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.errorCode").value("USER_NOT_FOUND"));
        }
    }
}
