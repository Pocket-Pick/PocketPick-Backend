# Jira 워크플로우 가이드 — PocketPick

> Atlassian, LINE, 카카오, SK C&C 실무 사례 기반

---

## 프로젝트 정보

- **Jira 사이트**: pocketpick1.atlassian.net
- **프로젝트 키**: `SCRUM`
- **티켓 번호 형식**: `SCRUM-1`, `SCRUM-2`, …

---

## 1. 이슈 타입 계층

```
에픽 (Epic)
└── 스토리 (Story) / 작업 (Task) / Bug
    └── Subtask
```

| 타입 | 언제 쓰나 | 예시 |
|------|----------|------|
| **에픽** | 2주 이상 걸리는 큰 기능 묶음 | `[auth] 인증 시스템 구현` |
| **스토리** | 사용자가 직접 체감하는 기능 | `구매자가 카드를 검색할 수 있다` |
| **작업** | 사용자 가치와 무관한 기술 작업 | `JWT 토큰 발급 API 개발` |
| **Bug** | 기존 기능이 의도대로 동작하지 않을 때 | `로그인 시 500 에러 발생` |
| **Subtask** | 작업/스토리를 반나절 단위로 쪼갠 것 | `JwtProvider 클래스 작성` |

### 스토리 vs 작업 구분 기준

> "이 작업의 결과를 사용자가 직접 느낄 수 있는가?"
> - **YES** → 스토리
> - **NO** → 작업

```
✅ 스토리: 판매자가 카드 사진을 올릴 수 있다
✅ 작업:   S3 업로드 presigned URL API 구현
✅ 작업:   gradle 멀티모듈 설정
❌ 잘못된 스토리: JWT 구현 (사용자가 JWT를 체감하지 않음)
```

---

## 2. 워크플로우 (상태 흐름)

```
To Do → In Progress → In Review → Done
```

| 상태 | 의미 | 전환 주체 |
|------|------|----------|
| **To Do** | 작업 예정, 스프린트에 포함됨 | - |
| **In Progress** | 개발 진행 중 | 본인 (작업 시작 즉시) |
| **In Review** | PR 오픈, 리뷰 대기 중 | 본인 (PR 오픈 시) |
| **Done** | 리뷰 승인 + develop 머지 완료 | 본인 (머지 후) |

**원칙**
- 작업 시작하면 **즉시** In Progress로 변경 (Daily Standup 전에)
- 하나의 티켓 = 하나의 브랜치 = 하나의 PR
- In Progress 티켓은 1인당 최대 2개

---

## 3. 티켓 작성법

### 제목
- **명령형 동사**로 시작
- 검증: "이 티켓을 완료하려면 나는 \_\_해야 한다"

```
✅ JWT 토큰 발급 API 구현
✅ 카드 목록 페이지네이션 적용
✅ 로그인 시 500 에러 수정
❌ Fix issue
❌ 버그 수정
❌ 작업
```

### 스토리 / 작업 설명 구조

```markdown
## 배경 (Why)
[역할]로서, [기능]을 원한다.
왜냐하면 [이유]이기 때문이다.

## 완료 기준 (Definition of Done)
- [ ] (구체적인 동작 조건)
- [ ] (엣지 케이스 처리)
- [ ] 통합 테스트 작성 완료

## 참고
- 관련 에픽: SCRUM-N
- API 명세: (링크)
```

**예시**
```markdown
## 배경 (Why)
구매자로서, 카드 목록을 페이지 단위로 보고 싶다.
왜냐하면 전체 목록을 한 번에 로딩하면 느리기 때문이다.

## 완료 기준 (Definition of Done)
- [ ] GET /api/v1/cards?page=0&size=20 응답 반환
- [ ] 빈 결과 시 200 + 빈 배열 반환 (404 아님)
- [ ] size 최대값 100 초과 시 400 반환
- [ ] 통합 테스트 작성 완료

## 참고
- 관련 에픽: SCRUM-1
```

### Bug 설명 구조

```markdown
## 환경
- 서비스: auth
- 발생 버전 / 브랜치:

## 재현 절차
1.
2.
3.

## 기대 결과

## 실제 결과

## 로그 / 스크린샷
```

---

## 4. PocketPick 에픽 목록 (예시)

| 에픽 | 설명 |
|------|------|
| `[auth] 인증 시스템 구현` | 회원가입, 로그인, JWT, OAuth |
| `[card] 카드 등록/조회` | 카드 CRUD, 검색, 필터 |
| `[trade] 거래 흐름` | 구매 제안, 수락, 결제 |
| `[chat] 채팅` | 거래 중 메시지 |
| `[infra] 인프라 설정` | 멀티모듈, CI/CD, 모니터링 |

---

## 5. 스프린트 운영

### 명명 규칙
```
SCRUM YYYY Sprint NN
예: SCRUM 2025 Sprint 01
```

### 기본 설정
- 기간: **2주**
- WIP 한도: In Progress **1인당 최대 2개**

### 스토리 포인트 (피보나치)

| 포인트 | 기준 |
|--------|------|
| 1 | 반나절 이내, 명확한 작업 |
| 2 | 하루 |
| 3 | 이틀 |
| 5 | 사흘~일주일 |
| 8 | 일주일 이상 → 분해 권고 |
| 13+ | 반드시 분해 후 재추정 |

- 포인트 = **난이도** 측정, 시간 추정 아님
- 부분 완료 = 0점 (완전히 Done이어야 포인트 부여)
- 버그 수정, 리팩터링 = 0점

### 세레모니

| 세레모니 | 시점 | 시간 |
|---------|------|------|
| Sprint Planning | 스프린트 시작일 | ~1시간 |
| Daily Standup | 매일 | 15분 이내 |
| Sprint Review | 스프린트 종료일 | ~30분 |
| Retrospective | Review 직후 | ~30분 (KPT 방식) |

**Daily Standup 3문항**
1. 어제 한 일 (티켓 번호 포함)
2. 오늘 할 일 (티켓 번호 포함)
3. 블로커 (없으면 "없음")

**KPT 회고**
- **Keep**: 잘 됐던 것, 계속할 것
- **Problem**: 문제가 됐던 것
- **Try**: 다음 스프린트에 시도할 것

---

## 6. 작업 흐름 요약

```
1. Jira 티켓 생성 (To Do)
2. 브랜치 생성 → git-convention.md 참고
3. 개발 시작 → 티켓 In Progress로 변경
4. PR 오픈 → 티켓 In Review로 변경
5. 리뷰 승인 + 머지 → 티켓 Done으로 변경
```

---

## 참고 출처

- [Atlassian Jira Workflow Guide](https://www.atlassian.com/software/jira/guides/workflows/overview)
- [LINE 광고 서버팀 DevOps 문화](https://engineering.linecorp.com/ko/blog/line-ads-devops-culture)
- [LINE Pay 스토리 포인트 운영](https://engineering.linecorp.com/ko/blog/user-story-point-in-line-pay-team)
- [카카오 애자일 개발 문화](https://tech.kakao.com/posts/398)
- [SK C&C Jira 워크플로우](https://engineering-skcc.github.io/devops-tools/jiraworkflow/)
