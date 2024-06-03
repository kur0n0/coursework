package ru.volsu.coursebot.dto;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

public class HandleResult {
    private BotApiMethod<?> botAction;
    private UserContext userContext;

    public HandleResult(BotApiMethod<?> botAction, UserContext userContext) {
        this.botAction = botAction;
        this.userContext = userContext;
    }

    public BotApiMethod<?> getBotAction() {
        return botAction;
    }

    public void setBotAction(BotApiMethod<?> botAction) {
        this.botAction = botAction;
    }

    public UserContext getUserContext() {
        return userContext;
    }

    public void setUserContext(UserContext userContext) {
        this.userContext = userContext;
    }
}
