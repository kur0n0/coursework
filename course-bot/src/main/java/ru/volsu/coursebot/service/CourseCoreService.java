package ru.volsu.coursebot.service;

import ru.volsu.coursebot.dto.ArticleDto;

import java.util.List;

public interface CourseCoreService {
    List<ArticleDto> getPageByTag(Integer page, String tag) throws Exception;
}
