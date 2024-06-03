package ru.volsu.coursebot.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.volsu.coursebot.dto.ArticlePage;
import ru.volsu.coursebot.dto.UserContext;

@Service
public class CacheService {

    @CachePut(value = "UserContext", key = "#userContext.userId")
    public UserContext updateUserContext(UserContext userContext) {
        return userContext;
    }

    @Cacheable(value = "UserContext", key = "#userId")
    public UserContext getUserContext(Long userId) {
        return null;
    }

    @CacheEvict(value = ArticlePage.SEARCH_BY_TAG_CACHE_NAME, allEntries = true)
    public void clearByTagCache() {}

    @CacheEvict(value = ArticlePage.SEARCH_BY_TITLE_CACHE_NAME, allEntries = true)
    public void clearByTitle() {}

    @CacheEvict(value = ArticlePage.SEARCH_BY_FULL_TEXT_CACHE_NAME, allEntries = true)
    public void clearByFullText() {}
}
