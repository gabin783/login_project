# Spring Boot - Flask 서버 연동 구현 계획

## 목표

Spring Boot 서버에서 사용자의 키와 몸무게 입력값을 받아 Flask AI 서버의 `/predict` API로 전달하고, 반환된 예측 결과를 Thymeleaf 화면에 출력합니다.

## API 정보

- URL: `http://localhost:5000/predict`
- Method: `POST`
- Request JSON:

```json
{
  "height": 170,
  "weight": 70
}
```

- Response JSON:

```json
{
  "result": "정상"
}
```

## 생성 및 수정 파일

### DTO

- `spring-server/src/main/java/com/example/loginproject/dto/PredictRequestDto.java`
  - 사용자가 입력한 키와 몸무게를 담습니다.
  - 화면 입력값 검증에 사용합니다.

- `spring-server/src/main/java/com/example/loginproject/dto/PredictResponseDto.java`
  - Flask 서버가 반환한 예측 결과 JSON을 매핑합니다.

### Service

- `spring-server/src/main/java/com/example/loginproject/service/PredictService.java`
  - `RestTemplate`을 사용해 Flask `/predict` API에 POST 요청을 보냅니다.
  - Flask 응답 JSON을 `PredictResponseDto`로 변환합니다.

### Config

- `spring-server/src/main/java/com/example/loginproject/config/RestTemplateConfig.java`
  - `RestTemplate` Bean을 등록합니다.

- `spring-server/src/main/java/com/example/loginproject/config/SecurityConfig.java`
  - `/predict`, `/predict/**` 경로를 비회원도 접근 가능하도록 허용합니다.

### Controller

- `spring-server/src/main/java/com/example/loginproject/controller/PredictController.java`
  - 예측 입력 폼을 출력합니다.
  - 입력값을 검증합니다.
  - `PredictService`를 호출하고 결과 화면에 데이터를 전달합니다.

### View

- `spring-server/src/main/resources/templates/predict/form.html`
  - 키와 몸무게 입력 화면입니다.

- `spring-server/src/main/resources/templates/predict/result.html`
  - 예측 결과 출력 화면입니다.

### Main

- `spring-server/src/main/resources/templates/index.html`
  - 메인 화면에서 예측 화면으로 이동할 수 있는 링크를 추가합니다.

## 처리 흐름

1. 사용자가 `/predict` 화면에서 키와 몸무게를 입력합니다.
2. Spring Controller가 입력값을 DTO로 받습니다.
3. Spring Service가 `RestTemplate`으로 Flask 서버에 JSON POST 요청을 보냅니다.
4. Flask 서버가 예측 결과 JSON을 반환합니다.
5. Spring Boot가 응답 DTO로 결과를 받습니다.
6. Thymeleaf 결과 화면에 예측 결과를 출력합니다.
