package ru.volsu.coursebot.service;

import ru.volsu.coursebot.dto.ArticlePage;
import ru.volsu.coursebot.dto.TaskDTO;
import ru.volsu.coursebot.exceptions.CoreException;

import java.util.Optional;

public interface CourseCoreService {
    ArticlePage getPageByTag(Integer page, String tag) throws CoreException;

    ArticlePage getPageByTitle(Integer page, String title) throws CoreException;

    ArticlePage getPageFullTextSearch(Integer page, String query) throws CoreException;

    Optional<TaskDTO> getRandomNotSolvedTask(String userName) throws CoreException;

    void createTaskHistory(Long taskId, String userName, String userAnswer);

    void createSolvedTask(String userName, Long taskId);
}
