package ru.volsu.commons.dto;

import java.io.Serial;
import java.io.Serializable;

public class TaskDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -5509848997815705727L;

    public static final String ANSWER_MAPPING_LONG = "LONG";
    public static final String ANSWER_MAPPING_STRING = "STRING";

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
