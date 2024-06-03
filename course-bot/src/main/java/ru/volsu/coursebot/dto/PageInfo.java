package ru.volsu.coursebot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.io.Serial;
import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PageInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = -7736002594512016344L;

    private Integer totalPages;
    private Integer currentPage;

    public PageInfo(Integer totalPages, Integer currentPage) {
        this.totalPages = totalPages;
        this.currentPage = currentPage;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    @JsonSetter(value = "number")
    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }
}
