package com.pocketpick.user.domain.domain;

import com.pocketpick.user.global.BaseEntity;
import com.pocketpick.user.domain.domain.constants.UserPolicyConstants;
import com.pocketpick.user.domain.domain.exception.InvalidPasswordException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Embedded
    private UserProfile profile;

    @Column(nullable = false)
    private boolean notificationEnabled = true;

    public static User create(String email, String rawPassword, String encodedPassword, UserProfile profile) {
        validatePassword(rawPassword);
        User user = new User();
        user.email = email;
        user.password = encodedPassword;
        user.profile = profile;
        return user;
    }

    private static void validatePassword(String rawPassword) {
        if (rawPassword == null || rawPassword.length() < UserPolicyConstants.MIN_PASSWORD_LENGTH) {
            throw new InvalidPasswordException();
        }
    }
}
