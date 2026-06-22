package com.pocketpick.user.domain.service;

import com.pocketpick.user.domain.domain.User;
import com.pocketpick.user.domain.domain.UserProfile;
import com.pocketpick.user.domain.domain.exception.DuplicateEmailException;
import com.pocketpick.user.domain.domain.exception.UserNotFoundException;
import com.pocketpick.user.domain.dto.RegisterRequest;
import com.pocketpick.user.domain.dto.UserResponse;
import com.pocketpick.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService implements UserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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

        return UserResponse.from(userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        return UserResponse.from(user);
    }
}
