package ru.volsu.commons.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticlePageDto {

    private List<ArticleDto> content;

    private Integer currentPage;

    private Integer totalPages;

    public ArticlePageDto(List<ArticleDto> content, Integer currentPage, Integer totalPages) {
        this.content = content;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
    }

    public ArticlePageDto() {
    }

    public List<ArticleDto> getContent() {
        return content;
    }

    public void setContent(List<ArticleDto> content) {
        this.content = content;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }
}
