package com.pocketpick.auth.service;

import com.pocketpick.auth.domain.domain.exception.DuplicateEmailException;
import com.pocketpick.auth.domain.domain.exception.WeakPasswordException;
import com.pocketpick.auth.domain.dto.CreateCredentialsRequest;
import com.pocketpick.auth.domain.dto.ValidateCredentialsRequest;
import com.pocketpick.auth.domain.repository.AccountRepository;
import com.pocketpick.auth.domain.service.AccountService;
import com.pocketpick.auth.support.fixture.AccountFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@DisplayName("AccountService")
@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @Nested
    @DisplayName("credentials 검증")
    class Validate {

        @Test
        @DisplayName("이메일 중복이 없고 비밀번호가 8자 이상이면 예외를 던지지 않는다")
        void validate_validRequest_doesNotThrow() {
            // given
            ValidateCredentialsRequest request = new ValidateCredentialsRequest(
                    AccountFixture.EMAIL, AccountFixture.RAW_PASSWORD);
            given(accountRepository.existsByEmail(AccountFixture.EMAIL)).willReturn(false);

            // when & then
            assertThatCode(() -> accountService.validate(request)).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("이메일이 중복이면 DuplicateEmailException을 던진다")
        void validate_duplicateEmail_throwsDuplicateEmailException() {
            // given
            ValidateCredentialsRequest request = new ValidateCredentialsRequest(
                    AccountFixture.EMAIL, AccountFixture.RAW_PASSWORD);
            given(accountRepository.existsByEmail(AccountFixture.EMAIL)).willReturn(true);

            // when & then
            assertThatThrownBy(() -> accountService.validate(request))
                    .isInstanceOf(DuplicateEmailException.class);
        }

        @Test
        @DisplayName("비밀번호가 8자 미만이면 WeakPasswordException을 던진다")
        void validate_shortPassword_throwsWeakPasswordException() {
            // given
            ValidateCredentialsRequest request = new ValidateCredentialsRequest(
                    AccountFixture.EMAIL, "short");
            given(accountRepository.existsByEmail(AccountFixture.EMAIL)).willReturn(false);

            // when & then
            assertThatThrownBy(() -> accountService.validate(request))
                    .isInstanceOf(WeakPasswordException.class);
        }
    }

    @Nested
    @DisplayName("credentials 저장")
    class CreateCredentials {

        @Test
        @DisplayName("정상 요청이면 인코딩된 비밀번호로 Account를 저장한다")
        void createCredentials_validRequest_savesAccount() {
            // given
            CreateCredentialsRequest request = new CreateCredentialsRequest(
                    AccountFixture.USER_ID, AccountFixture.EMAIL, AccountFixture.ENCODED_PASSWORD);

            // when
            accountService.createCredentials(request);

            // then
            verify(accountRepository).save(any());
        }
    }
}
