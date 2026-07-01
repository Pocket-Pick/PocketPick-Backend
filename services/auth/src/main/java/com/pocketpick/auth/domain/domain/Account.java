package com.pocketpick.auth.domain.domain;

import com.pocketpick.auth.domain.domain.exception.InvalidPasswordException;
import com.pocketpick.auth.global.BaseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Entity
@Table(name = "accounts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Long userId;

    public static Account create(String email, String encodedPassword, Long userId) {
        Account account = new Account();
        account.email = email;
        account.password = encodedPassword;
        account.userId = userId;
        return account;
    }

    public void checkPassword(String rawPassword, PasswordEncoder encoder) {
        if (!encoder.matches(rawPassword, this.password)) {
            throw new InvalidPasswordException();
        }
    }
}
