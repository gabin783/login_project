package com.example.loginproject.service;

import com.example.loginproject.dto.PredictRequestDto;
import com.example.loginproject.dto.PredictResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class PredictService {

    private static final String FLASK_PREDICT_URL = "http://localhost:5000/predict";

    private final RestTemplate restTemplate;

    public PredictResponseDto predict(PredictRequestDto requestDto) {
        try {
            ResponseEntity<PredictResponseDto> response = restTemplate.postForEntity(
                    FLASK_PREDICT_URL,
                    requestDto,
                    PredictResponseDto.class
            );

            PredictResponseDto responseBody = response.getBody();
            if (responseBody == null || responseBody.getResult() == null) {
                throw new IllegalStateException("Flask 서버에서 예측 결과를 받지 못했습니다.");
            }

            return responseBody;
        } catch (RestClientException e) {
            throw new IllegalStateException("Flask 예측 서버와 통신할 수 없습니다.", e);
        }
    }
}
