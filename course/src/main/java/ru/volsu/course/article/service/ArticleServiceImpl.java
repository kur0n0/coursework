package ru.volsu.course.article.service;

import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.volsu.commons.dto.ArticleDto;
import ru.volsu.commons.dto.ArticlePageDto;
import ru.volsu.commons.dto.FileDto;
import ru.volsu.course.article.dao.ArticleRepository;
import ru.volsu.course.article.model.Article;
import ru.volsu.course.article.model.ArticleMapper;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ArticleServiceImpl implements ArticleService {

    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ArticleMapper articleMapper;

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

        return articleMapper.toDto(article, files);
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
    public ArticlePageDto findByTag(String tag, PageRequest pageRequest) throws Exception {
        Page<Article> articlePage = articleRepository.findByTagContaining(tag, pageRequest);
        return getArticleFilesDto(articlePage);
    }

    @Override
    public ArticlePageDto findByTitle(String title, PageRequest pageRequest) throws Exception { // todo: подумать, как можно убрать дублирование кода, возможно добавить спецификацию для запроса в бд с пагинацией
        Page<Article> articlePage = articleRepository.findByTitleContaining(title, pageRequest);
        return getArticleFilesDto(articlePage);
    }

    @Override
    public ArticlePageDto fullTextSearch(String queryString, PageRequest pageRequest) throws Exception {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

        QueryBuilder queryBuilder = fullTextEntityManager
                .getSearchFactory()
                .buildQueryBuilder()
                .forEntity(Article.class)
                .get();

        Query query = queryBuilder.keyword()
                .onFields("text", "title", "tag")
                .matching(queryString)
                .createQuery();

        Integer page = pageRequest.getPageNumber() > 0 ? pageRequest.getPageNumber() : 1;
        Integer size = pageRequest.getPageSize() > 0 ? pageRequest.getPageSize() : 10;

        Sort sorting = new Sort(SortField.FIELD_SCORE);

        FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(query, Article.class);
        fullTextQuery.setFirstResult((page - 1) * size);
        fullTextQuery.setMaxResults(size);
        fullTextQuery.setSort(sorting);

        List<Article> resultList = fullTextQuery.getResultList();
        Integer totalResult = fullTextQuery.getResultSize();

        Integer totalPages = totalResult / size + (totalResult % size == 0 ? 0 : 1);

        return getArticleFilesDto(resultList, page, totalPages);
    }

    @Override
    public Optional<Article> findByTitle(String articleTitleHint) {
        return articleRepository.findByTitle(articleTitleHint);
    }

    private ArticlePageDto getArticleFilesDto(Page<Article> articlePage) throws Exception {
        List<ArticleDto> articleDtoList = new ArrayList<>();
        for (Article article : articlePage.getContent()) {
            List<FileDto> files = fileService.getFiles(article.getFilesUuidList(), article.getArticleId());
            articleDtoList.add(articleMapper.toDto(article, files));
        }

        return articleMapper.toDtoPage(articleDtoList, articlePage.getNumber(), articlePage.getTotalPages());
    }

    private ArticlePageDto getArticleFilesDto(List<Article> articles, Integer currentPage, Integer totalPages) throws Exception {
        List<ArticleDto> articleDtoList = new ArrayList<>();
        for (Article article : articles) {
            List<FileDto> files = fileService.getFiles(article.getFilesUuidList(), article.getArticleId());
            articleDtoList.add(articleMapper.toDto(article, files));
        }

        return articleMapper.toDtoPage(articleDtoList, currentPage, totalPages);
    }
}
