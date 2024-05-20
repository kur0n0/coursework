package ru.volsu.course.dto;

import ru.volsu.course.enums.AnswerMappingEnum;
import ru.volsu.course.model.Task;

public class TaskDTO {

    private Long taskId;

    private String question;

    private String answer;

    private AnswerMappingEnum answerMapping;

    private ArticleDto hint;

    public TaskDTO(Task task, ArticleDto articleDto) {
        this.taskId = task.getTaskId();
        this.question = task.getQuestion();
        this.answer = task.getAnswer();
        this.answerMapping = task.getAnswerMapping();
        this.hint = articleDto;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public AnswerMappingEnum getAnswerMapping() {
        return answerMapping;
    }

    public void setAnswerMapping(AnswerMappingEnum answerMapping) {
        this.answerMapping = answerMapping;
    }

    public ArticleDto getHint() {
        return hint;
    }

    public void setHint(ArticleDto hint) {
        this.hint = hint;
    }
}
