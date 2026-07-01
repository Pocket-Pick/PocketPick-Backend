package com.pocketpick.user.domain.controller;

import com.pocketpick.user.domain.dto.RegisterRequest;
import com.pocketpick.user.domain.dto.UpdateProfileRequest;
import com.pocketpick.user.domain.dto.UserResponse;
import com.pocketpick.user.domain.service.UserUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserUseCase userUseCase;

    @PostMapping
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse response = userUseCase.register(request);
        URI location = URI.create("/users/" + response.id());
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userUseCase.getUser(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse> updateProfile(@PathVariable Long id, @Valid @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(userUseCase.updateProfile(id, request));
    }
}
