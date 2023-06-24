package ru.volsu.coursebot.service;

import ru.volsu.coursebot.dto.ArticlePage;

public interface CourseCoreService {
    ArticlePage getPageByTag(Integer page, String tag) throws Exception;

    ArticlePage getPageByTitle(Integer page, String title) throws Exception;
}
