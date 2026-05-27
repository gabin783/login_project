package com.example.loginproject.controller;

import com.example.loginproject.dto.MemberJoinRequest;
import com.example.loginproject.service.MemberService;
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
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/join")
    public String joinForm(Model model) {
        model.addAttribute("memberJoinRequest", new MemberJoinRequest());
        return "members/join";
    }

    @PostMapping("/join")
    public String join(
            @Valid @ModelAttribute MemberJoinRequest memberJoinRequest,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "members/join";
        }

        try {
            memberService.join(memberJoinRequest);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("joinFailed", e.getMessage());
            return "members/join";
        }

        return "redirect:/";
    }
}
