package ru.volsu.course.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.volsu.course.dao.ArticleRepository;
import ru.volsu.course.model.Article;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ArticleServiceImpl implements ArticleService {

    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public Article findById(Integer articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new NoSuchElementException(String.format("Статья с id: %s не найдена", articleId)));
        log.debug("Получена статья с id: " + article);
        return article;
    }

    @Override
    public void save(Article article) {
        article = articleRepository.save(article);
        log.debug("Сохранена статья с id: " + article.getArticleId());
    }

    @Override
    public void delete(Integer articleId) {
        articleRepository.deleteById(articleId);
        log.debug("Удалена статья с id: " + articleId);
    }

    @Override
    public List<Article> findAll() {
        return articleRepository.findAll();
    }
}
