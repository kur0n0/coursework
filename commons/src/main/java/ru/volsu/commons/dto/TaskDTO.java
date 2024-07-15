package ru.volsu.commons.dto;

public class TaskDTO {

    private Long taskId;

    private String question;

    private String answer;

    private String answerMapping;

    private ArticleDto hint;

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

    public String getAnswerMapping() {
        return answerMapping;
    }

    public void setAnswerMapping(String answerMapping) {
        this.answerMapping = answerMapping;
    }

    public ArticleDto getHint() {
        return hint;
    }

    public void setHint(ArticleDto hint) {
        this.hint = hint;
    }
}
