package ru.volsu.coursebot.handler;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.volsu.coursebot.enums.BotSectionEnum;
import ru.volsu.coursebot.exceptions.BotException;
import ru.volsu.coursebot.exceptions.CoreException;

public interface MessageHandler {
    BotApiMethod<?> handle(Update update) throws BotException, CoreException;

    BotSectionEnum getBotState();
}
