# 코딩 컨벤션 — PocketPick

> Google AIP, Microsoft Azure REST API Guidelines, RFC 9110, Stripe 실무 사례 기반

---

## 1. RESTful API 설계 규칙

### URL 네이밍

- **명사 사용, 동사 금지** — 행위는 HTTP 메서드로 표현
- **복수형** 컬렉션 명사
- **소문자 + 하이픈(kebab-case)**

```
✅ GET /users
✅ GET /users/1
✅ GET /users/1/favorite-cards
✅ POST /sale-posts
❌ GET /getUser
❌ POST /createSalePost
❌ GET /user (단수)
```

### URL 계층 깊이

- **최대 2depth** (컬렉션/아이템/서브컬렉션) 권고
- 그 이상이면 별도 최상위 리소스로 분리

```
✅ /users/{id}/favorite-cards
✅ /sale-posts/{id}/images
❌ /users/{id}/sale-posts/{postId}/items/{itemId}  → /sale-post-items/{itemId} 로 분리
```

### HTTP 메서드

| 메서드 | 대상 | 의미 |
|--------|------|------|
| `GET` | 컬렉션 / 단일 | 조회 (상태 변경 없음) |
| `POST` | 컬렉션 | 새 리소스 생성 |
| `PATCH` | 단일 | 부분 수정 (변경 필드만 전송) |
| `PUT` | 단일 | 전체 교체 (원칙적으로 지양, PATCH 우선) |
| `DELETE` | 단일 | 삭제 |

> **PUT 지양 이유 (Google AIP-134)**: PUT으로 설계하면 나중에 필드 추가 시 기존 클라이언트가 새 필드를 날려버릴 수 있음.

### HTTP 상태 코드

| 코드 | 사용 시점 |
|------|----------|
| `200 OK` | 조회 성공, 수정 성공 |
| `201 Created` | POST로 리소스 생성 완료 (`Location` 헤더 포함) |
| `204 No Content` | DELETE 성공, 본문 불필요한 수정 |
| `400 Bad Request` | 유효성 검증 실패, 필수 파라미터 누락 |
| `401 Unauthorized` | 인증 정보 없거나 만료 |
| `403 Forbidden` | 인증은 됐으나 권한 없음 |
| `404 Not Found` | 리소스 없음 |
| `409 Conflict` | 중복 생성, 동시성 충돌 |
| `422 Unprocessable Entity` | 형식은 올바르나 비즈니스 규칙 위반 |
| `500 Internal Server Error` | 예상치 못한 서버 오류 |

> **403 vs 404**: 권한 없는 리소스는 존재 여부를 노출하지 않을 경우 404 대신 403 사용 (Google AIP-193).

### 응답 형식

**성공**
```json
// 단건 (GET /users/1)
{
  "id": 1,
  "email": "user@example.com",
  "nickname": "닉네임"
}

// 목록 (GET /users)
{
  "content": [...],
  "totalElements": 100,
  "totalPages": 5,
  "size": 20,
  "number": 0
}
```

**에러**
```json
{
  "code": "RESOURCE_NOT_FOUND",
  "message": "해당 리소스를 찾을 수 없습니다.",
  "target": "userId"
}
```

- `code`: 기계가 처리할 수 있는 문자열 enum
- `message`: 개발자가 읽을 수 있는 설명
- `target`: 오류가 발생한 필드명 (해당 시)

### 버저닝

이 프로젝트는 `/v1/` prefix를 **사용하지 않는다.**
내부 마이크로서비스 간 API이므로 하위 호환성 유지로 관리.
외부 공개 API가 생기면 별도 논의.

---

## 2. Entity / VO 설계 — 필드 변경 주기 고려

엔티티와 VO를 설계할 때 **필드의 변경 주기(Rate of Change)** 를 함께 고려한다.
변경 주기가 다른 필드를 같은 클래스에 묶으면 단일 책임 원칙(SRP) 위반으로 이어진다.

### 원칙

> 함께 변하는 것은 함께 묶고, 다르게 변하는 것은 분리한다.

### 판단 기준

| 변경 주기 | 예시 | 처리 |
|----------|------|------|
| 거의 변하지 않음 | 이메일, 가입일시 | 엔티티 기본 필드 |
| 사용자가 수시로 변경 | 닉네임, 프로필 사진, 지역 | 별도 VO 또는 그룹화 |
| 비즈니스 상태 전이 | 판매글 status (SELLING→RESERVED→COMPLETED) | 상태 전이 메서드로 캡슐화 |
| 외부 시스템 연동 데이터 | 결제 정보, 외부 ID | 별도 엔티티로 분리 |

### 예시 — User 엔티티

```java
// ❌ 변경 주기가 다른 필드를 한 곳에 묶은 경우
@Entity
public class User {
    private String email;          // 변경 안 함
    private String password;       // 보안 이벤트 시 변경
    private String nickname;       // 사용자가 수시로 변경
    private String profileImageUrl;// 사용자가 수시로 변경
    private String region;         // 사용자가 수시로 변경
    private boolean notificationEnabled; // 사용자가 수시로 변경
}

// ✅ 변경 주기별 분리
@Entity
public class User {
    private String email;          // 변경 안 함
    private String password;       // 별도 메서드로 변경

    @Embedded
    private UserProfile profile;   // 닉네임, 사진, 지역 — 함께 변함
    private boolean notificationEnabled;
}

@Embeddable
public class UserProfile {         // VO: 함께 변하는 필드 묶음
    private String nickname;
    private String profileImageUrl;
    private String region;
}
```

### VO(Value Object) 사용 기준

- **식별자가 없고**, **값 자체가 동등성 기준**일 때 VO 사용
- **불변(immutable)** 으로 설계 — setter 없음, 변경 시 새 객체 생성
- 비즈니스 규칙 검증을 VO 생성자 안에서 수행

```java
// VO 예시 — 가격
public class Price {
    private final BigDecimal value;

    public Price(BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 0 이상이어야 합니다.");
        }
        this.value = value;
    }
}
```

---

## 3. 메서드 명명 규칙

### 기본 형태

```
V (동사)          — 반환값 없거나 동작 자체가 목적
V + O (동사+목적어) — 목적어가 있을 때
```

### 패턴별 가이드

| 패턴 | 용도 | 예시 |
|------|------|------|
| `get + O` | 단건 조회, 없으면 예외 | `getUser()`, `getSalePost()` |
| `find + O` | 단건 조회, 없으면 Optional | `findByEmail()` |
| `get + O + s` / `getAll + O` | 목록 조회 | `getSalePosts()` |
| `create + O` | 생성 | `createSalePost()` |
| `update + O` | 수정 | `updateProfile()` |
| `delete + O` | 삭제 | `deleteSalePost()` |
| `register + O` | 등록 (도메인 의미 있을 때) | `registerCard()` |
| `validate + O` | 검증, 실패 시 예외 | `validatePassword()` |
| `is + 상태` | boolean 반환 | `isReserved()`, `isOwner()` |
| `has + O` | 보유 여부 boolean | `hasPermission()` |
| `can + V` | 가능 여부 boolean | `canCancel()` |
| `publish + O` | 이벤트/알림 발행 | `publishNotification()` |
| `send + O` | 외부 전송 | `sendEmail()` |

### 상태 전이 메서드 (도메인 객체 내부)

상태 변경은 setter 대신 **의미 있는 동사 메서드**로 표현

```java
// ❌
salePost.setStatus(SalePostStatus.RESERVED);

// ✅
salePost.reserve();     // 예약
salePost.complete();    // 거래 완료
salePost.cancel();      // 취소
```

### Service 계층 메서드명 예시

```java
// AuthService
login(LoginRequest request)
reissueToken(String refreshToken)
logout(Long userId)

// UserService
register(RegisterRequest request)
getUser(Long userId)
updateProfile(Long userId, UpdateProfileRequest request)

// SalePostService
createSalePost(Long sellerId, CreateSalePostRequest request)
getSalePost(Long salePostId)
getSalePosts(SalePostSearchCondition condition)
updateSalePost(Long salePostId, UpdateSalePostRequest request)
deleteSalePost(Long salePostId)
```

---

## 4. 예외 처리 구조

> [f-lab-edu/woowahan-eats](https://github.com/f-lab-edu/woowahan-eats) 구조 기반

### 패키지 구조

```
global/
└── exception/
    ├── BusinessException.java       ← 전역 최상위 커스텀 예외
    ├── ErrorResponse.java           ← 공통 에러 응답 DTO
    └── GlobalExceptionHandler.java  ← 전역 예외 핸들러

domain/
└── user/
    └── exception/
        ├── UserException.java           ← 도메인 중간 예외
        ├── DuplicateEmailException.java ← 구체 예외
        └── UserNotFoundException.java
```

### 예외 계층 구조

```
RuntimeException
  └── BusinessException                  ← errorCode(String) + HttpStatus 보유
        └── UserException                ← 도메인 중간 예외 (protected 생성자)
              ├── DuplicateEmailException ← 생성자에 코드/상태 직접 하드코딩
              └── UserNotFoundException
```

### BusinessException

```java
@Getter
public class BusinessException extends RuntimeException {
    private final String errorCode;
    private final HttpStatus httpStatus;

    public BusinessException(String errorCode, String message, HttpStatus httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }
}
```

### 도메인 중간 예외

```java
@Getter
public class UserException extends BusinessException {
    protected UserException(String errorCode, String message, HttpStatus httpStatus) {
        super(errorCode, message, httpStatus);
    }
}
```

### 구체 예외

각 예외 클래스가 자신의 에러 코드, 메시지, HTTP 상태를 직접 보유한다.

```java
public class UserNotFoundException extends UserException {
    public UserNotFoundException() {
        super("USER_NOT_FOUND", "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}

public class DuplicateEmailException extends UserException {
    public DuplicateEmailException() {
        super("DUPLICATE_EMAIL", "이미 사용 중인 이메일입니다.", HttpStatus.CONFLICT);
    }
}
```

### ErrorResponse

```java
public record ErrorResponse(String errorCode, String message) {
    public static ErrorResponse of(String errorCode, String message) {
        return new ErrorResponse(errorCode, message);
    }
}
```

JSON 응답:
```json
{
  "errorCode": "USER_NOT_FOUND",
  "message": "사용자를 찾을 수 없습니다."
}
```

HTTP status는 `ResponseEntity`로만 전달, body에는 포함하지 않음.

### GlobalExceptionHandler

```java
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        return ResponseEntity.status(e.getHttpStatus())
                .body(ErrorResponse.of(e.getErrorCode(), e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest()
                .body(ErrorResponse.of("INVALID_INPUT_VALUE", message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Unexpected error", e);
        return ResponseEntity.internalServerError()
                .body(ErrorResponse.of("INTERNAL_SERVER_ERROR", "서버 오류가 발생했습니다."));
    }
}
```

### 계층별 책임

| 계층 | 역할 |
|------|------|
| Controller | `@Valid`로 입력값 기본 검증만 |
| Service | 비즈니스 규칙 검증, `orElseThrow()` 사용, 구체 예외 throw |
| Repository | DB 예외는 Service에서 비즈니스 예외로 재변환 |

