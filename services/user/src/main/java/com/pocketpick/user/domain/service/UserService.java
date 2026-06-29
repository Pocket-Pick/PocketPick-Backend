package com.pocketpick.user.domain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pocketpick.user.domain.domain.OutboxEvent;
import com.pocketpick.user.domain.domain.User;
import com.pocketpick.user.domain.domain.UserProfile;
import com.pocketpick.user.domain.domain.exception.UserNotFoundException;
import com.pocketpick.user.domain.dto.RegisterRequest;
import com.pocketpick.user.domain.dto.UserResponse;
import com.pocketpick.user.domain.repository.OutboxEventRepository;
import com.pocketpick.user.domain.repository.UserRepository;
import com.pocketpick.user.infrastructure.auth.AuthServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService implements UserUseCase {

    private final UserRepository userRepository;
    private final OutboxEventRepository outboxEventRepository;
    private final AuthServiceClient authServiceClient;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public UserResponse register(RegisterRequest request) {
        authServiceClient.validate(request.email(), request.password());

        User user = User.create(
                new UserProfile(request.nickname(), null, null)
        );

        User savedUser = userRepository.save(user);
        String encodedPassword = passwordEncoder.encode(request.password());
        outboxEventRepository.save(OutboxEvent.create(
                "CREDENTIALS_CREATED",
                toJson(Map.of(
                        "userId", savedUser.getId(),
                        "email", request.email(),
                        "encodedPassword", encodedPassword
                ))
        ));

        return UserResponse.from(savedUser);
    }

    private String toJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("OutboxEvent payload 직렬화 실패", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        return UserResponse.from(user);
    }
}
