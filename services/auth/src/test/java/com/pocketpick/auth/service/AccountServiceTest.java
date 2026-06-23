package com.pocketpick.auth.service;

import com.pocketpick.auth.domain.dto.CreateCredentialsRequest;
import com.pocketpick.auth.domain.repository.AccountRepository;
import com.pocketpick.auth.domain.service.AccountService;
import com.pocketpick.auth.support.fixture.AccountFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@DisplayName("AccountService")
@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AccountService accountService;

    @Nested
    @DisplayName("credentials 저장")
    class CreateCredentials {

        @Test
        @DisplayName("정상 요청이면 비밀번호를 인코딩해 Account를 저장한다")
        void createCredentials_validRequest_savesEncodedPassword() {
            // given
            CreateCredentialsRequest request = new CreateCredentialsRequest(
                    AccountFixture.USER_ID, AccountFixture.EMAIL, AccountFixture.RAW_PASSWORD);
            given(passwordEncoder.encode(AccountFixture.RAW_PASSWORD))
                    .willReturn(AccountFixture.ENCODED_PASSWORD);

            // when
            accountService.createCredentials(request);

            // then
            verify(accountRepository).save(any());
        }
    }
}
