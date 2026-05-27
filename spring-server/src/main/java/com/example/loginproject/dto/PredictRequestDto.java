package com.example.loginproject.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PredictRequestDto {

    @NotNull(message = "키를 입력해주세요.")
    @DecimalMin(value = "100.0", message = "키는 100cm 이상으로 입력해주세요.")
    @DecimalMax(value = "230.0", message = "키는 230cm 이하로 입력해주세요.")
    private Double height;

    @NotNull(message = "몸무게를 입력해주세요.")
    @DecimalMin(value = "30.0", message = "몸무게는 30kg 이상으로 입력해주세요.")
    @DecimalMax(value = "250.0", message = "몸무게는 250kg 이하로 입력해주세요.")
    private Double weight;
}
