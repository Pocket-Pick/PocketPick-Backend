package com.pocketpick.auth.support.fixture;

import com.pocketpick.auth.domain.domain.Account;

public class AccountFixture {

    public static final Long USER_ID = 1L;
    public static final String EMAIL = "test@example.com";
    public static final String RAW_PASSWORD = "password123";
    public static final String ENCODED_PASSWORD = "$2a$10$encodedPasswordForTest";

    public static Account account() {
        return Account.create(EMAIL, RAW_PASSWORD, ENCODED_PASSWORD, USER_ID);
    }
}
