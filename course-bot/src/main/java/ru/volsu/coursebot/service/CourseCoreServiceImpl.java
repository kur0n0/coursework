package ru.volsu.coursebot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.volsu.coursebot.dto.ArticlePage;
import ru.volsu.coursebot.dto.TaskDTO;
import ru.volsu.coursebot.exceptions.CoreException;

import java.util.Optional;

@Service
public class CourseCoreServiceImpl implements CourseCoreService {

    @Autowired
    @Qualifier(value = "coreTemplate")
    private RestTemplate coreTemplate;

    Logger log = LoggerFactory.getLogger(getClass());

    private final Integer pageSize = 3;

    @Override
    public ArticlePage getPageByTag(Integer page, String tag) throws CoreException {
        ResponseEntity<ArticlePage> responseEntity;
        try {
            responseEntity = coreTemplate.getForEntity("/api/article/tag/page?page={page}&size={pageSize}&tag={tag}",
                    ArticlePage.class,
                    page,
                    pageSize,
                    tag);
        } catch (RestClientException e) {
            log.error("Сетевая ошибка при получении страницы статей: {}", e.getMessage());
            throw new CoreException("Сетевая ошибка при получении страницы статей: " + e.getMessage());
        }

        return responseEntity.getBody();
    }

    @Override
    public ArticlePage getPageByTitle(Integer page, String title) throws CoreException {
        ResponseEntity<ArticlePage> responseEntity;
        try {
            responseEntity = coreTemplate.getForEntity("/api/article/title/page?page={page}&size={pageSize}&title={title}",
                    ArticlePage.class,
                    page,
                    pageSize,
                    title);
        } catch (RestClientException e) {
            log.error("Сетевая ошибка при получении страницы статей: {}", e.getMessage());
            throw new CoreException("Сетевая ошибка при получении страницы статей: " + e.getMessage());
        }

        return responseEntity.getBody();
    }

    @Override
    public ArticlePage getPageFullTextSearch(Integer page, String query) throws CoreException {
        ResponseEntity<ArticlePage> responseEntity;
        try {
            responseEntity = coreTemplate.getForEntity("/api/article/page?query={query}&page={page}&size={pageSize}",
                    ArticlePage.class,
                    query,
                    page,
                    pageSize);
        } catch (RestClientException e) {
            log.error("Сетевая ошибка при получении страницы статей: {}", e.getMessage());
            throw new CoreException("Сетевая ошибка при получении страницы статей: " + e.getMessage());
        }

        return responseEntity.getBody();
    }

    @Override
    public Optional<TaskDTO> getRandomNotSolvedTask(String userName) throws CoreException {
        ResponseEntity<TaskDTO> responseEntity;
        try {
            responseEntity = coreTemplate.getForEntity("/api/task/random?username={userName}", TaskDTO.class, userName);
        } catch (RestClientException e) {
            log.error("Сетевая ошибка при получении задания: {}", e.getMessage());
            throw new CoreException("Сетевая ошибка при получении задания: " + e.getMessage());
        }

        return Optional.ofNullable(responseEntity.getBody());
    }

    @Override
    public void createTaskHistory(Long taskId, String userName, String userAnswer) {
        coreTemplate.postForObject("/api/task/history?taskId={taskId}&username={userName}&userAnswer={userAnswer}",
                null,
                Void.class,
                taskId,
                userName,
                userAnswer);
    }

    @Override
    public void createSolvedTask(String userName, Long taskId) {
        coreTemplate.postForObject("/api/task/solved?username={username}&taskId={taskId}",
                null,
                Void.class,
                userName,
                taskId);
    }
}
