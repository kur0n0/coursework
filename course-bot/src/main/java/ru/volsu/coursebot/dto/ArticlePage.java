package ru.volsu.coursebot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ArticlePage implements Serializable {
    @Serial
    private static final long serialVersionUID = 3218439972342566556L;

//    public static final String SEARCH_BY_TAG_CACHE_NAME = "ArticleByTag";
//    public static final String SEARCH_BY_TITLE_CACHE_NAME = "ArticleByTitle";
//    public static final String SEARCH_BY_FULL_TEXT_CACHE_NAME = "ArticleByFullText";

    public ArticlePage() {
    }

    private List<ArticleDto> content;
    private Integer totalPages;
    private Integer currentPage;

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public List<ArticleDto> getContent() {
        return content;
    }

    public void setContent(List<ArticleDto> content) {
        this.content = content;
    }
}
