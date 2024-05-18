package ru.volsu.course.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;
import ru.volsu.course.model.Article;
import ru.volsu.course.dto.ArticleDto;
import ru.volsu.course.dto.ArticleFilesDto;

import java.util.Optional;

public interface ArticleService {

    Article findById(Integer articleId);

    ArticleDto getOne(Integer articleId) throws Exception;

    void save(Article article, MultipartFile[] files) throws Exception;

    void delete(Integer articleId);

    Page<Article> findAll(PageRequest pageRequest);

    ArticleFilesDto findByTag(String tag, PageRequest pageRequest) throws Exception;

    ArticleFilesDto findByTitle(String title, PageRequest pageRequest) throws Exception;

    ArticleFilesDto fullTextSearch(String query, PageRequest pageRequest) throws Exception;

    Optional<Article> findByTitle(String articleTitleHint);
}
