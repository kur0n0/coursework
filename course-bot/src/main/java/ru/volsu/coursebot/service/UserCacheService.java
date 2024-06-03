package ru.volsu.coursebot.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.volsu.coursebot.enums.BotSectionEnum;
import ru.volsu.coursebot.enums.UserCommandEnum;

@Service
public class UserCacheService {

    @Cacheable(value = "BotSection", key = "#userId")
    public BotSectionEnum getBotSectionByUserId(Long userId) {
        return null;
    }

    @CachePut(value = "BotSection", key = "#userId")
    public BotSectionEnum setBotSectionForUser(Long userId, BotSectionEnum botSection) {
        return botSection;
    }

    @Cacheable(value = "UserCommand", key = "#userId")
    public UserCommandEnum getCommand(Long userId) {
        return null;
    }

    @CachePut(value = "UserCommand", key = "#userId")
    public UserCommandEnum putCommand(Long userId, UserCommandEnum userCommandEnum) {
        return userCommandEnum;
    }

    @CacheEvict(value = "UserCommand", key = "#userId")
    public void clearCommand(Long userId) {

    }
}
