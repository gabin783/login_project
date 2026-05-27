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
