package ru.volsu.coursebot.handler;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.volsu.coursebot.dto.HandleResult;
import ru.volsu.coursebot.dto.UserContext;
import ru.volsu.coursebot.enums.BotSectionEnum;
import ru.volsu.coursebot.exceptions.BotException;
import ru.volsu.coursebot.exceptions.CoreException;

public interface MessageHandler {
    HandleResult handle(Update update, UserContext userContext) throws BotException, CoreException;

    BotSectionEnum getBotState();
}
