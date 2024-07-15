package ru.volsu.course.article.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.volsu.course.article.model.Article;

import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {
    Page<Article> findByTagContaining(String tag, Pageable pageable);

    Page<Article> findByTitleContaining(String title, Pageable pageRequest);

    Optional<Article> findByTitle(String articleTitleHint);
}
