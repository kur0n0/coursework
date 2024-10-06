package ru.volsu.course.article.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;
import ru.volsu.commons.dto.ArticleDto;
import ru.volsu.commons.dto.ArticlePageDto;
import ru.volsu.course.article.model.Article;

import java.util.Optional;

public interface ArticleService {

    Article findById(Integer articleId);

    ArticleDto getOne(Integer articleId) throws Exception;

    void save(Article article, MultipartFile[] files) throws Exception;

    void delete(Integer articleId);

    Page<Article> findAll(PageRequest pageRequest);

    ArticlePageDto findByTag(String tag, PageRequest pageRequest) throws Exception;

    ArticlePageDto findByTitle(String title, PageRequest pageRequest) throws Exception;

    ArticlePageDto fullTextSearch(String query, PageRequest pageRequest) throws Exception;

    Optional<Article> findByTitle(String articleTitleHint);

    ArticlePageDto findByTagConcurrently(String tag, PageRequest pageRequest) throws Exception;
}
