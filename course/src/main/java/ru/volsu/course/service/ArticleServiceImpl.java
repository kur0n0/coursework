package ru.volsu.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.volsu.course.dao.ArticleRepository;
import ru.volsu.course.model.Article;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public Article findById(Integer articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new NoSuchElementException(String.format("Статья с id: %s не найдена", articleId)));
    }

    @Override
    public void save(Article article) {
        articleRepository.save(article);
    }

    @Override
    public void delete(Integer articleId) {
        articleRepository.deleteById(articleId);
    }

    @Override
    public List<Article> findAll() {
        return articleRepository.findAll();
    }
}
