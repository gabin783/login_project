package com.example.loginproject.controller;

import com.example.loginproject.dto.PredictRequestDto;
import com.example.loginproject.dto.PredictResponseDto;
import com.example.loginproject.service.PredictService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/predict")
public class PredictController {

    private final PredictService predictService;

    @GetMapping
    public String predictForm(Model model) {
        model.addAttribute("predictRequestDto", new PredictRequestDto());
        return "predict/form";
    }

    @PostMapping
    public String predict(
            @Valid @ModelAttribute PredictRequestDto predictRequestDto,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "predict/form";
        }

        try {
            PredictResponseDto predictResponseDto = predictService.predict(predictRequestDto);
            model.addAttribute("request", predictRequestDto);
            model.addAttribute("response", predictResponseDto);
            return "predict/result";
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "predict/form";
        }
    }
}
