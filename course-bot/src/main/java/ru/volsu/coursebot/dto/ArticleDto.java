package ru.volsu.coursebot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ArticleDto {

    private Integer articleId;

    private String text;

    private String title;

    private String tag;

    private List<FileDto> fileDtoList;

    public ArticleDto() {
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
