package ru.volsu.coursebot.service;

import ru.volsu.coursebot.dto.ArticlePage;
import ru.volsu.coursebot.exceptions.CoreException;

public interface CourseCoreService {
    ArticlePage getPageByTag(Integer page, String tag) throws CoreException;

    ArticlePage getPageByTitle(Integer page, String title) throws CoreException;

    ArticlePage getPageFullTextSearch(Integer page, String query) throws CoreException;
}
