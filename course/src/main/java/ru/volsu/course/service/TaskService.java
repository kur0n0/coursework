package ru.volsu.course.service;

import ru.volsu.course.dto.TaskDTO;

import java.util.Optional;

public interface TaskService {
    Optional<TaskDTO> findRandomNotCorrectTask(String username) throws Exception;

    void saveHistory(Long taskId, String username, String userAnswer);

    void createSolvedTask(String username, Long taskId);
}
