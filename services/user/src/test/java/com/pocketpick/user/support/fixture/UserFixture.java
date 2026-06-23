package com.pocketpick.user.support.fixture;

import com.pocketpick.user.domain.domain.User;
import com.pocketpick.user.domain.domain.UserProfile;
import org.springframework.test.util.ReflectionTestUtils;

public class UserFixture {

    public static final Long ID = 1L;
    public static final String EMAIL = "test@pocketpick.com";
    public static final String RAW_PASSWORD = "password123";
    public static final String NICKNAME = "테스트유저";

    public static User user() {
        User user = User.create(new UserProfile(NICKNAME, null, null));
        ReflectionTestUtils.setField(user, "id", ID);
        return user;
    }
}
