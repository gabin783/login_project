from pathlib import Path

import joblib
import pandas as pd
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import accuracy_score, classification_report
from sklearn.model_selection import train_test_split
from sklearn.pipeline import Pipeline
from sklearn.preprocessing import StandardScaler


BASE_DIR = Path(__file__).resolve().parent
DATA_PATH = BASE_DIR / "data" / "health_checkup.csv"
MODEL_PATH = BASE_DIR / "obesity_model.pkl"

HEIGHT_COLUMN_CANDIDATES = [
    "height",
    "Height",
    "HEIGHT",
    "키",
    "신장",
    "신장(5Cm단위)",
    "신장(5cm단위)",
]

WEIGHT_COLUMN_CANDIDATES = [
    "weight",
    "Weight",
    "WEIGHT",
    "몸무게",
    "체중",
    "체중(5Kg단위)",
    "체중(5kg단위)",
]

BMI_OBESITY_THRESHOLD = 25.0


def find_column(df: pd.DataFrame, candidates: list[str]) -> str:
    for candidate in candidates:
        if candidate in df.columns:
            return candidate

    raise ValueError(f"CSV 파일에서 다음 컬럼 중 하나를 찾을 수 없습니다: {candidates}")


def load_data(path: Path) -> pd.DataFrame:
    if not path.exists():
        raise FileNotFoundError(
            f"학습 데이터 파일을 찾을 수 없습니다: {path}\n"
            "data/health_checkup.csv 파일을 추가한 뒤 다시 실행해주세요."
        )

    return pd.read_csv(path)


def preprocess_data(df: pd.DataFrame) -> tuple[pd.DataFrame, pd.Series]:
    height_column = find_column(df, HEIGHT_COLUMN_CANDIDATES)
    weight_column = find_column(df, WEIGHT_COLUMN_CANDIDATES)

    data = df[[height_column, weight_column]].copy()
    data.columns = ["height", "weight"]

    data["height"] = pd.to_numeric(data["height"], errors="coerce")
    data["weight"] = pd.to_numeric(data["weight"], errors="coerce")
    data = data.dropna(subset=["height", "weight"])

    data = data[(data["height"] >= 100) & (data["height"] <= 230)]
    data = data[(data["weight"] >= 30) & (data["weight"] <= 250)]

    height_meter = data["height"] / 100
    data["bmi"] = data["weight"] / (height_meter ** 2)
    data["is_obese"] = (data["bmi"] >= BMI_OBESITY_THRESHOLD).astype(int)

    features = data[["height", "weight"]]
    target = data["is_obese"]

    if len(features) < 10:
        raise ValueError("학습에 사용할 데이터가 너무 적습니다. 최소 10개 이상의 데이터가 필요합니다.")

    return features, target


def train_model(features: pd.DataFrame, target: pd.Series) -> Pipeline:
    x_train, x_test, y_train, y_test = train_test_split(
        features,
        target,
        test_size=0.2,
        random_state=42,
        stratify=target,
    )

    model = Pipeline(
        steps=[
            ("scaler", StandardScaler()),
            (
                "classifier",
                RandomForestClassifier(
                    n_estimators=100,
                    random_state=42,
                    class_weight="balanced",
                ),
            ),
        ]
    )

    model.fit(x_train, y_train)

    y_pred = model.predict(x_test)
    accuracy = accuracy_score(y_test, y_pred)

    print(f"테스트 정확도: {accuracy:.4f}")
    print(classification_report(y_test, y_pred, target_names=["정상", "비만"]))

    return model


def save_model(model: Pipeline, path: Path) -> None:
    joblib.dump(model, path)
    print(f"모델 저장 완료: {path}")


def main() -> None:
    raw_data = load_data(DATA_PATH)
    features, target = preprocess_data(raw_data)
    model = train_model(features, target)
    save_model(model, MODEL_PATH)


if __name__ == "__main__":
    main()
