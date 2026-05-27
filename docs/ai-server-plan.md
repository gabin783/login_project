# AI 서버 구현 계획

## 목표

Flask 기반 AI 서버를 구축하여 Spring Boot 서버와 REST API 방식으로 연동 가능한 비만 여부 예측 기능을 제공합니다.

입력값은 키(`height`)와 몸무게(`weight`)이며, 출력값은 비만 여부 예측 결과입니다.

## 추천 폴더 구조

```text
flask-server/
 ┣ app.py
 ┣ train_model.py
 ┣ obesity_model.pkl
 ┣ requirements.txt
 ┣ data/
 ┃ ┗ health_checkup.csv
 ┗ README.md
```

## 파일 역할

- `app.py`: Flask 서버 실행 파일이며 `/predict` POST API를 제공합니다.
- `train_model.py`: 건강검진 데이터 전처리, 학습 데이터 분리, 모델 학습, 모델 저장을 담당합니다.
- `obesity_model.pkl`: 학습이 완료된 Scikit-learn 모델 파일입니다.
- `requirements.txt`: Flask AI 서버 실행에 필요한 Python 패키지 목록입니다.
- `data/`: 건강검진 원본 데이터 또는 학습용 CSV 파일을 저장하는 폴더입니다.

## 구현 단계

### 1단계 - 데이터 전처리 및 모델 학습

- 건강검진 CSV 데이터를 로드합니다.
- 키와 몸무게 컬럼을 숫자형으로 변환합니다.
- 결측치와 이상치를 제거합니다.
- BMI를 계산하여 비만 여부 라벨을 생성합니다.
- 학습 데이터와 테스트 데이터를 분리합니다.
- Scikit-learn 모델을 학습합니다.
- 학습된 모델을 `joblib`으로 저장합니다.

### 2단계 - Flask 서버 구현

- Flask 애플리케이션을 생성합니다.
- `flask-cors`를 적용해 Spring Boot 서버 연동 시 CORS 문제를 줄입니다.
- 저장된 모델 파일을 로드합니다.

### 3단계 - 예측 API 구현

- `/predict` POST API를 생성합니다.
- JSON 요청으로 `height`, `weight`를 전달받습니다.
- 입력값을 검증합니다.
- 모델 예측 결과를 JSON으로 반환합니다.

### 4단계 - Spring Boot 연동 형태 정리

- Spring Boot 서버가 호출할 API URL과 요청/응답 JSON 형식을 문서화합니다.
- 예측 성공, 입력값 오류, 서버 오류 응답 형태를 정리합니다.

## API 요청/응답 예정 형식

### 요청

```json
{
  "height": 170,
  "weight": 75
}
```

### 응답

```json
{
  "obese": true,
  "result": "비만",
  "bmi": 25.95
}
```
