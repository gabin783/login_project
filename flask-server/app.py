from pathlib import Path

import joblib
import pandas as pd
from flask import Flask, jsonify, request
from flask_cors import CORS


BASE_DIR = Path(__file__).resolve().parent
MODEL_PATH = BASE_DIR / "obesity_model.pkl"
BMI_OBESITY_THRESHOLD = 25.0

app = Flask(__name__)
CORS(app)

model = joblib.load(MODEL_PATH) if MODEL_PATH.exists() else None


def parse_measurement(payload: dict, key: str) -> float:
    value = payload.get(key)
    if value is None:
        raise ValueError(f"{key} 값이 필요합니다.")

    try:
        return float(value)
    except (TypeError, ValueError) as exc:
        raise ValueError(f"{key} 값은 숫자여야 합니다.") from exc


def validate_input(height: float, weight: float) -> None:
    if not 100 <= height <= 230:
        raise ValueError("height 값은 100cm 이상 230cm 이하로 입력해주세요.")

    if not 30 <= weight <= 250:
        raise ValueError("weight 값은 30kg 이상 250kg 이하로 입력해주세요.")


def calculate_bmi(height: float, weight: float) -> float:
    height_meter = height / 100
    return weight / (height_meter ** 2)


def predict_result(height: float, weight: float) -> str:
    if model is None:
        bmi = calculate_bmi(height, weight)
        return "비만" if bmi >= BMI_OBESITY_THRESHOLD else "정상"

    features = pd.DataFrame([{"height": height, "weight": weight}])
    prediction = model.predict(features)[0]
    return "비만" if int(prediction) == 1 else "정상"


@app.get("/")
def health_check():
    return jsonify({"status": "ok", "modelLoaded": model is not None})


@app.post("/predict")
def predict():
    try:
        payload = request.get_json(silent=True) or {}
        height = parse_measurement(payload, "height")
        weight = parse_measurement(payload, "weight")
        validate_input(height, weight)

        result = predict_result(height, weight)

        return jsonify({"result": result})
    except ValueError as exc:
        return jsonify({"error": str(exc)}), 400
    except Exception:
        return jsonify({"error": "예측 처리 중 서버 오류가 발생했습니다."}), 500


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000, debug=True, use_reloader=False)
