package ru.volsu.coursebot.dto;

public class TaskDTO {
    private Long taskId;
    private ArticleDto hint;

    private String question;

    private String answer;

    private AnswerMappingEnum answerMapping;

    public TaskDTO() {
    }

    public ArticleDto getHint() {
        return hint;
    }

    public void setHint(ArticleDto hint) {
        this.hint = hint;
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

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
}
