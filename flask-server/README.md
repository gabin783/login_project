# Flask AI Server

비만 여부 예측을 담당하는 Flask 기반 AI 서버입니다.

## 폴더 구조

```text
flask-server/
 ┣ app.py
 ┣ train_model.py
 ┣ obesity_model.pkl
 ┣ requirements.txt
 ┗ data/
```

## 1단계 - 모델 학습

`data/health_checkup.csv` 파일을 준비한 뒤 아래 명령으로 모델을 학습합니다.

```bash
python train_model.py
```

학습이 완료되면 `obesity_model.pkl` 파일이 생성됩니다.

## Flask 서버 실행

```bash
python app.py
```

서버가 실행되면 `http://localhost:5000/predict`에서 POST 방식으로 예측 API를 사용할 수 있습니다.

학습된 `obesity_model.pkl`이 있으면 모델 기반 예측을 사용하고, 모델 파일이 아직 없으면 BMI 기준으로 임시 예측을 반환합니다.
