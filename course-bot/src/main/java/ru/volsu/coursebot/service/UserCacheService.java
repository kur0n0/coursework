package ru.volsu.coursebot.service;

import org.springframework.stereotype.Service;
import ru.volsu.coursebot.enums.BotSectionEnum;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserCacheService {

    private Map<Long, BotSectionEnum> userState = new HashMap<>();

    public BotSectionEnum getBotSectionByUserId(Long userId) {
        return userState.getOrDefault(userId, BotSectionEnum.MAIN_MENU);
    }

    public void setBotSectionForUser(Long userId, BotSectionEnum botSection) {
        if (userState.containsKey(userId)) {
            userState.replace(userId, botSection);
        } else {
            userState.put(userId, botSection);
        }
    }
}
