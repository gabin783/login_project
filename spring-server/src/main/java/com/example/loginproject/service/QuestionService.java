package com.example.loginproject.service;

import com.example.loginproject.domain.Member;
import com.example.loginproject.domain.Question;
import com.example.loginproject.domain.Role;
import com.example.loginproject.dto.QuestionRequest;
import com.example.loginproject.repository.QuestionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionService {

    private final QuestionRepository questionRepository;

    public List<Question> findAll() {
        return questionRepository.findAllByOrderByCreatedAtDesc();
    }

    public Question findById(Long id) {
        return questionRepository.findByIdWithAuthor(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
    }

    @Transactional
    public Long create(QuestionRequest request, Member author) {
        Question question = Question.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .author(author)
                .build();

        return questionRepository.save(question).getId();
    }

    @Transactional
    public void update(Long id, QuestionRequest request, Member loginMember) {
        Question question = findById(id);
        validateManagePermission(question, loginMember);
        question.update(request.getTitle(), request.getContent());
    }

    @Transactional
    public void delete(Long id, Member loginMember) {
        Question question = findById(id);
        validateManagePermission(question, loginMember);
        questionRepository.delete(question);
    }

    public boolean canManage(Question question, Member loginMember) {
        if (question.isWrittenBy(loginMember)) {
            return true;
        }

        return !question.hasAuthor() && isAdmin(loginMember);
    }

    private void validateManagePermission(Question question, Member loginMember) {
        if (!canManage(question, loginMember)) {
            throw new AccessDeniedException("게시글 수정/삭제 권한이 없습니다.");
        }
    }

    private boolean isAdmin(Member member) {
        return member != null && member.getRole() == Role.ADMIN;
    }
}
