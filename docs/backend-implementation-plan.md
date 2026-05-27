# 백엔드 구현 계획

## 1단계 - 회원 기능

### 생성 파일

- `spring-server/settings.gradle`: Gradle 프로젝트 이름 설정
- `spring-server/build.gradle`: Spring Boot, Security, JPA, Thymeleaf, MySQL 의존성 설정
- `spring-server/src/main/java/com/example/loginproject/LoginProjectApplication.java`: Spring Boot 실행 클래스
- `spring-server/src/main/java/com/example/loginproject/domain/Member.java`: 회원 Entity
- `spring-server/src/main/java/com/example/loginproject/domain/Role.java`: 회원 권한 Enum
- `spring-server/src/main/java/com/example/loginproject/repository/MemberRepository.java`: 회원 JPA Repository
- `spring-server/src/main/java/com/example/loginproject/dto/MemberJoinRequest.java`: 회원가입 요청 DTO
- `spring-server/src/main/java/com/example/loginproject/service/MemberService.java`: 회원가입 및 회원 조회 비즈니스 로직
- `spring-server/src/main/java/com/example/loginproject/controller/MemberController.java`: 회원가입 화면 및 요청 처리 Controller
- `spring-server/src/main/java/com/example/loginproject/config/PasswordEncoderConfig.java`: BCryptPasswordEncoder Bean 설정
- `spring-server/src/main/resources/application.properties`: DB/JPA/Thymeleaf 기본 설정
- `spring-server/src/main/resources/templates/index.html`: 메인 페이지
- `spring-server/src/main/resources/templates/members/join.html`: 회원가입 페이지

### 주요 구현 내용

- 아이디, 비밀번호, 이름을 입력받아 회원가입을 처리합니다.
- 회원 아이디 중복 여부를 검증합니다.
- 비밀번호는 BCryptPasswordEncoder로 암호화하여 저장합니다.
- 기본 가입 권한은 `USER`로 저장합니다.

## 2단계 - 로그인 및 Spring Security 인증

### 생성 및 수정 파일

- `spring-server/src/main/java/com/example/loginproject/config/SecurityConfig.java`: URL 권한, formLogin, logout 설정
- `spring-server/src/main/java/com/example/loginproject/service/CustomUserDetailsService.java`: Spring Security 사용자 인증 정보 로드
- `spring-server/src/main/resources/templates/members/login.html`: 로그인 페이지
- `spring-server/src/main/resources/templates/fragments/navbar.html`: 로그인/로그아웃 메뉴 공통 UI
- `spring-server/src/main/resources/templates/index.html`: 로그인 상태별 화면 표시

### 주요 구현 내용

- Spring Security formLogin 방식으로 로그인합니다.
- 로그인 성공 시 메인 페이지로 이동합니다.
- 로그아웃 기능을 제공합니다.
- 메인 페이지와 AI 예측 페이지는 비회원도 접근할 수 있도록 허용합니다.
- 게시판 기능은 로그인 사용자만 접근할 수 있도록 제한합니다.
- 회원 목록은 관리자만 접근할 수 있도록 제한합니다.

## 3단계 - 게시판 CRUD

### 생성 파일

- `spring-server/src/main/java/com/example/loginproject/domain/Question.java`: 게시글 Entity
- `spring-server/src/main/java/com/example/loginproject/repository/QuestionRepository.java`: 게시글 Repository
- `spring-server/src/main/java/com/example/loginproject/dto/QuestionRequest.java`: 게시글 등록/수정 DTO
- `spring-server/src/main/java/com/example/loginproject/service/QuestionService.java`: 게시글 CRUD 비즈니스 로직
- `spring-server/src/main/java/com/example/loginproject/controller/QuestionController.java`: 게시글 Controller
- `spring-server/src/main/resources/templates/questions/list.html`: 게시글 목록 화면
- `spring-server/src/main/resources/templates/questions/detail.html`: 게시글 상세 화면
- `spring-server/src/main/resources/templates/questions/form.html`: 게시글 등록/수정 화면

### 주요 구현 내용

- 게시글 목록, 상세, 등록, 수정, 삭제 기능을 구현합니다.
- 게시글 작성자 정보를 저장합니다.
- 로그인한 사용자만 글을 작성할 수 있습니다.
- 작성자 본인만 수정 및 삭제할 수 있습니다.

## 4단계 - 답글 기능

### 생성 파일

- `spring-server/src/main/java/com/example/loginproject/domain/Answer.java`: 답글 Entity
- `spring-server/src/main/java/com/example/loginproject/repository/AnswerRepository.java`: 답글 Repository
- `spring-server/src/main/java/com/example/loginproject/dto/AnswerRequest.java`: 답글 등록/수정 DTO
- `spring-server/src/main/java/com/example/loginproject/service/AnswerService.java`: 답글 비즈니스 로직
- `spring-server/src/main/java/com/example/loginproject/controller/AnswerController.java`: 답글 Controller
- `spring-server/src/main/resources/templates/answers/form.html`: 답글 수정 화면

### 주요 구현 내용

- 회원은 게시글에 답글을 등록할 수 있습니다.
- 본인이 작성한 답글만 수정 및 삭제할 수 있습니다.
- 게시글 상세 화면에서 답글 목록을 함께 출력합니다.

## 5단계 - 관리자 회원 목록

### 생성 및 수정 파일

- `spring-server/src/main/java/com/example/loginproject/controller/AdminController.java`: 관리자 전용 회원 목록 Controller
- `spring-server/src/main/resources/templates/admin/member-list.html`: 회원 목록 화면
- `spring-server/src/main/java/com/example/loginproject/config/SecurityConfig.java`: 관리자 URL 권한 추가

### 주요 구현 내용

- 관리자만 회원 목록 페이지에 접근할 수 있습니다.
- 회원 아이디, 이름, 권한, 가입일을 목록으로 확인할 수 있습니다.

## 6단계 - AI 예측 연동

### 생성 파일

- `spring-server/src/main/java/com/example/loginproject/dto/PredictRequest.java`: 예측 요청 DTO
- `spring-server/src/main/java/com/example/loginproject/dto/PredictResponse.java`: 예측 응답 DTO
- `spring-server/src/main/java/com/example/loginproject/service/PredictService.java`: Flask API 호출 로직
- `spring-server/src/main/java/com/example/loginproject/controller/PredictController.java`: 예측 화면 Controller
- `spring-server/src/main/resources/templates/predict/form.html`: 예측 입력 화면
- `spring-server/src/main/resources/templates/predict/result.html`: 예측 결과 화면

### 주요 구현 내용

- 비회원도 예측 페이지에 접근할 수 있습니다.
- Spring Boot 서버에서 Flask AI 서버로 JSON 요청을 보냅니다.
- Flask 서버의 예측 결과를 화면에 출력합니다.
