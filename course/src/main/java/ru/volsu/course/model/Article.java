package ru.volsu.course.model;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Indexed
@Table(name = "articles")
public class Article implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Integer articleId;

    @Field
    private String text;

    @Field
    private String title;

    @Field
    private String tag;

    @ElementCollection
    @CollectionTable(name = "files", joinColumns = @JoinColumn(name = "article_id"))
    @Column(name = "file_uuid")
    private List<String> filesUuidList;

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

    public List<String> getFilesUuidList() {
        return filesUuidList;
    }

    public void setFilesUuidList(List<String> filesUuidList) {
        this.filesUuidList = filesUuidList;
    }
}
