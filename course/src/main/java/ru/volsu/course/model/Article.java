package ru.volsu.course.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
public class Article implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer articleId;

    private String text;

    private String title;

    private String tag;

    public void setArticleId(Integer id) {
        this.articleId = id;
    }

    public Integer getArticleId() {
        return articleId;
    }
}
