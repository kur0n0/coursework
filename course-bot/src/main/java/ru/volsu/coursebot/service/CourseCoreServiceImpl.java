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

import java.util.Arrays;
import java.util.List;

@Service
public class CourseCoreServiceImpl implements CourseCoreService {

    @Autowired
    @Qualifier(value = "coreTemplate")
    private RestTemplate coreTemplate;

    Logger log = LoggerFactory.getLogger(getClass());

    private final Integer pageSize = 1;

    @Override
    public List<ArticleDto> getPageByTag(Integer page, String tag) throws Exception {
        ResponseEntity<ArticleDto[]> responseEntity;
        try {
            responseEntity = coreTemplate.getForEntity("/api/article/page?page={page}&size={pageSize}&tag={tag}", ArticleDto[].class, page, pageSize, tag);
        } catch (RestClientException e) {
            log.error("Ошибка при получении старницы статей: {}", e.getMessage());
            throw new Exception("Ошибка при получении старницы статей: " + e.getMessage());
        }

        return Arrays.asList(responseEntity.getBody());
    }
}
