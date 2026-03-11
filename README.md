# 🌅 Morning Page

매일 아침, 나를 위한 작은 기록

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.10-brightgreen)
![MySQL](https://img.shields.io/badge/MySQL-8.0-orange)

## 📝 프로젝트 소개

Morning Page는 매일 아침 활동을 기록하고 습관을 형성하는 웹 애플리케이션입니다.
침대에서 휴대폰만 보는 대신, 의미 있는 활동을 하고 기록하며 성장해가는 자신을 발견하세요.


---

## ✨ 주요 기능

### 1. 기록 관리
- ✅ 매일 아침 활동 기록 (운동, 독서, 명상 등)
- ✅ 8개 기본 카테고리 제공
- ✅ 공개/비공개 설정
- ✅ 기록 수정/삭제

### 2. 달력 뷰
- 📅 월별 달력으로 기록 한눈에 보기
- 📅 날짜별 기록 확인
- 📅 빈 날짜 클릭 시 기록 작성

### 3. 통계
- 🔥 연속 기록일 추적 (Streak)
- 📊 총 기록 수
- 📊 월별 기록 수
- 📊 카테고리별 분석

### 4. 공개 피드
- 🌍 다른 사람들의 공개 기록 보기
- 💪 동기부여 및 영감

### 5. 검색 & 필터
- 🔍 활동명/내용 검색
- 🏷️ 카테고리별 필터링

### 6. 다크모드
- 🌙 라이트/다크 테마 전환
- 💾 설정 자동 저장

---

## 🛠️ 기술 스택

### Backend
- **Java 17**
- **Spring Boot 3.5.10**
  - Spring Data JPA
  - Spring Security (비밀번호 암호화)
  - Spring Web
- **MySQL 8.0**
- **Gradle**

### Frontend
- **HTML5**
- **CSS3** (Vanilla CSS, 반응형)
- **JavaScript** (ES6+)

### 개발 도구
- **IntelliJ IDEA / VSCode**
- **Git & GitHub**
- **Postman**

### 배포
- **AWS EC2** (Ubuntu 22.04)
- **MySQL 8.0**

---

## 🏗️ 아키텍처
```
┌─────────────┐
│  Frontend   │  HTML/CSS/JS
│  (Static)   │
└──────┬──────┘
       │ REST API
┌──────▼──────────────────┐
│   Spring Boot Backend   │
│  ┌──────────────────┐  │
│  │  Controller      │  │
│  └────────┬─────────┘  │
│  ┌────────▼─────────┐  │
│  │  Service         │  │
│  └────────┬─────────┘  │
│  ┌────────▼─────────┐  │
│  │  Repository      │  │
│  └────────┬─────────┘  │
└───────────┼────────────┘
            │ JPA
┌───────────▼────────────┐
│     MySQL Database     │
└────────────────────────┘
```

---

## 🚀 배포 URL

**AWS EC2:** http://43.201.10.52:8080

## 🎯 브라우저 테스트

지금 바로 접속해보세요: http://43.201.10.52:8080

⚠️ 포트폴리오용 서버로, 예고 없이 중단될 수 있습니다.

### 배포 환경
- 서버: AWS EC2 t3.micro (1vCPU, 1GB RAM)
- OS: Ubuntu 22.04 LTS
- 데이터베이스: MySQL 8.0
- 런타임: Java 17

---

## 💻 로컬 실행 방법

### 사전 요구사항
- JDK 17 이상
- MySQL 8.0 이상
- Gradle

### 1. 저장소 클론
```bash
git clone https://github.com/silversound-code/morning-page.git
cd morning-page
```

### 2. MySQL 설정
```sql
-- MySQL 접속
mysql -u root -p

-- 사용자 생성
CREATE USER 'morningpage'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON morningpage_db.* TO 'morningpage'@'localhost';
FLUSH PRIVILEGES;

-- DB 생성
CREATE DATABASE morningpage_db;
exit;
```

### 3. 환경 설정
```bash
cd src/main/resources
cp application-local.properties.example application-local.properties
```

`application-local.properties` 파일 수정:
```properties
spring.datasource.username=morningpage
spring.datasource.password=your_password
```

### 4. 실행
```bash
./gradlew bootRun
```

### 5. 접속
```
http://43.201.10.52:8080

```

---

## 📂 프로젝트 구조
```
morningpage/
├── src/
│   └── main/
│       ├── java/com/morning/morningpage/
│       │   ├── config/           # 설정 (Security 등)
│       │   ├── controller/       # REST API 컨트롤러
│       │   ├── service/          # 비즈니스 로직
│       │   ├── repository/       # JPA 레포지토리
│       │   ├── entity/           # 엔티티 (User, MorningPage, Category)
│       │   └── dto/              # 데이터 전송 객체
│       └── resources/
│           ├── static/           # 프론트엔드 (HTML/CSS/JS)
│           │   ├── index.html
│           │   ├── css/
│           │   └── js/
│           ├── application.properties
│           └── application-local.properties.example
├── build.gradle
└── README.md
```

---

## 📸 주요 화면

### 메인 대시보드
_(스크린샷 추가 예정)_

### 달력 뷰
_(스크린샷 추가 예정)_

### 다크모드
_(스크린샷 추가 예정)_

---

## 💡 주요 구현 사항

### 1. 비밀번호 암호화 (BCrypt)
```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```
- 단방향 해시 함수
- Salt 자동 생성
- 평문 저장 방지

### 2. 연속 기록일 계산
```java
private int calculateStreak(List<MorningPage> records) {
    // 오늘/어제 기록 확인
    // 연속된 날짜 계산
    // Streak 반환
}
```

### 3. 환경별 설정 분리
```
application.properties              (공통)
application-local.properties        (로컬)
application-prod.properties         (배포)
```

---

## 🐛 트러블슈팅

### 문제 1: MySQL 비밀번호 정책
**문제:** 비밀번호가 약해서 에러 발생
```
ERROR 1819: Your password does not satisfy the current policy requirements
```

**해결:** 강력한 비밀번호 사용 (대문자+소문자+숫자+특수문자)

### 문제 2: Git에 비밀번호 노출
**문제:** application.properties에 DB 비밀번호 포함

**해결:**
- 환경별 설정 파일 분리
- .gitignore에 민감 정보 추가
- application-local.properties.example 제공

### 문제 3: Render 배포 실패
**문제:** Java 자동 감지 실패

**해결:** AWS EC2로 변경하여 배포 성공

---

## 복습 항목들

### 기술
- Spring Boot 전체 구조 이해
- JPA 관계 매핑 및 쿼리 작성
- REST API 설계 원칙
- BCrypt 암호화 원리
- AWS EC2 배포 경험

### 개발 프로세스
- Entity → Repository → Service → Controller 흐름
- 환경별 설정 분리의 중요성
- 보안 고려사항 (비밀번호 암호화, 민감정보 관리)
- 파일 구조화의 필요성

---

## 🎯 향후 개선 계획

### 기능 추가
- [ ] 이미지 업로드
- [ ] 페이징
- [ ] 친구 기능
- [ ] 배지 시스템
- [ ] 목표 설정

### 기술 개선
- [ ] JWT 인증
- [ ] Redis 캐싱
- [ ] 테스트 코드 작성
- [ ] CI/CD 파이프라인
- [ ] Docker 컨테이너화

---

## 🗓️ 개발 일정

- [x] 프로젝트 초기 설정
- [x] Entity 및 Repository 구현
- [x] Service 및 Controller 구현
- [x] 프론트엔드 UI 개발
- [x] 보안 기능 (비밀번호 암호화)
- [x] Validation 및 최적화
- [x] AWS EC2 배포

---

## 👤 개발자
- GitHub: [@silversound-code](https://github.com/silversound-code)

---

## 📄 라이센스

This project is licensed under the MIT License.
```

---
