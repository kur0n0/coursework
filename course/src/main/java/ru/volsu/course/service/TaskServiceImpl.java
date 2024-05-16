package ru.volsu.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.volsu.course.dao.SolvedTaskRepository;
import ru.volsu.course.dao.TaskHistoryRepository;
import ru.volsu.course.dao.TaskRepository;
import ru.volsu.course.dto.ArticleDto;
import ru.volsu.course.dto.TaskDTO;
import ru.volsu.course.model.SolvedTask;
import ru.volsu.course.model.Task;
import ru.volsu.course.model.TaskHistory;

import javax.persistence.EntityManager;
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
    private EntityManager entityManager;

    @Override
    public Optional<TaskDTO> findRandomNotCorrectTask(String username) throws Exception {
//        Optional<Task> randomNotCorrectTaskOpt = entityManager.createQuery(
//                        "select t from Task t left join TaskHistory th on t.taskId = th.task.taskId " +
//                                "where th.taskHistoryId is null or (th.username != :username)" +
//                                "order by random()", Task.class
//                ).setParameter("username", username)
//                .getResultStream()
//                .findAny();
        Optional<Task> randomNotCorrectTaskOpt = repository.findRandomNotSolvedTask(username);
        if (randomNotCorrectTaskOpt.isEmpty()) {
            return Optional.empty();
        }

        Task task = randomNotCorrectTaskOpt.get();
        ArticleDto articleDto = articleService.getOne(task.getHintArticleId());

        return Optional.of(new TaskDTO(task, articleDto));
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
}
