# 🌅 Morning Page

매일 아침, 나를 위한 작은 기록

## 📝 프로젝트 소개

Morning Page는 매일 아침 활동을 기록하고 습관을 형성하는 웹 애플리케이션입니다.
침대에서 휴대폰만 보는 대신, 의미 있는 활동을 하고 기록하며 성장해가는 자신을 발견하세요.

## 🎯 주요 기능

- ✅ 매일 아침 활동 기록
- 📅 달력으로 한눈에 보는 기록
- 🔥 연속 기록일 추적
- 📊 카테고리별 통계
- 🌍 공개 기록으로 다른 사람들과 동기부여

## 🛠️ 기술 스택

### Backend
- Java 17
- Spring Boot 3.5.10
- Spring Data JPA
- MySQL 8.0
- Gradle

### Frontend
- HTML5
- CSS3
- Vanilla JavaScript

## 🚀 로컬 실행 방법

### 사전 요구사항
- JDK 17 이상
- MySQL 8.0 이상
- Gradle

### 실행 단계

1. 저장소 클론
```bash
git clone https://github.com/silversound-code/morning-page.git
cd morning-page
```

2. MySQL 데이터베이스 생성
```sql
CREATE DATABASE morningpage_db;
```

3. application.properties 설정
```properties
spring.datasource.username=root
spring.datasource.password=your_password
```

4. 애플리케이션 실행
```bash
./gradlew bootRun
```

5. 브라우저에서 접속
```
http://localhost:8080
```

## 📂 프로젝트 구조
```
morningpage/
├── src/
│   └── main/
│       ├── java/com/morning/morningpage/
│       │   ├── entity/          # 엔티티 클래스
│       │   ├── repository/      # JPA 레포지토리
│       │   ├── service/         # 비즈니스 로직
│       │   ├── controller/      # REST API 컨트롤러
│       │   └── dto/             # 데이터 전송 객체
│       └── resources/
│           ├── static/          # 정적 파일 (HTML, CSS, JS)
│           └── application.properties
└── build.gradle
```

## 🗓️ 개발 일정

- [x] 프로젝트 초기 설정 (2026.02.14)
- [x] Entity 및 Repository 구현 (2026.02.14)
- [ ] Service 및 Controller 구현 (2026.02.15)
- [ ] 프론트엔드 UI 개발 (2026.02.16)
- [ ] 통계 및 달력 기능 (2026.02.21-23)
- [ ] 배포 (2026.03.01)

## 👤 개발자

- GitHub: [@silversound-code](https://github.com/silversound-code/morning-page.git)

## 📄 라이센스

This project is licensed under the MIT License.

## 🚀 로컬 실행 방법

### 1. 저장소 클론
```bash
git clone https://github.com/your-username/morning-page.git
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

### 3. 환경 설정 파일 생성
```bash
cd src/main/resources
cp application-local.properties.example application-local.properties
```

### 4. 비밀번호 설정
`application-local.properties` 파일 열어서:
```properties
spring.datasource.username=morningpage
spring.datasource.password=your_password  # 여기에 MySQL 비밀번호 입력
```

### 5. 실행
```bash
./gradlew bootRun
```

### 6. 접속
```
http://localhost:8080
```