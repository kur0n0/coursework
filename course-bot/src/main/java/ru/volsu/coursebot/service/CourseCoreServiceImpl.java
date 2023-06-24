package ru.volsu.coursebot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.volsu.coursebot.dto.ArticleDto;
import ru.volsu.coursebot.dto.ArticlePage;

import java.util.Arrays;
import java.util.List;

@Service
public class CourseCoreServiceImpl implements CourseCoreService {

    @Autowired
    @Qualifier(value = "coreTemplate")
    private RestTemplate coreTemplate;

    Logger log = LoggerFactory.getLogger(getClass());

    private final Integer pageSize = 3;

    @Override
    public ArticlePage getPageByTag(Integer page, String tag) throws Exception {
        ResponseEntity<ArticlePage> responseEntity;
        try {
            responseEntity = coreTemplate.getForEntity("/api/article/tag/page?page={page}&size={pageSize}&tag={tag}", ArticlePage.class, page, pageSize, tag);
        } catch (RestClientException e) {
            log.error("Ошибка при получении старницы статей: {}", e.getMessage());
            throw new Exception("Ошибка при получении старницы статей: " + e.getMessage());
        }

        return responseEntity.getBody();
    }

    @Override
    public ArticlePage getPageByTitle(Integer page, String title) throws Exception {
        ResponseEntity<ArticlePage> responseEntity;
        try {
            responseEntity = coreTemplate.getForEntity("/api/article/title/page?page={page}&size={pageSize}&title={title}", ArticlePage.class, page, pageSize, title);
        } catch (RestClientException e) {
            log.error("Ошибка при получении старницы статей: {}", e.getMessage());
            throw new Exception("Ошибка при получении старницы статей: " + e.getMessage());
        }

        return responseEntity.getBody();
    }
}
