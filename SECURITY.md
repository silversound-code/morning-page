# 🔒 보안 구현 문서

## 1. 비밀번호 암호화

### 1.1 왜 필요한가?

평문 비밀번호 저장의 문제점:
- DB 유출 시 비밀번호 그대로 노출
- 내부 관리자도 비밀번호 볼 수 있음
- 법적 문제 (개인정보보호법)

### 1.2 어떻게 구현했나?

**사용 기술:**
- Spring Security
- BCryptPasswordEncoder

**구현 위치:**
- `SecurityConfig.java`: PasswordEncoder 빈 설정
- `UserService.java`: 암호화/비교 로직

### 1.3 BCrypt란?

[여기에 당신이 이해한 내용 작성]

- 단방향 해시 함수: ...
- Salt 자동 생성: ...
- 같은 비밀번호도 다르게 암호화: ...

### 1.4 코드 설명

#### SecurityConfig.java
```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

**설명:**
[여기에 이 코드가 하는 일 작성]

#### UserService.java - 회원가입
```java
.password(passwordEncoder.encode(password))
```

**설명:**
[여기에 이 코드가 하는 일 작성]

#### UserService.java - 로그인
```java
if (!passwordEncoder.matches(password, user.getPassword())) {
    throw new RuntimeException("비밀번호가 일치하지 않습니다.");
}
```

**설명:**
[여기에 이 코드가 하는 일 작성]

### 1.5 테스트 결과

**평문:**
```
1234
```

**암호화 결과:**
```
$2a$10$xM8VjWE7h3nQ7kLp9zE8/.eU9K...
```

**구조:**
```
$2a$10$xM8VjWE7h3nQ7kLp9zE8/.eU9K...
 │  │  │                         │
 │  │  └─ Salt                   └─ 해시값
 │  └──── Cost (반복 횟수)
 └─────── 알고리즘 버전
```

### 1.6 보안 강화 계획

향후 개선사항:
- [ ] 비밀번호 강도 체크
- [ ] 비밀번호 변경 기능
- [ ] 비밀번호 찾기
- [ ] 로그인 실패 횟수 제한

---

## 2. 환경별 설정 분리

### 2.1 왜 필요한가?

[여기에 작성]

### 2.2 구현 방법

[여기에 작성]

---

## 3. 추가 보안 고려사항

### 3.1 SQL Injection 방지
- JPA 사용으로 자동 방지

### 3.2 XSS 방지
- [TODO]

### 3.3 CSRF 방지
- 현재는 비활성화
- API 서버이므로 필요 없음

---

## 참고 자료

- [Spring Security 공식 문서](https://spring.io/projects/spring-security)
- [BCrypt 설명](...)