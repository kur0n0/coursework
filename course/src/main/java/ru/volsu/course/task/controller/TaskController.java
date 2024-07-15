package ru.volsu.course.task.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.volsu.commons.dto.TaskDTO;
import ru.volsu.course.task.service.TaskService;

import java.util.Optional;

@RestController
@RequestMapping(value = "/api/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping(value = "/random")
    public Optional<TaskDTO> getRandomNotCorrectTask(@RequestParam String username) throws Exception {
        return taskService.findRandomNotCorrectTask(username);
    }

    @PostMapping(value = "/history")
    public void createTaskHistory(@RequestParam Long taskId,
                                  @RequestParam String username,
                                  @RequestParam String userAnswer) {
        taskService.saveHistory(taskId, username, userAnswer);
    }

    @PostMapping(value = "/solved")
    public void createSolvedTask(@RequestParam String username,
                                 @RequestParam Long taskId) {
        taskService.createSolvedTask(username, taskId);
    }
}
