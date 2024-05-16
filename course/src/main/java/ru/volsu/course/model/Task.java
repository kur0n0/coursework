package ru.volsu.course.model;

import ru.volsu.course.enums.AnswerMappingEnum;

import javax.persistence.*;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long taskId;

    private String question;

    private String answer;

    @Column(name = "hint_article_id")
    private Integer hintArticleId;

    @Enumerated(EnumType.STRING)
    @Column(name = "answer_mapping")
    private AnswerMappingEnum answerMapping;

    public Task(Long taskId) {
        this.taskId = taskId;
    }

    public Task() {

    }

    public void setTaskId(Long id) {
        this.taskId = id;
    }

    public Long getTaskId() {
        return taskId;
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

    public Integer getHintArticleId() {
        return hintArticleId;
    }

    public void setHintArticleId(Integer hintArticleId) {
        this.hintArticleId = hintArticleId;
    }
}
