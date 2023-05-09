package ru.volsu.course.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.volsu.course.model.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {
}
