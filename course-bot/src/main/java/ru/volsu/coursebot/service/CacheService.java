package ru.volsu.coursebot.service;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
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
}
