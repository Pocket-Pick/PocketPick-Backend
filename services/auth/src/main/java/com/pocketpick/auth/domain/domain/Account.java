package com.pocketpick.auth.domain.domain;

import com.pocketpick.auth.domain.domain.exception.WeakPasswordException;
import com.pocketpick.auth.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "accounts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseEntity {

    private static final int MIN_PASSWORD_LENGTH = 8;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Long userId;

    public static Account create(String email, String rawPassword, String encodedPassword, Long userId) {
        validatePassword(rawPassword);
        Account account = new Account();
        account.email = email;
        account.password = encodedPassword;
        account.userId = userId;
        return account;
    }

    private static void validatePassword(String rawPassword) {
        if (rawPassword == null || rawPassword.length() < MIN_PASSWORD_LENGTH) {
            throw new WeakPasswordException();
        }
    }
}
