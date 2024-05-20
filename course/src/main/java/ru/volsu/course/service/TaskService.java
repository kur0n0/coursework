package ru.volsu.course.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.volsu.course.dto.TaskDTO;
import ru.volsu.course.model.Task;

import java.util.Optional;

public interface TaskService {
    Optional<TaskDTO> findRandomNotCorrectTask(String username) throws Exception;

    void saveHistory(Long taskId, String username, String userAnswer);

    void createSolvedTask(String username, Long taskId);

    Page<Task> findAll(PageRequest pageRequest);

    void delete(Long taskId);

    void save(Task task);
}
