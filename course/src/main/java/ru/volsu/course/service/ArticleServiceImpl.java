package ru.volsu.course.service;

import netscape.javascript.JSObject;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import ru.volsu.course.dao.ArticleRepository;
import ru.volsu.course.model.Article;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class ArticleServiceImpl implements ArticleService {

    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    @Qualifier(value = "fileTemplate")
    private RestTemplate restTemplate;

    @Override
    public Article findById(Integer articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new NoSuchElementException(String.format("Статья с id: %s не найдена", articleId)));
        log.debug("Получена статья с id: " + article);
        return article;
    }

    @Override
    @Transactional
    public void save(Article article, MultipartFile[] files) throws Exception {
        article = articleRepository.save(article);
        log.debug("Сохранена статья с id: " + article.getArticleId());

        List<String> fileUuidList = new ArrayList<>();
        for (MultipartFile file : files) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> map= new LinkedMultiValueMap<>();
            map.add("title", Optional.ofNullable(file.getOriginalFilename())
                    .orElse(UUID.randomUUID().toString()));
            map.add("file",  new ByteArrayResource(file.getBytes()));

            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);

            ResponseEntity<String> responseEntity;
            try {
                responseEntity = restTemplate.postForEntity("/api/file", request, String.class);
            } catch (RestClientException e) {
                log.error("Сетевая ошибка при запросе создания файла: {}", e.getMessage());
                throw new Exception("Сетевая ошибка при запросе создания файла: " + e.getMessage()); // todo создать свое исключение
            }

            String body = responseEntity.getBody();
            String uuid;
            try {
                JSONObject jsonObject = new JSONObject(body);
                uuid = jsonObject.getString("uuid");
            } catch (JSONException e) {
                log.error("Ошибка разбора ответа от файлового сервиса: {}", e.getMessage());
                throw new Exception("Ошибка разбора ответа от файлового сервиса: " + e.getMessage()); // todo создать свое исключение
            }

            log.debug("Создан файл: {} для статьи: {}", uuid, article.getArticleId());
            fileUuidList.add(uuid);
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
    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    @Override
    public Page<Article> findAll(PageRequest pageRequest) {
        return articleRepository.findAll(pageRequest);
    }
}
