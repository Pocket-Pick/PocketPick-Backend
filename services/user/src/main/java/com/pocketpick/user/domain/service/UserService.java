package com.pocketpick.user.domain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pocketpick.user.domain.domain.OutboxEvent;
import com.pocketpick.user.domain.domain.User;
import com.pocketpick.user.domain.domain.UserProfile;
import com.pocketpick.user.domain.domain.exception.DuplicateEmailException;
import com.pocketpick.user.domain.domain.exception.UserNotFoundException;
import com.pocketpick.user.domain.dto.RegisterRequest;
import com.pocketpick.user.domain.dto.UserResponse;
import com.pocketpick.user.domain.repository.OutboxEventRepository;
import com.pocketpick.user.domain.repository.UserRepository;
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
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateEmailException();
        }

        User user = User.create(
                request.email(),
                request.password(),
                passwordEncoder.encode(request.password()),
                new UserProfile(request.nickname(), null, null)
        );

        User savedUser = userRepository.save(user);
        outboxEventRepository.save(OutboxEvent.create(
                "CREDENTIALS_CREATED",
                toJson(Map.of(
                        "userId", savedUser.getId(),
                        "email", savedUser.getEmail(),
                        "password", request.password()
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
