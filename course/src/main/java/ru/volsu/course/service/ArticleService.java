package ru.volsu.course.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;
import ru.volsu.course.model.Article;
import ru.volsu.course.model.ArticleDto;

import java.util.List;

public interface ArticleService {

    Article findById(Integer articleId);

    ArticleDto getOne(Integer articleId) throws Exception;

    void save(Article article, MultipartFile[] files) throws Exception;

    void delete(Integer articleId);

    Page<Article> findAll(PageRequest pageRequest);

    Page<Article> findAllByTag(String tag, PageRequest pageRequest);
}
