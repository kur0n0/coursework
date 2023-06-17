package ru.volsu.course.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleDto {

    private Integer articleId;

    private String text;

    private String title;

    private String tag;

    private List<FileDto> fileDtoList;

    public ArticleDto() {
    }

    public ArticleDto(Article article, FileDto[] fileDtos) {
        this.articleId = article.getArticleId();
        this.text = article.getText();
        this.title = article.getTitle();
        this.tag = article.getTag();
        this.fileDtoList = Arrays.asList(fileDtos);
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<FileDto> getFileDtoList() {
        return fileDtoList;
    }

    public void setFileDtoList(List<FileDto> fileDtoList) {
        this.fileDtoList = fileDtoList;
    }
}
