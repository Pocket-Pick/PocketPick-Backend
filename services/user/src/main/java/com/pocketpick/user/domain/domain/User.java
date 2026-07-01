package com.pocketpick.user.domain.domain;

import com.pocketpick.user.global.BaseEntity;
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

    @Embedded
    private UserProfile profile;

    @Column(nullable = false)
    private boolean notificationEnabled = true;

    public static User create(UserProfile profile) {
        User user = new User();
        user.profile = profile;
        return user;
    }

    public void updateProfile(UserProfile profile) {
        this.profile = profile;
    }
}
