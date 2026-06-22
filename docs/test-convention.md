# 테스트 코드 컨벤션 — PocketPick

---

## 1. 테스트 피라미드

```
단위 테스트 70~80%  → @ExtendWith(MockitoExtension.class)
슬라이스 테스트 15~20% → @WebMvcTest, @DataJpaTest
통합 테스트 5~10%   → @SpringBootTest
```

---

## 2. 명명 규칙

```java
// 메서드명_조건_결과
void login_wrongPassword_throwsUnauthorizedException()

// @Nested + 한글 DisplayName 조합
@DisplayName("AuthService")
class AuthServiceTest {
    @Nested @DisplayName("로그인")
    class Login {
        @Test @DisplayName("비밀번호 불일치 시 예외를 던진다")
        void failsWhenPasswordMismatch() { ... }
    }
}
```

---

## 3. Given-When-Then

- `// given`, `// when`, `// then` 주석 항상 유지
- `when` 단계는 테스트당 반드시 1개
- 상태 검증 우선, 행위 검증은 꼭 필요한 경우만

```java
@Test
void login_validCredentials_returnsAccessToken() {
    // given
    given(userRepository.findByEmail(email)).willReturn(Optional.of(user));

    // when
    LoginResponse response = authService.login(new LoginRequest(email, password));

    // then
    assertThat(response.accessToken()).isNotBlank();
}
```

---

## 4. 단위 테스트 원칙

- **F.I.R.S.T**: Fast, Isolated, Repeatable, Self-validating, Timely
- **DAMP > DRY**: 가독성 우선, 중복 허용
- **Public API만 테스트**: private 메서드 직접 테스트 금지
- **로직 금지**: 테스트 안에 if/for/while 금지 → `@ParameterizedTest` 사용
- **불변 테스트**: 리팩터링해도 깨지지 않도록 구현 세부사항 검증 금지

```java
❌ verify(repo, times(1)).save(any());   // 구현 세부사항
✅ assertThat(response.userId()).isNotNull();  // 관찰 가능한 결과
```

---

## 5. 테스트 더블 선택 우선순위

```
1순위: 실제 객체
2순위: Fake (In-Memory 구현체)
3순위: Stub (반환값 지정)
4순위: Spy
5순위: Mock (최후 수단 — 과도 사용 시 구현에 결합됨)
```

| 종류 | 사용 시점 |
|------|----------|
| Stub | 의존 객체가 특정 값을 반환해야 할 때 |
| Mock | 호출 자체가 비즈니스 의미를 가질 때 |
| Fake | 외부 서비스의 현실적인 대체가 필요할 때 |

---

## 6. Spring Boot 어노테이션

| 어노테이션 | 대상 | 특징 |
|-----------|------|------|
| `@ExtendWith(MockitoExtension.class)` | Service, Domain | Spring 컨텍스트 없음, 가장 빠름 |
| `@WebMvcTest` | Controller | Web 계층만 로드, Service는 `@MockBean` |
| `@DataJpaTest` | Repository | JPA 계층만 로드, 기본 H2 사용 |
| `@SpringBootTest` | 통합 | 전체 컨텍스트 로드, 남발 금지 |

---

## 7. 테스트 대상 / 비대상

```
✅ 도메인 비즈니스 규칙, 경계값, 예외 케이스
✅ Service 유스케이스 (성공/실패)
✅ Controller HTTP 상태코드 + 응답 구조
✅ 커스텀 Repository 쿼리

❌ 단순 getter/setter, Lombok 생성 메서드
❌ Framework 자체 동작 (Spring DI, JPA 기본 CRUD)
❌ private 메서드 직접 테스트
```

---

## 8. 안티패턴

```java
❌ Thread.sleep() → Awaitility 사용
❌ System.out.println() → assertion으로 대체
❌ try-catch로 예외 삼키기 → assertThatThrownBy() 사용
❌ assertion 없는 테스트
❌ LocalDate.now() 직접 호출 → Clock 주입으로 제어
❌ @SpringBootTest를 단위 테스트에 사용
```

---

## 9. 테스트 파일 구조

```
test/java/com/pocketpick/auth/
├── domain/          ← 도메인 단위 테스트
├── service/         ← 서비스 단위 테스트
├── controller/      ← 컨트롤러 슬라이스 테스트
├── repository/      ← 레포지터리 슬라이스 테스트
├── integration/     ← 통합 테스트
└── support/
    ├── fixture/     ← 테스트 픽스처 (UserFixture 등)
    └── fake/        ← Fake 구현체
```
