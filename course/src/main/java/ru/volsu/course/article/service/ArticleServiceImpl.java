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
import ru.volsu.course.common.dto.InnerFileDto;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

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
        List<InnerFileDto> innerFileDtoList = new ArrayList<>();
        for (MultipartFile file : files) {
            innerFileDtoList.add(new InnerFileDto(file));
        }

        final Article articleToSave = article;
        CompletableFuture.supplyAsync(() -> {
            log.debug("Сохранена статья с id: " + articleToSave.getArticleId());
            return articleRepository.save(articleToSave);
        }).thenCombine(createFiles(innerFileDtoList, article.getArticleId()), (art, fileList) -> {
            art.setFilesUuidList(fileList);
            return articleRepository.save(art);
        }).handle((res, ex) -> {
            if (ex != null) {
                throw new RuntimeException(ex);
            }
            return null;
        });


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
        long start = System.currentTimeMillis();
        Page<Article> articlePage = articleRepository.findByTagContaining(tag, pageRequest);
        ArticlePageDto articleFilesDto = getArticleFilesDto(articlePage);
        long end = System.currentTimeMillis();
        log.info("Выполнение метода заняло: {} мс", end - start);

        return articleFilesDto;
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

    @Override
    public ArticlePageDto findByTagConcurrently(String tag, PageRequest pageRequest) {
        Page<Article> articlePage = articleRepository.findByTagContaining(tag, pageRequest);
        List<CompletionStage<ArticleDto>> articleDtos = articlePage.getContent().stream()
                .map(article -> getFiles(article).thenApply(list -> articleMapper.toDto(article, list))
                ).toList();

        return articleMapper.toDtoPage(
                CompletableFuture.allOf(articleDtos.toArray(new CompletableFuture[articleDtos.size()]))
                        .thenApply(v -> articleDtos.stream()
                                .map(CompletionStage::toCompletableFuture)
                                .map(CompletableFuture::join).toList()
                        ).join(), articlePage.getNumber(), articlePage.getTotalPages()
        );
    }

    private CompletionStage<List<FileDto>> getFiles(Article article) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return fileService.getFiles(article.getFilesUuidList(), article.getArticleId());
            } catch (Exception e) {
                throw new RuntimeException("Ошибка при получении файлов: " + e);
            }
        }).exceptionally(e -> Collections.emptyList());
    }

    private CompletionStage<List<String>> createFiles(List<InnerFileDto> innerFileDtoList, Integer articleId) {
        return CompletableFuture.supplyAsync(() -> {
            List<String> result = new ArrayList<>();
            for (InnerFileDto file : innerFileDtoList) {
                try {
                    result.add(fileService.createFile(file, articleId));
                } catch (Exception e) {
                    throw new RuntimeException("Ошибка при создании файлов: " + e);
                }
            }
            return result;
        }).exceptionally((e) -> {
            log.error("Ошибка: ", e);
            return Collections.emptyList();
        });
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
