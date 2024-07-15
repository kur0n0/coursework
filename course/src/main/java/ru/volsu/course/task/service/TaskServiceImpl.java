package ru.volsu.course.task.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.volsu.commons.dto.ArticleDto;
import ru.volsu.commons.dto.TaskDTO;
import ru.volsu.course.article.service.ArticleService;
import ru.volsu.course.task.dao.SolvedTaskRepository;
import ru.volsu.course.task.dao.TaskHistoryRepository;
import ru.volsu.course.task.dao.TaskRepository;
import ru.volsu.course.task.model.TaskMapper;
import ru.volsu.course.task.model.SolvedTask;
import ru.volsu.course.task.model.Task;
import ru.volsu.course.task.model.TaskHistory;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository repository;

    @Autowired
    private TaskHistoryRepository historyRepository;

    @Autowired
    private SolvedTaskRepository solvedTaskRepository;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private TaskMapper taskMapper;

    @Override
    public Optional<TaskDTO> findRandomNotCorrectTask(String username) throws Exception {
        Optional<Task> randomNotCorrectTaskOpt = repository.findRandomNotSolvedTask(username);
        if (randomNotCorrectTaskOpt.isEmpty()) {
            return Optional.empty();
        }

        Task task = randomNotCorrectTaskOpt.get();
        ArticleDto articleDto = articleService.getOne(task.getHintArticleId());

        return Optional.of(taskMapper.toDto(task, articleDto));
    }

    @Override
    public void saveHistory(Long taskId, String username, String userAnswer) {
        TaskHistory taskHistory = new TaskHistory();
        taskHistory.setTask(new Task(taskId));
        taskHistory.setUsername(username);
        taskHistory.setCreated(LocalDateTime.now());
        taskHistory.setUserAnswer(userAnswer);

        historyRepository.save(taskHistory);
    }

    @Override
    public void createSolvedTask(String username, Long taskId) {
        SolvedTask solvedTask = new SolvedTask();
        solvedTask.setTask(new Task(taskId));
        solvedTask.setUsername(username);
        solvedTask.setCreated(LocalDateTime.now());
        solvedTaskRepository.save(solvedTask);
    }

    @Override
    public Page<Task> findAll(PageRequest pageRequest) {
        return repository.findAll(pageRequest);
    }

    @Override
    public void delete(Long taskId) {
        repository.deleteById(taskId);
    }

    @Override
    public void save(Task task) {
        repository.save(task);
    }
}
