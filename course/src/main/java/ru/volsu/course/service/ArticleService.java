package ru.volsu.course.service;

import ru.volsu.course.model.Article;

import java.util.List;

public interface ArticleService {
    
    Article findById(Integer articleId);

    void save(Article article);

    void delete(Integer articleId);

    List<Article> findAll();
}
