package com.example.loginproject.controller;

import com.example.loginproject.domain.Member;
import com.example.loginproject.domain.Question;
import com.example.loginproject.dto.QuestionRequest;
import com.example.loginproject.service.MemberService;
import com.example.loginproject.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionService questionService;
    private final MemberService memberService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("questions", questionService.findAll());
        return "questions/list";
    }

    @GetMapping("/{id}")
    public String detail(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model
    ) {
        Question question = questionService.findById(id);
        Member loginMember = getLoginMember(userDetails);

        model.addAttribute("question", question);
        model.addAttribute("canManage", questionService.canManage(question, loginMember));
        return "questions/detail";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("questionRequest", new QuestionRequest());
        return "questions/new";
    }

    @PostMapping
    public String create(
            @Valid @ModelAttribute QuestionRequest questionRequest,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        if (bindingResult.hasErrors()) {
            return "questions/new";
        }

        Member loginMember = getLoginMember(userDetails);
        Long questionId = questionService.create(questionRequest, loginMember);
        return "redirect:/questions/" + questionId;
    }

    @GetMapping("/{id}/edit")
    public String editForm(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model
    ) {
        Question question = questionService.findById(id);
        Member loginMember = getLoginMember(userDetails);

        if (!questionService.canManage(question, loginMember)) {
            return "redirect:/questions/" + id;
        }

        QuestionRequest questionRequest = new QuestionRequest();
        questionRequest.setTitle(question.getTitle());
        questionRequest.setContent(question.getContent());

        model.addAttribute("question", question);
        model.addAttribute("questionRequest", questionRequest);
        return "questions/edit";
    }

    @PostMapping("/{id}/edit")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute QuestionRequest questionRequest,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("question", questionService.findById(id));
            return "questions/edit";
        }

        Member loginMember = getLoginMember(userDetails);
        questionService.update(id, questionRequest, loginMember);
        return "redirect:/questions/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Member loginMember = getLoginMember(userDetails);
        questionService.delete(id, loginMember);
        return "redirect:/questions";
    }

    private Member getLoginMember(UserDetails userDetails) {
        return memberService.findByUsername(userDetails.getUsername());
    }
}
