package ru.volsu.coursebot.service;

import ru.volsu.commons.dto.ArticlePageDto;
import ru.volsu.commons.dto.TaskDTO;
import ru.volsu.coursebot.exceptions.CoreException;

import java.util.Optional;

public interface CourseCoreService {
    ArticlePageDto getPageByTag(Integer page, String tag) throws CoreException;

    ArticlePageDto getPageByTitle(Integer page, String title) throws CoreException;

    ArticlePageDto getPageFullTextSearch(Integer page, String query) throws CoreException;

    Optional<TaskDTO> getRandomNotSolvedTask(String userName) throws CoreException;

    void createTaskHistory(Long taskId, String userName, String userAnswer);

    void createSolvedTask(String userName, Long taskId);
}
