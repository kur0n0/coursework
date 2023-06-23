package ru.volsu.course.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.volsu.course.dao.ArticleRepository;
import ru.volsu.course.model.Article;
import ru.volsu.course.model.ArticleDto;
import ru.volsu.course.model.ArticleFilesDto;
import ru.volsu.course.model.FileDto;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ArticleServiceImpl implements ArticleService {

    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private FileService fileService;

    @Override
    public Article findById(Integer articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new NoSuchElementException(String.format("Статья с id: %s не найдена", articleId)));
        log.debug("Получена статья с id: " + article);
        return article;
    }

    @Override
    public ArticleDto getOne(Integer articleId) throws Exception {
        Article article = findById(articleId);
        List<FileDto> files = fileService.getFiles(article.getFilesUuidList(), articleId);
        return new ArticleDto(article, files);
    }

    @Override
    @Transactional
    public void save(Article article, MultipartFile[] files) throws Exception {
        article = articleRepository.save(article);
        log.debug("Сохранена статья с id: " + article.getArticleId());

        List<String> fileUuidList = new ArrayList<>();
        for (MultipartFile file : files) {
            fileUuidList.add(fileService.createFile(file, article.getArticleId()));
        }

        article.setFilesUuidList(fileUuidList);
        articleRepository.save(article);
    }

    @Override
    public void delete(Integer articleId) {
        articleRepository.deleteById(articleId);
        log.debug("Удалена статья с id: " + articleId);
    }

    @Override
    public Page<Article> findAll(PageRequest pageRequest) {
        return articleRepository.findAll(pageRequest);
    }

    @Override
    public ArticleFilesDto findByTag(String tag, PageRequest pageRequest) throws Exception {
        Page<Article> articlePage = articleRepository.findByTag(tag, pageRequest);
        List<ArticleDto> articleDtoList = new ArrayList<>();
        for (Article article : articlePage.getContent()) {
            List<FileDto> files = fileService.getFiles(article.getFilesUuidList(), article.getArticleId());
            articleDtoList.add(new ArticleDto(article, files));
        }

        return new ArticleFilesDto(articleDtoList, articlePage.getNumber(), articlePage.getTotalPages());
    }
}
