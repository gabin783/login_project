package com.example.loginproject.repository;

import com.example.loginproject.domain.Question;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @EntityGraph(attributePaths = "author")
    List<Question> findAllByOrderByCreatedAtDesc();

    @Query("select q from Question q left join fetch q.author where q.id = :id")
    Optional<Question> findByIdWithAuthor(@Param("id") Long id);
}
