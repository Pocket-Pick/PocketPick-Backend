package com.pocketpick.user.support.fixture;

import com.pocketpick.user.domain.domain.User;
import com.pocketpick.user.domain.domain.UserProfile;

public class UserFixture {

    public static final Long ID = 1L;
    public static final String EMAIL = "test@pocketpick.com";
    public static final String RAW_PASSWORD = "password123";
    public static final String ENCODED_PASSWORD = "$2a$10$encodedpasswordhash";
    public static final String NICKNAME = "테스트유저";

    public static User user() {
        return User.create(EMAIL, RAW_PASSWORD, ENCODED_PASSWORD, new UserProfile(NICKNAME, null, null));
    }
}
